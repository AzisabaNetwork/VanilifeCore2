package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VwmListSubcommand implements ISubcommand
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
        return Sara.ADMIN;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /vwm list").color(NamedTextColor.RED));
            return;
        }

        int size = VanilifeWorld.getInstances().size();
        sender.sendMessage(Component.text("Worlds (" + size + "):").color((0 < size) ? NamedTextColor.WHITE : NamedTextColor.RED));

        for (VanilifeWorld world : VanilifeWorld.getInstances())
        {
            sender.sendMessage(Component.text("- ").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text(world.getName()).color(NamedTextColor.GRAY).clickEvent(ClickEvent.runCommand(String.format("/vwm teleport %s", world.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして %s にテレポート", world.getName()))))));
        }
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}