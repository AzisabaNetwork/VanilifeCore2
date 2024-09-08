package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VwmTeleportSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "teleport";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.ADMIN;
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
            sender.sendMessage(Component.text("Correct syntax: /vwm teleport <world>").color(NamedTextColor.RED));
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(args[0]);

        if (world == null)
        {
            sender.sendMessage(Component.text(String.format("ワールド %s は定義されていません", args[0])).color(NamedTextColor.RED));
            return;
        }

        VanilifeWorld currentWorld = VanilifeWorld.getInstance(player.getWorld());

        if (currentWorld != null)
        {
            currentWorld.setLocation(player, player.getLocation());
        }

        player.teleport(world.getLocation(player));
        sender.sendMessage(Component.text(String.format("%s にテレポートしました！", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            VanilifeWorld.getInstances().forEach(w -> suggest.add(w.getName()));
        }

        return suggest;
    }
}
