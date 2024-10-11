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

public class ServiceReloadSubcommand implements Subcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "reload";
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
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /service reload").color(NamedTextColor.RED));
            return;
        }

        Service.kill();
        Service.mount();

        sender.sendMessage(Component.text("サービスのリロードに成功しました！").color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
