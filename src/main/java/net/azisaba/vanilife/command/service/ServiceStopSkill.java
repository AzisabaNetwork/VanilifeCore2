package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.ICommandSkill;
import net.azisaba.vanilife.service.Service;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ServiceStopSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "stop";
    }

    @Override
    public boolean isOpCommand()
    {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /service stop <service>").color(NamedTextColor.RED));
            return;
        }

        if (Service.getInstance(args[0]) == null)
        {
            sender.sendMessage(Component.text("This service is already not started.").color(NamedTextColor.RED));
            return;
        }

        Service.getInstance(args[0]).stop();
        sender.sendMessage(Component.text("The service has been stopped.").color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Service.getInstances().forEach(i -> suggest.add(i.getName()));
        }

        return suggest;
    }
}
