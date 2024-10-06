package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.service.Service;
import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceStopSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "stop";
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
            sender.sendMessage(Component.text("Correct syntax: /service stop <service>").color(NamedTextColor.RED));
            return;
        }

        Service service = Service.getInstance(args[0]);

        if (service == null)
        {
            sender.sendMessage(Component.text("This service is already not started.").color(NamedTextColor.RED));
            return;
        }

        service.stop();
        Service.getInstances().remove(service);
        sender.sendMessage(Component.text("The service has been stopped.").color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Service.getInstances().forEach(i -> suggest.add(i.name()));
        }

        return suggest;
    }
}
