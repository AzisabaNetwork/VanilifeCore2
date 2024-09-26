package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VwmCreateSkill implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "create";
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
            sender.sendMessage(Component.text("Correct syntax: /vwm create <name>").color(NamedTextColor.RED));
            return;
        }

        if (VanilifeWorld.getInstances().stream().anyMatch(w -> w.getName().equals(args[0])))
        {
            sender.sendMessage(Component.text(String.format("ワールド名 %s は既に使用されています", args[0])).color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text("ワールドの生成を開始中…").color(NamedTextColor.GREEN));
        VanilifeWorld.Builder.build(args[0]);
        sender.sendMessage(Component.text(String.format("ワールド %s の生成に成功しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
