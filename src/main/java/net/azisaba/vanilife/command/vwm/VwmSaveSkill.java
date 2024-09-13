package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VwmSaveSkill implements ICommandSkill
{
    @Override
    public @NotNull String getName()
    {
        return "save";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.ADMIN;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (1 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /vwm save [world]").color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text("ワールドの保存を開始しています…").color(NamedTextColor.GREEN));

        if (args.length == 0)
        {
            VanilifeWorld.getInstances().forEach(w -> w.getWorlds().forEach(World::save));
            sender.sendMessage(Component.text("ワールドの保存に成功しました！").color(NamedTextColor.GREEN));
            return;
        }

        VanilifeWorld world = VanilifeWorld.getInstance(args[0]);

        if (world == null)
        {
            sender.sendMessage(Component.text(String.format("ワールド %s を解決できません", args[0])).color(NamedTextColor.RED));
            return;
        }

        world.getWorlds().forEach(World::save);
        sender.sendMessage(Component.text(args[0] + " の保存に成功しました！").color(NamedTextColor.GREEN));
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
