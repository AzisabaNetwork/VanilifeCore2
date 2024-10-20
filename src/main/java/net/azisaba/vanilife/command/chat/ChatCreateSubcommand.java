package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatCreateSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "create";
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

        User user = User.getInstance(player);

        if (user.getTrustRank().getLevel() < TrustRank.USER.getLevel())
        {
            sender.sendMessage(Language.translate("cmd.chat.create.cant", player).color(NamedTextColor.RED));
            return;
        }

        if (GroupChat.getInstance(args[0]) != null)
        {
            sender.sendMessage(Language.translate("cmd.chat.create.already", player).color(NamedTextColor.RED));
            return;
        }

        if (! GroupChat.namepattern.matcher(args[0]).matches())
        {
            sender.sendMessage(Language.translate("cmd.chat.create.invalid", player).color(NamedTextColor.RED));
            return;
        }

        if (args[0].length() < 3)
        {
            sender.sendMessage(Language.translate("cmd.chat.create.limit-under", player).color(NamedTextColor.RED));
            return;
        }

        if (10 < args[0].length())
        {
            sender.sendMessage(Language.translate("cmd.chat.create.limit-over", player).color(NamedTextColor.RED));
            return;
        }

        int chats = (int) GroupChat.getInstances().stream().filter(chat -> chat.getOwner() == user).count();

        if (3 < chats && ! UserUtility.isAdmin(user))
        {
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            sender.sendMessage(Language.translate("cmd.chat.create.rate-limiting", player).color(NamedTextColor.RED));
            return;
        }

        new GroupChat(args[0], user);
        sender.sendMessage(Language.translate("cmd.chat.create.created", player).color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
