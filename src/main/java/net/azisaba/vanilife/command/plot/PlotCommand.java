package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ParentCommand;

public class PlotCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "/plot";
    }

    @Override
    protected void registerSkills()
    {
        this.registerSkill(new PlotClaimSkill());
        this.registerSkill(new PlotInviteSkill());
        this.registerSkill(new PlotJoinSkill());
        this.registerSkill(new PlotKickSkill());
        this.registerSkill(new PlotNewSkill());
        this.registerSkill(new PlotQuitSkill());
        this.registerSkill(new PlotUnclaimSkill());
    }
}
