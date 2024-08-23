package net.azisaba.vanilife.command.service;

import net.azisaba.vanilife.command.AbstractSkillCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ServiceCommand extends AbstractSkillCommand
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
        if (!sender.isOp())
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
