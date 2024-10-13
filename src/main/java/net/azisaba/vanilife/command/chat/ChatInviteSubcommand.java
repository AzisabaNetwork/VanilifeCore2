package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.ChatInvite;
import net.azisaba.vanilife.util.Watch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatInviteSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "invite";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.DEFAULT;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: //chat invite <player>").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);
        IChat chat = user.getChat();

        if (! (chat instanceof GroupChat group))
        {
            sender.sendMessage(Language.translate("cmd.chat.not-focused", player).color(NamedTextColor.RED));
            return;
        }

        if (group.getOwner() != user)
        {
            sender.sendMessage(Language.translate("cmd.chat.permission-error", player).color(NamedTextColor.RED));
            return;
        }

        Player to = Bukkit.getPlayer(args[0]);

        if (to == null)
        {
            sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
            return;
        }

        if (to == player)
        {
            sender.sendMessage(Language.translate("cmd.chat.invite.cant-yourself", player).color(NamedTextColor.RED));
            return;
        }

        if (User.getInstance(to).isBlock(user))
        {
            sender.sendMessage(Language.translate("cmd.chat.invite.cant", player).color(NamedTextColor.RED));
            return;
        }

        new ChatInvite(group, to);
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
