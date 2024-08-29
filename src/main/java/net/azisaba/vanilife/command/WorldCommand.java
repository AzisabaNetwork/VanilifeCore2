package net.azisaba.vanilife.command;

import net.azisaba.vanilife.vwm.VanilifeWorld;
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

public class WorldCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /world <world>").color(NamedTextColor.RED));
            return true;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(args[0]);

        if (world == null)
        {
            sender.sendMessage(Component.text(String.format("%s は未定義のワールドです", args[0])).color(NamedTextColor.RED));
            return true;
        }

        VanilifeWorld currentWorld = VanilifeWorld.getInstance(player.getWorld());

        if (currentWorld != null)
        {
            currentWorld.setLocation(player, player.getLocation());
        }

        player.teleport(world.getLocation(player));

        player.sendMessage(Component.text(String.format("%s にテレポートしました！", args[0])).color(NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            VanilifeWorld.getInstances().forEach(w -> suggest.add(w.getName()));
        }

        return suggest;
    }
}
