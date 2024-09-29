package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.ParentCommand;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GomenneCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "gomenne";
    }

    @Override
    public void register()
    {
        this.register(new GomenneAddSubcommand());
        this.register(new GomenneOffSubcommand());
        this.register(new GomenneOnSubcommand());
        this.register(new GomenneRemoveSubcommand());
        this.register(new GomenneRequestSubcommand());
        this.register(new GomenneRequestsSubcommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0)
        {
            User user = User.getInstance(player);
            boolean b = user.read("settings.ime").getAsBoolean();
            args = new String[] {b ? "off" : "on"};
        }

        return super.onCommand(sender, command, label, args);
    }
}
