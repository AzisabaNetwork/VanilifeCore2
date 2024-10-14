package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.penalty.Jail;
import net.azisaba.vanilife.ui.ConfirmUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JailCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0 || 2 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /jail <player> [details]").color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isModerator(sender) && sender instanceof Player player && User.getInstance(player).getTrustRank().getLevel() < TrustRank.NEW.getLevel())
        {
            sender.sendMessage(Language.translate("jail.permission-error", player).color(NamedTextColor.RED));
            return true;
        }

        Language lang = (sender instanceof Player player) ? Language.getInstance(player) : Language.getInstance("ja-jp");

        if (lang == null)
        {
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

            if (uuid == null)
            {
                sender.sendMessage(lang.translate("msg.not-found.player", "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            if (! UserUtility.exists(uuid))
            {
                sender.sendMessage(lang.translate("msg.not-found.user", "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            User target = User.getInstance(args[0]);

            if (UserUtility.isModerator(target))
            {
                sender.sendMessage(lang.translate("jail.cant").color(NamedTextColor.RED));
                return;
            }

            if (sender instanceof Player player && target == User.getInstance(player))
            {
                sender.sendMessage(lang.translate("jail.cant-yourself").color(NamedTextColor.RED));
                return;
            }

            Jail jail = Jail.getInstance(target);

            if (jail != null)
            {
                if (! (sender instanceof Player player))
                {
                    sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
                    return;
                }

                if (jail.isVoted(User.getInstance(player)))
                {
                    sender.sendMessage(lang.translate("jail.already").color(NamedTextColor.RED));
                    return;
                }

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
                    new ConfirmUI(player, () -> {
                        jail.vote(player);
                        sender.sendMessage(Component.text().build());
                        sender.sendMessage(lang.translate("jail.voted").color(NamedTextColor.GREEN));
                        sender.sendMessage(Component.text().build());
                    }, () -> {
                        sender.sendMessage(Component.text().build());
                        sender.sendMessage(lang.translate("jail.cancelled").color(NamedTextColor.RED));
                        sender.sendMessage(Component.text().build());
                    });
                });

                return;
            }

            if (args.length != 2)
            {
                sender.sendMessage(Component.text("Correct syntax: /jail <player> <details>").color(NamedTextColor.RED));
                return;
            }

            if (args[1].length() < 8)
            {
                sender.sendMessage(lang.translate("jail.limit-under").color(NamedTextColor.RED));
                return;
            }

            if (30 < args[1].length())
            {
                sender.sendMessage(lang.translate("jail.limit-over").color(NamedTextColor.RED));
                return;
            }

            if (UserUtility.isModerator(sender))
            {
                Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Jail 通知")
                        .addField("対象者", String.format("%s (%s)", target.getPlaneName(), target.getId()), false)
                        .addField("実行者", sender instanceof Player player ? String.format("%s (%s)", player.getName(), player.getUniqueId()) : sender.getName(), false)
                        .addField("詳細", args[1], false)
                        .build()).queue();

                target.jail();
                return;
            }

            new Jail(target, args[1]);
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .filter(p -> p.getName().startsWith(args[0]))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
