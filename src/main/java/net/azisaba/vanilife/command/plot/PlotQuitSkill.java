package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ICommandSkill;
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
import java.util.List;

public class PlotQuitSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "quit";
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
            sender.sendMessage(Component.text("Correct syntax: //plot leave <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (plot.getOwner() == user)
        {
            sender.sendMessage(Language.translate("cmd.plot.quit.owner", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (! plot.getMembers().contains(user))
        {
            sender.sendMessage(Language.translate("cmd.plot.quit.already", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        plot.removeMember(user);
        sender.sendMessage(Language.translate("cmd.plot.quit.complete", player, "plot=" + plot.getName()).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();
        User user = User.getInstance(player);

        if (args.length == 1)
        {
            Plot.getInstances().stream().filter(i -> i.isMember(user)).forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
