package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.util.SubscriptionUtility;
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

public class SubscribeCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /subscribe <subscription>").color(NamedTextColor.RED));
            return true;
        }

        ISubscription subscription = Subscriptions.valueOf(args[0]);

        if (subscription == null)
        {
            sender.sendMessage(Language.translate("cmd.subscribe.undefined", player, "subscription=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        double rest = 1.0d - SubscriptionUtility.getProgress();
        int cost = (int) (subscription.getCost() * rest);

        User user = User.getInstance(player);

        if (user.getMola() < cost)
        {
            sender.sendMessage(Language.translate("cmd.subscribe.shortage", player, "need=" + (cost - user.getMola())).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        user.setMola(user.getMola() - cost);
        user.subscribe(subscription);
        sender.sendMessage(subscription.getDisplayName(Language.getInstance(user)).color(NamedTextColor.GOLD).append(Component.text(" を購入しました").color(NamedTextColor.GREEN)).append(Component.text(String.format(" (%s Mola × %s %%)", subscription.getCost(), (int) (rest * 100))).color(NamedTextColor.GRAY)));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.9f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
