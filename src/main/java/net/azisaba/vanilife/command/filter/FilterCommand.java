package net.azisaba.vanilife.command.filter;

import net.azisaba.vanilife.command.skill.ParentCommand;
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
    protected void registerSkills()
    {
        this.registerSkill(new FilterAddSkill());
        this.registerSkill(new FilterListSkill());
        this.registerSkill(new FilterRemoveSkill());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isModerator(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
