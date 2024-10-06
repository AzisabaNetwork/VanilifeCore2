package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.service.Service;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.util.ResourceUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceStartSubcommand implements ISubcommand
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
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /service start <service>").color(NamedTextColor.RED));
            return;
        }

        if (! ResourceUtility.getResource("/service/" + args[0]).exists())
        {
            sender.sendMessage(Component.text(String.format("%s は未定義のサービスです", args[0])).color(NamedTextColor.RED));
            return;
        }

        if (Service.getInstance(args[0]) != null)
        {
            sender.sendMessage(Component.text(String.format("%s は既に開始されています", args[0])).color(NamedTextColor.RED));
            return;
        }

        new Service(args[0]);
        sender.sendMessage(Component.text(String.format("%s を開始しました！", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        File directory = new File(Vanilife.getPlugin().getDataFolder(), "/service");
        File[] services = directory.listFiles();

        if (services == null)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();

        for (File service : services)
        {
            if (! service.isFile() || Service.getInstance(service.getName()) != null)
            {
                continue;
            }

            suggest.add(service.getName());
        }

        return suggest;
    }
}
