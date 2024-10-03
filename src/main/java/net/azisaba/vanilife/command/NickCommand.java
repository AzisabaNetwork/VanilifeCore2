package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NickCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        Sara sara = UserUtility.getSara(player);

        if (sara.level < Sara.$10000YEN.level)
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (args.length == 0)
        {
            user.setNick(null);
            sender.sendMessage(Language.translate("cmd.nick.reset", player).color(NamedTextColor.GREEN));
        }
        else if (args.length == 1)
        {
            if (args[0].length() < 3)
            {
                sender.sendMessage(Language.translate("cmd.nick.limit-under", player).color(NamedTextColor.RED));
                player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return true;
            }

            if (16 < args[0].length())
            {
                sender.sendMessage(Language.translate("cmd.nick.limit-over", player).color(NamedTextColor.RED));
                player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return true;
            }

            user.setNick("~" + args[0]);
            sender.sendMessage(Language.translate("cmd.nick.changed", player, "nick=" + args[0].replace("&", "＆")).color(NamedTextColor.GREEN));
        }
        else
        {
            sender.sendMessage(Component.text("Correct syntax: /nick [name]").color(NamedTextColor.RED));
            return true;
        }

        player.displayName(user.getName());
        player.playerListName(user.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
