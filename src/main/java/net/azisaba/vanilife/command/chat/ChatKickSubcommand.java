package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.chat.Chat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatKickSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "kick";
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
            sender.sendMessage(Component.text("Correct syntax: //chat kick <player>").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);
        Chat chat = Chat.getInstance(user);

        if (chat == null)
        {
            sender.sendMessage(Language.translate("cmd.chat.not-focused", player).color(NamedTextColor.RED));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
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

            User target = User.getInstance(args[0]);

            if (chat.getOwner() != user)
            {
                sender.sendMessage(Language.translate("cmd.chat.permission-error", player).color(NamedTextColor.RED));
                return;
            }

            if (target == chat.getOwner())
            {
                sender.sendMessage(Language.translate("cmd.chat.kick.owner-cant", player).color(NamedTextColor.RED));
                return;
            }

            chat.removeMember(target);
            sender.sendMessage(Language.translate("cmd.chat.kick.kicked", player, "name=" + target.getPlaneName()).color(NamedTextColor.GREEN));
        });
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player) || args.length != 1)
        {
            return List.of();
        }

        User user = User.getInstance(player);
        Chat chat = Chat.getInstance(user);

        if (chat == null || chat.getOwner() != user)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();
        chat.getMembers().forEach(member -> suggest.add(member.getPlaneName()));
        return suggest;
    }
}
