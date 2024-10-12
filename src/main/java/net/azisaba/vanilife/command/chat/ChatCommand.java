package net.azisaba.vanilife.command.chat;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.chat.IChat;
import net.azisaba.vanilife.command.subcommand.ParentCommand;
import net.azisaba.vanilife.ui.GroupChatSettingsUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "/chat";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0)
        {
            User user = User.getInstance(player);
            IChat chat = user.getChat();

            if (chat instanceof GroupChat group && group.getOwner() == User.getInstance(player))
            {
                new GroupChatSettingsUI(player, group);
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    protected void register()
    {
        this.register(new ChatCreateSubcommand());
        this.register(new ChatDeleteSubcommand());
        this.register(new ChatInviteSubcommand());
        this.register(new ChatJoinSubcommand());
        this.register(new ChatKickSubcommand());
        this.register(new ChatQuitSubcommand());
    }
}
