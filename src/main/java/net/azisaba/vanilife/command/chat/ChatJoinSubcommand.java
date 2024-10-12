package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.ChatInvite;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatJoinSubcommand implements Subcommand
{
    @Override
    public @NotNull String getName()
    {
        return "join";
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
            sender.sendMessage(Component.text("Correct syntax: //chat join <chat>").color(NamedTextColor.RED));
            return;
        }

        GroupChat chat = GroupChat.getInstance(args[0]);

        if (chat == null)
        {
            sender.sendMessage(Language.translate("cmd.chat.not-found", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (chat.inScope(user))
        {
            chat.addMember(user);
            chat.getOnline().forEach(member -> member.asPlayer().sendMessage(Component.text(chat.getName() + " > ").color(NamedTextColor.BLUE).append(user.getName(member)).append(Language.translate("msg.chat.join", member).color(NamedTextColor.GRAY))));
            return;
        }

        if (user.getRequests().stream().noneMatch(r -> r.match(ChatInvite.class, chat.getOwner().asPlayer())))
        {
            sender.sendMessage(Language.translate("cmd.chat.join.cant", player).color(NamedTextColor.RED));
            return;
        }

        user.getRequests().stream().filter(r -> r.match(ChatInvite.class, chat.getOwner().asPlayer())).toList().getFirst().onAccept();
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
