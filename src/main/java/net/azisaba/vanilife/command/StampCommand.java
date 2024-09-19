package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.*;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StampCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /stamp <stamp>").color(NamedTextColor.RED));
            return true;
        }

        ISubscription subscription = Subscriptions.valueOf(args[0]);

        if (! (subscription instanceof IStampSubscription stamp))
        {
            sender.sendMessage(Language.translate("cmd.stamp.undefined", player, "stamp=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (! user.hasSubscription(subscription))
        {
            sender.sendMessage(Language.translate("cmd.stamp.cant", player, "subscription=" + ComponentUtility.getAsString(subscription.getDisplayName(Language.getInstance(user)))).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        Location location = player.getLocation().add(0, 3, 0);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                stamp.rendering(location);
            }
        }.runTask(Vanilife.getPlugin());

        sender.sendMessage(Language.translate("cmd.stamp.used", player, "stamp=" + ComponentUtility.getAsString(stamp.getDisplayName(Language.getInstance(user)))));
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
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

        List<String> suggest = new ArrayList<>();

        User.getInstance(player).getSubscriptions().stream().filter(i -> i instanceof IStampSubscription).toList().forEach(stamp -> suggest.add(stamp.getName()));

        return suggest;
    }
}
