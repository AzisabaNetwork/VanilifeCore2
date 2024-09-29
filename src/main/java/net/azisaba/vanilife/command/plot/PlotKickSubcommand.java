package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlotKickSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "kick";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.DEFAULT;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: //plot kick <player>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Language.translate("cmd.plot.permission-error", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        User member = User.getInstance(args[0]);

        if (! plot.getMembers().contains(member))
        {
            sender.sendMessage(Language.translate("cmd.plot.kick.not-found", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (plot.getOwner() == member)
        {
            sender.sendMessage(Language.translate("cmd.plot.kick.cant-kick-owner", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        plot.removeMember(member);
        sender.sendMessage(Language.translate("cmd.plot.kick.complete", player, "name=" + args[0]).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return new ArrayList<>();
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            return new ArrayList<>();
        }

        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            plot.getMembers().forEach(m -> suggest.add(m.getPlaneName()));
        }

        return suggest;
    }
}
