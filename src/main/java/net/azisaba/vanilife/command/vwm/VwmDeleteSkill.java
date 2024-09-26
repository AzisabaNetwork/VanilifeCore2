package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.ISubcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VwmDeleteSkill implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "delete";
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
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /vwm delete <world>").color(NamedTextColor.RED));
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(args[0]);

        if (world == null)
        {
            sender.sendMessage(Component.text(String.format("ワールド %s は定義されていません", args[0])).color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text(String.format("%s を削除しています…", args[0])).color(NamedTextColor.GREEN));

        for (Plot plot : new ArrayList<>(world.getPlots()))
        {
            plot.delete();
        }

        String archive = world.archive();
        sender.sendMessage(Component.text("ワールドの削除に成功しました！").color(NamedTextColor.GREEN));
        sender.sendMessage(Component.text(String.format("%s ドは %s としてアーカイブされました", args[0], archive)).color(NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text(String.format("./plugins/Vanilife/vwm/archive/%s", archive)))));
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
