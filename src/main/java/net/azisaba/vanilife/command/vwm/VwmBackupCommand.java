package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VwmBackupCommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "backup";
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
        if (1 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /vwm backup [world]").color(NamedTextColor.RED));
            return;
        }

        if (args.length == 1 && VanilifeWorld.getInstance(args[0]) == null)
        {
            sender.sendMessage(Component.text(String.format("%s は未定義のワールドです", args[0])).color(NamedTextColor.RED));
            return;
        }

        List<VanilifeWorld> worlds = new ArrayList<>();

        if (args.length == 1)
        {
            worlds.add(VanilifeWorld.getInstance(args[0]));
        }
        else
        {
            worlds.addAll(VanilifeWorld.getInstances());
        }

        worlds.forEach(world -> {
            sender.sendMessage(Component.text(String.format("%s のバックアップを開始しています…", world.getName())).color(NamedTextColor.GREEN));
            String backup = world.backup();
            sender.sendMessage(Component.text(String.format("%s は %s としてバックアップされました", world.getName(), backup)).color(NamedTextColor.GREEN));
        });

        sender.sendMessage(Component.text(String.format("%s 件のバックアップに成功しました！", worlds.size())).color(NamedTextColor.GREEN));
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
