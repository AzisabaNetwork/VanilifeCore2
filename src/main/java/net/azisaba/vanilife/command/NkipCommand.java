package net.azisaba.vanilife.command;

import net.azisaba.vanilife.nkip.NkipVote;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NkipCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /nkip <world>").color(NamedTextColor.RED));
            return true;
        }

        World world = args.length == 1 ? Bukkit.getWorld(args[0]) : player.getWorld();

        NkipVote vote = NkipVote.getInstance(world);

        if (vote == null)
        {
            sender.sendMessage(Language.translate("cmd.nkip.not-found", player).color(NamedTextColor.RED));
            return true;
        }

        if (! player.getWorld().equals(world))
        {
            sender.sendMessage(Language.translate("cmd.nkip.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (vote.isVoter(player))
        {
            sender.sendMessage(Language.translate("cmd.nkip.already", player).color(NamedTextColor.RED));
            return true;
        }

        vote.vote(player);
        sender.sendMessage(Language.translate("cmd.nkip.voted", player).color(NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
