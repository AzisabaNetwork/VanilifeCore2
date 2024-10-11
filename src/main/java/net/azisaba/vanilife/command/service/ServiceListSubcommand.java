package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.subcommand.Subcommand;
import net.azisaba.vanilife.service.Service;
import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServiceListSubcommand implements Subcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "list";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /service list").color(NamedTextColor.RED));
            return;
        }

        int size = Service.getInstances().size();
        sender.sendMessage(Component.text("Service (" + size + "):").color((0 < size) ? NamedTextColor.WHITE : NamedTextColor.RED));

        for (Service service : Service.getInstances())
        {
            sender.sendMessage(Component.text("- ").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text(service.getName()).color(NamedTextColor.GRAY)));
        }
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
