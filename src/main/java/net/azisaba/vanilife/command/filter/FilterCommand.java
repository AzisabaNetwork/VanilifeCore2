package net.azisaba.vanilife.command.filter;

import net.azisaba.vanilife.command.subcommand.ParentCommand;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FilterCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "filter";
    }

    @Override
    protected void register()
    {
        this.register(new FilterAddSubcommand());
        this.register(new FilterListSubcommand());
        this.register(new FilterRemoveSubcommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
