package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.*;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class EmoteCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <emote>").color(NamedTextColor.RED));
            return true;
        }

        List<ISubscription> filteredSubscriptions = Subscriptions.products().stream().filter(p -> p instanceof IEmoteSubscription).filter(e -> ((IEmoteSubscription) e).getEmoteName().equals(args[0])).toList();

        if (filteredSubscriptions.isEmpty() || ! (filteredSubscriptions.getFirst() instanceof IEmoteSubscription emote))
        {
            sender.sendMessage(Language.translate("cmd.emote.undefined", player, "emote=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (! user.hasSubscription(emote))
        {
            sender.sendMessage(Language.translate("cmd.emote.cant", player, "subscription=" + ComponentUtility.asString(emote.getDisplayName(Language.getInstance(user)))).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return true;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                emote.use(player);
            }
        }.runTask(Vanilife.getPlugin());

        sender.sendMessage(Language.translate("cmd.emote.used", player, "emote=" + ComponentUtility.asString(emote.getDisplayName(Language.getInstance(user)))));
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

        User.getInstance(player).getSubscriptions().stream().filter(i -> i instanceof IEmoteSubscription).forEach(emote ->suggest.add(((IEmoteSubscription) emote).getEmoteName()));

        return suggest;
    }
}
