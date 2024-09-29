package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.subcommand.ParentCommand;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VwmCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "vwm";
    }

    @Override
    protected void register()
    {
        this.register(new VwmBackupSubcommand());
        this.register(new VwmCreateSubcommand());
        this.register(new VwmDeleteSubcommand());
        this.register(new VwmListSubcommand());
        this.register(new VwmSaveSubcommand());
        this.register(new VwmTeleportSubcommand());
        this.register(new VwmUpdateSubcommand());
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
