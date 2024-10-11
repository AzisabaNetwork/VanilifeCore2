package net.azisaba.vanilife.command;

import net.azisaba.vanilife.chat.Chat;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
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
            Chat chat = Chat.getInstance(user);

            if (chat != null)
            {
                chat.unfocus(user);
            }

            sender.sendMessage(Language.translate("cmd.chat.unfocused", player).color(NamedTextColor.GREEN));
            return true;
        }

        Chat chat = null;

        if (args[0].startsWith("@") && args[0].length() > 1)
        {
            Player mention = Bukkit.getPlayer(args[0].substring(1));

            if (mention == null)
            {
                sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
                return true;
            }

            if (User.getInstance(mention).isBlock(user))
            {
                sender.sendMessage(Language.translate("cmd.chat.cant-dm", player).color(NamedTextColor.RED));
                return true;
            }

            chat = Chat.getInstances().stream().filter(dm -> dm.isDirect() && dm.isMember(player) && dm.isMember(mention)).findFirst().orElse(null);

            if (chat == null)
            {
                chat = new Chat(user, User.getInstance(mention));
            }
        }
        else
        {
            chat = Chat.getInstance(args[0]);
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
            chat.focus(user);
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

        chat.chat(player, message.toString());
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

        Chat.getInstances().stream()
                .filter(chat -> ! chat.isDirect() && chat.isMember(user))
                .forEach(chat -> suggest.add(chat.getName()));

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != player)
                .forEach(p -> suggest.add("@" + p.getName()));

        return suggest;
    }
}
