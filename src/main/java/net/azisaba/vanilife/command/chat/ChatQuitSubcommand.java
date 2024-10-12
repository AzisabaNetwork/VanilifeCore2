package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatQuitSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "quit";
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
            sender.sendMessage(Component.text("Correct syntax: //chat quit"));
            return;
        }

        User user = User.getInstance(player);
        IChat chat = user.getChat();

        if (! (chat instanceof GroupChat group))
        {
            sender.sendMessage(Language.translate("cmd.chat.not-focused", player).color(NamedTextColor.RED));
            return;
        }

        if (user == group.getOwner())
        {
            sender.sendMessage(Language.translate("cmd.chat.quit.owner-cant", player).color(NamedTextColor.RED));
            return;
        }

        group.removeMember(user);
        sender.sendMessage(Language.translate("cmd.chat.quit.quited", player, "chat=" + group.getName()).color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player) || args.length != 1)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        GroupChat.getInstances().stream()
                .filter(chat -> chat.isMember(player))
                .forEach(chat -> suggest.add(chat.getName()));

        return suggest;
    }
}
