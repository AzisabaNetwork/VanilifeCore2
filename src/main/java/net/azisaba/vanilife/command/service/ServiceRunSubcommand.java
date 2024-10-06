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

public class ServiceRunSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "run";
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
            sender.sendMessage(Component.text("Correct syntax: /service run <service>").color(NamedTextColor.RED));
            return;
        }

        Service service = Service.getInstance(args[0]);

        if (service == null)
        {
            sender.sendMessage(Component.text(String.format("%s は未定義のサービスです", args[0])).color(NamedTextColor.RED));
            return;
        }

        service.run();
        sender.sendMessage(Component.text(String.format("%s を実行しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Service.getInstances().forEach(i -> suggest.add(i.getName()));
        }

        return suggest;
    }
}
