package net.azisaba.vanilife.command.filter;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterListSubcommand implements ISubcommand
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
            sender.sendMessage(Component.text("Correct syntax: /filter list").color(NamedTextColor.RED));
            return;
        }

        int size = Vanilife.filter.getFilters().size();
        sender.sendMessage(Component.text("NG ワード (" + size + "):").color((0 < size) ? NamedTextColor.WHITE : NamedTextColor.RED));

        for (String ng : Vanilife.filter.getFilters())
        {
            sender.sendMessage(Component.text("- ").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text(ng).color(NamedTextColor.GREEN)));
        }
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}