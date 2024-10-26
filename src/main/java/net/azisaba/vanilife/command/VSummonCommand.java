package net.azisaba.vanilife.command;

import net.azisaba.vanilife.entity.VanilifeEntities;
import net.azisaba.vanilife.entity.VanilifeEntity;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class VSummonCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (! UserUtility.isAdmin(player))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /vsummon <entity>").color(NamedTextColor.RED));
            return true;
        }

        Class<? extends VanilifeEntity<?>> clazz = VanilifeEntities.registry.get(args[0]);

        if (clazz == null)
        {
            sender.sendMessage(Component.text(args[0] + " は未定義のエンティティです").color(NamedTextColor.RED));
            return true;
        }

        try
        {
            clazz.getConstructor(Location.class).newInstance(player.getLocation());
            sender.sendMessage(Component.text(args[0] + " を召喚しました"));
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            sender.sendMessage(Component.text(args[0] + " の召喚に失敗しました: " + e.getLocalizedMessage()).color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player) || ! UserUtility.isAdmin(sender) || args.length != 1)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();
        VanilifeEntities.registry.values().forEach(entity -> suggest.add(entity.getName()));
        return suggest;
    }
}
