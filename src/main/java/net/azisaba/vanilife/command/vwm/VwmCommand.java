package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.command.skill.SkillCommand;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VwmCommand extends SkillCommand
{
    @Override
    public String getName()
    {
        return "vwm";
    }

    @Override
    protected void registerSkills()
    {
        this.registerSkill(new VwmBackupCommand());
        this.registerSkill(new VwmCreateSkill());
        this.registerSkill(new VwmDeleteSkill());
        this.registerSkill(new VwmListSkill());
        this.registerSkill(new VwmSaveSkill());
        this.registerSkill(new VwmTeleportSkill());
        this.registerSkill(new VwmUpdateSkill());
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
