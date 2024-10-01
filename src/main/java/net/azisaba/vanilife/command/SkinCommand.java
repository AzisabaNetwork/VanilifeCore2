package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.SkinUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.Skin;
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

import java.util.ArrayList;
import java.util.List;

public class SkinCommand implements CommandExecutor, TabCompleter
{
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
            new SkinUI(player);
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /skin <avatar>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (user.getSkins().stream().noneMatch(s -> s.getName().equals(args[0])))
        {
            sender.sendMessage(Language.translate("cmd.skin.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        Skin skin = user.getSkins().stream().filter(s -> s.getName().equals(args[0])).toList().getFirst();

        if (user.getSkin() == skin)
        {
            sender.sendMessage(Language.translate("cmd.skin.already", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        user.setSkin(skin);

        sender.sendMessage(Language.translate("cmd.skin.changed", player).color(NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
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

        User user = User.getInstance(player);
        List<String> suggest = new ArrayList<>();
        user.getSkins().forEach(skin -> suggest.add(skin.getName()));

        return suggest;
    }
}
