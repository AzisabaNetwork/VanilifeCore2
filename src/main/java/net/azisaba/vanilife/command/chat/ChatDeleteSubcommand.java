package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.Chat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.ConfirmUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatDeleteSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "delete";
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

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: //chat delete").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);
        Chat chat = Chat.getInstance(user);

        if (chat == null)
        {
            sender.sendMessage(Language.translate("cmd.chat.not-focused", player).color(NamedTextColor.RED));
            return;
        }

        if (chat.getOwner() != user)
        {
            sender.sendMessage(Language.translate("cmd.chat.permission-error", player).color(NamedTextColor.RED));
            return;
        }

        new ConfirmUI(player, () -> {
            chat.delete();
            sender.sendMessage(Language.translate("cmd.chat.delete.deleted", player).color(NamedTextColor.GREEN));
        }, () -> sender.sendMessage(Language.translate("cmd.chat.delete.cancelled", player).color(NamedTextColor.RED)));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
