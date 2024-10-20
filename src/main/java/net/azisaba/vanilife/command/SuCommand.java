package net.azisaba.vanilife.command;

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

public class SuCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /su").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);
        Sara oldSara = user.getSara();
        Sara newSara = UserUtility.calculateSara(player, oldSara.isPermission());

        if (oldSara == newSara)
        {
            sender.sendMessage(Component.text("利用可能な権限が見つかりませんでした").color(NamedTextColor.RED));
            return true;
        }

        user.setSara(newSara);

        Component permission = Component.text(newSara.isPermission() ? "Admin Mode" : "Player Mode").color(newSara.getColor());
        sender.sendMessage(permission.appendSpace().append(Component.text("に切り替えました").color(NamedTextColor.GRAY)));
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
