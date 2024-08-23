package net.azisaba.vanilife.command;

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

public abstract class AbstractSkillCommand implements CommandExecutor, TabCompleter
{
    protected final HashMap<String, ICommandSkill> skills = new HashMap<>();

    public AbstractSkillCommand()
    {
        this.registerSkills();
    }

    public String getName()
    {
        return null;
    }

    protected void registerSkill(ICommandSkill skill)
    {
        this.skills.put(skill.getName(), skill);
    }

    protected void registerSkills()
    {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(Component.text(String.format("Correct syntax: /%s <skill> [args…]", this.getName())).color(NamedTextColor.RED));
            return true;
        }

        if (! this.skills.containsKey(args[0]))
        {
            sender.sendMessage(Component.text(String.format("%s is an unknown skill.", args[0])).color(NamedTextColor.RED));
            return true;
        }

        ICommandSkill skill = this.skills.get(args[0]);

        if (! sender.isOp() && skill.isOpCommand())
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
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            for (ICommandSkill skill : this.skills.values())
            {
                if ((skill.isOpCommand() && ! sender.isOp()) || ! skill.getName().startsWith(args[0]))
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
            suggest = this.skills.get(args[0]).onTabComplete(sender, command, args[0], args2);
            return suggest == null ? new ArrayList<>() : suggest;
        }
        else
        {
            return suggest;
        }
    }
}
