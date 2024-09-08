package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.service.Service;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.util.ResourceUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceStartSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "start";
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
        YamlConfiguration config = ResourceUtility.getYamlResource("service.yml");

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /service start <service>").color(NamedTextColor.RED));
            return;
        }

        if (! config.contains(args[0]))
        {
            sender.sendMessage(Component.text(String.format("%s is an undefined service.", args[0])).color(NamedTextColor.RED));
            return;
        }

        if (Service.getInstance(args[0]) != null)
        {
            sender.sendMessage(Component.text(String.format("%s has already started.", args[0])).color(NamedTextColor.RED));
            return;
        }

        new Service(args[0]);
        sender.sendMessage(Component.text(String.format("%s has started.", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return new ArrayList<>(ResourceUtility.getYamlResource("service.yml").getKeys(false));
    }
}
