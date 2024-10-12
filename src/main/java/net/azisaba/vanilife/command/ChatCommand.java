package net.azisaba.vanilife.command;

import net.azisaba.vanilife.chat.DirectChat;
import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (args.length == 0)
        {
            IChat chat = user.getChat();

            if (chat == null)
            {
                sender.sendMessage(Language.translate("cmd.chat.already-unfocused", player).color(NamedTextColor.RED));
                return true;
            }

            user.setChat(null);
            sender.sendMessage(Language.translate("cmd.chat.unfocused", player).color(NamedTextColor.GREEN));
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);
            return true;
        }

        IChat chat;

        if (args[0].startsWith("@") && 1 < args[0].length())
        {
            Player mention = Bukkit.getPlayer(args[0].substring(1));

            if (mention == null)
            {
                sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
                return true;
            }

            User target = User.getInstance(mention);

            if (target.isBlock(user))
            {
                sender.sendMessage(Language.translate("cmd.chat.cant-dm", player).color(NamedTextColor.RED));
                return true;
            }

            chat = DirectChat.getInstance(user, target);
        }
        else
        {
            chat = GroupChat.getInstance(args[0]);
        }

        if (chat == null)
        {
            sender.sendMessage(Language.translate("cmd.chat.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        if (! chat.isMember(user))
        {
            sender.sendMessage(Language.translate("cmd.chat.permission-error", player).color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 1)
        {
            if (user.getChat() == chat)
            {
                sender.sendMessage(Language.translate("cmd.chat.already", player).color(NamedTextColor.RED));
                return true;
            }

            user.setChat(chat);
            sender.sendMessage(Language.translate("cmd.chat.focused", player, "chat=" + args[0]).color(NamedTextColor.GREEN));
            return true;
        }

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i ++)
        {
            if (i < args.length - 1)
            {
                message.append(' ');
            }

            message.append(args[i]);
        }

        chat.send(user, message.toString());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        if (args.length != 1)
        {
            return List.of();
        }

        final List<String> suggest = new ArrayList<>();

        User user = User.getInstance(player);

        GroupChat.getInstances().stream()
                .filter(chat -> chat.isMember(user) && chat.getName().startsWith(args[0]))
                .forEach(chat -> suggest.add(chat.getName()));

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != player && (("@" + p.getName()).startsWith(args[0]) || p.getName().startsWith(args[0])))
                .forEach(p -> suggest.add("@" + p.getName()));

        return suggest;
    }
}
