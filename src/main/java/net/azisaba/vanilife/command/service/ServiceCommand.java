package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.skill.SkillCommand;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ServiceCommand extends SkillCommand
{
    @Override
    public String getName()
    {
        return "service";
    }

    @Override
    protected void registerSkills()
    {
        this.registerSkill(new ServiceListSkill());
        this.registerSkill(new ServiceReloadSkill());
        this.registerSkill(new ServiceRunSkill());
        this.registerSkill(new ServiceStartSkill());
        this.registerSkill(new ServiceStopSkill());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (!UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
