package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.SeasonUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
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

        if (args.length == 0)
        {
            Bukkit.dispatchCommand(player, "vanilife:worlds");
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /world <world>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (user.getStatus() == UserStatus.JAILED)
        {
            return true;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(args[0]);

        if (world == null)
        {
            sender.sendMessage(Language.translate("cmd.world.undefined", player, "world=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        VanilifeWorld currentWorld = VanilifeWorld.getInstance(player.getWorld());

        if (currentWorld != null)
        {
            currentWorld.setLocation(player, player.getLocation());
        }

        player.teleport(world.getLocation(player));

        player.sendMessage(Language.translate("cmd.world.teleported", player, "world=" + ComponentUtility.asString(Language.translate("ui.worlds." + world.getSeason().name().toLowerCase(), player))).color(SeasonUtility.getSeasonColor(world.getSeason())).decorate(TextDecoration.BOLD));
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
