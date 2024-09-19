package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.MathUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FriendListCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (1 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " [page]").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 1 && ! MathUtility.isInt(args[0]))
        {
            sender.sendMessage(Component.text("The argument page must be an integer.").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        ArrayList<User> friends = new ArrayList<>();
        friends.addAll(user.getFriends().stream().filter(f -> f.isOnline()).toList());
        friends.addAll(user.getFriends().stream().filter(f -> ! f.isOnline()).toList());

        int page = (args.length == 0) ? 1 : Integer.valueOf(args[0]);
        int pages = friends.size() / 8 + ((friends.size() % 8 != 0) ? 1 : 0);

        if (page <= 0 || pages < page)
        {
            sender.sendMessage(Language.translate("cmd.friendlist.empty", player).color(NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        sender.sendMessage(Component.text(CLI.getSpaces(12) + "<< ").color((1 < page) ? NamedTextColor.BLUE : NamedTextColor.DARK_GRAY).clickEvent(ClickEvent.runCommand((1 < page) ? String.format("/friendlist %d", page - 1) : ""))
                .append(Component.text(String.format("Friends (%d/%d)", page, pages)).color(NamedTextColor.YELLOW))
                .append(Component.text(" >>").color((page < pages) ? NamedTextColor.BLUE : NamedTextColor.DARK_GRAY).clickEvent(ClickEvent.runCommand((page < pages) ? String.format("/friendlist %d", page + 1) : ""))));

        for (User friend : friends.subList((page - 1) * 8, Math.min(page * 8, friends.size())))
        {
            sender.sendMessage(Component.text("● ").color(friend.isOnline() ? NamedTextColor.GREEN : NamedTextColor.RED)
                    .append(friend.getName()));
        }

        sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
