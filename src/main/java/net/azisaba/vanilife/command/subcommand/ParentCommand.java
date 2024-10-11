package net.azisaba.vanilife.command.subcommand;

import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParentCommand implements CommandExecutor, TabCompleter
{
    protected final Map<String, Subcommand> skills = new HashMap<>();

    public ParentCommand()
    {
        this.register();
    }

    public String getName()
    {
        return null;
    }

    protected void register(@NotNull Subcommand skill)
    {
        this.skills.put(skill.getName(), skill);
    }

    protected void register() {}

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(Component.text(String.format("Correct syntax: /%s <subcommand> [args…]", this.getName())).color(NamedTextColor.RED));
            return true;
        }

        if (! this.skills.containsKey(args[0]))
        {
            sender.sendMessage(Component.text(String.format("%s is an unknown subcommand.", args[0])).color(NamedTextColor.RED));
            return true;
        }

        Subcommand skill = this.skills.get(args[0]);

        if (! UserUtility.isAdmin(sender) && UserUtility.getSara(sender).level < skill.getRequirement().level)
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args.length - 1);
        skill.onCommand(sender, command, label, args2);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            for (Subcommand skill : this.skills.values())
            {
                if ((! sender.isOp() && UserUtility.getSara(sender).level < skill.getRequirement().level) || ! skill.getName().startsWith(args[0]))
                {
                    continue;
                }

                suggest.add(skill.getName());
            }

            return suggest;
        }
        else if (this.skills.get(args[0]) != null)
        {
            String[] args2 = new String[args.length - 1];
            System.arraycopy(args, 1, args2, 0, args.length - 1);
            return this.skills.get(args[0]).onTabComplete(sender, command, args[0], args2);
        }
        else
        {
            return suggest;
        }
    }
}
