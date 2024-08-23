package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.AbstractSkillCommand;

public class PlotCommand extends AbstractSkillCommand
{
    @Override
    public String getName()
    {
        return "plot";
    }

    @Override
    protected void registerSkills()
    {
        this.registerSkill(new PlotCheckoutSkill());
        this.registerSkill(new PlotClaimSkill());
        this.registerSkill(new PlotDeleteSkill());
        this.registerSkill(new PlotInviteSkill());
        this.registerSkill(new PlotJoinSkill());
        this.registerSkill(new PlotKickSkill());
        this.registerSkill(new PlotLeaveSkill());
        this.registerSkill(new PlotListSkill());
        this.registerSkill(new PlotNewSkill());
        this.registerSkill(new PlotRenameSkill());
        this.registerSkill(new PlotScopeSkill());
        this.registerSkill(new PlotSetSpawnSkill());
        this.registerSkill(new PlotTeleportSkill());
        this.registerSkill(new PlotUnclaimSkill());
    }
}
