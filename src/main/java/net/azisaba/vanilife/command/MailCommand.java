package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.mail.Mail;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.MathUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailCommand implements CommandExecutor, TabCompleter
{
    private static Component getComponent(Mail mail)
    {
        Component component = Component.text("");
        String[] rows = mail.getMessage().split("\n");

        for (int i = 0; i < rows.length; i ++)
        {
            String row = rows[i];
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < row.length(); j ++)
            {
                sb.append(row.charAt(j));

                if (16 <= sb.length())
                {
                    component = component.append(Component.text(sb.toString()));
                    sb = new StringBuilder();
                }
            }

            if (sb.length() % 16 != 0)
            {
                component = component.append(Component.text(sb.toString()));
            }

            if (i + 1 != rows.length)
            {
                component = component.appendNewline();
            }
        }

        return component;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull final String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (3 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /mail [player] [subject] [message]").color(NamedTextColor.RED));
            return true;
        }

        User from = User.getInstance(player);

        if (args.length == 1 && ! MathUtility.isInt(args[0]))
        {
            sender.sendMessage(Component.text("The argument page must be an integer.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length <= 1)
        {
            List<Mail> mails = from.getMails();

            int page = args.length == 1 ? Integer.parseInt(args[0]) : 0;
            int pages = mails.size() / 8 + ((mails.size() % 8 != 0) ? 1 : 0);

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

            sender.sendMessage(Component.text(CLI.getSpaces(12) + "<< ").color((1 < page) ? NamedTextColor.BLUE : NamedTextColor.DARK_GRAY).clickEvent(ClickEvent.runCommand((1 < page) ? String.format("/mail %d", page - 1) : ""))
                    .append(Component.text(String.format("Mails (%d/%d)", page, pages)).color(NamedTextColor.YELLOW))
                    .append(Component.text(" >>").color((page < pages) ? NamedTextColor.BLUE : NamedTextColor.DARK_GRAY).clickEvent(ClickEvent.runCommand((page < pages) ? String.format("/mail %d", page + 1) : ""))));

            if (page <= 0 || pages < page)
            {
                sender.sendMessage(Language.translate("cmd.mail.empty", player).color(NamedTextColor.GRAY));
            }
            else
            {
                for (Mail mail: mails.subList((page - 1) * 8, Math.min(page * 8, mails.size())))
                {
                    sender.sendMessage(Component.text(Vanilife.sdf1.format(mail.getDate()) + " ").color(NamedTextColor.GRAY)
                            .append((mail.isRead() ? Language.translate("cmd.mail.read", player) : Language.translate("cmd.mail.unread", player)).color(mail.isRead() ? NamedTextColor.GREEN : NamedTextColor.RED))
                            .append(Component.text((mail.getFrom() == from) ? "To: " + mail.getTo().getPlaneName() + " " : "From: " + mail.getFrom().getPlaneName() + " ").color(NamedTextColor.WHITE)
                            .append(Language.translate("cmd.mail.subject", player).append(Component.text(mail.getSubject()).color(NamedTextColor.WHITE).hoverEvent(HoverEvent.showText(MailCommand.getComponent(mail)))))));

                    if (mail.getTo() == from)
                    {
                        mail.read();
                    }
                }
            }

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
            return true;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

                if (uuid == null)
                {
                    sender.sendMessage(Language.translate("msg.not-found.player", player, "name=" + args[0]).color(NamedTextColor.RED));
                    return;
                }

                if (! UserUtility.exists(uuid))
                {
                    sender.sendMessage(Language.translate("msg.not-found.user", player, "name=" + args[0]).color(NamedTextColor.RED));
                    return;
                }

                User to = User.getInstance(args[0]);
                String subject = (args.length == 3) ? args[1] : "件名なし";
                String message = (args.length == 3) ? args[2] : args[1];

                if (! to.getSettings().MAIL.isWithinScope(from) || to.isBlock(from))
                {
                    sender.sendMessage(Language.translate("cmd.mail.cant", player, "name=" + to.getPlaneName()).color(NamedTextColor.RED));
                    return;
                }

                if (16 < subject.length())
                {
                    sender.sendMessage(Language.translate("cmd.mail.limit-over.subject", player).color(NamedTextColor.RED));
                    return;
                }

                if (250 < message.length())
                {
                    sender.sendMessage(Language.translate("cmd.mail.limit-over.message", player).color(NamedTextColor.RED));
                    return;
                }

                new Mail(from, to, subject, message.replace("\n", "\\n"));
                sender.sendMessage(Language.translate("cmd.mail.sent", player, "name=" + to.getPlaneName()).color(NamedTextColor.GREEN));
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        if (args.length == 2)
        {
            suggest.add("[subject] / <message>");
        }

        if (args.length == 3)
        {
            suggest.add("<message>");
        }

        return suggest;
    }
}
