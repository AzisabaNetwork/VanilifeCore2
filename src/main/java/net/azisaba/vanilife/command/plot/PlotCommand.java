package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.subcommand.ParentCommand;

public class PlotCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "/plot";
    }

    @Override
    protected void register()
    {
        this.register(new PlotClaimSubcommand());
        this.register(new PlotInviteSubcommand());
        this.register(new PlotJoinSubcommand());
        this.register(new PlotKickSubcommand());
        this.register(new PlotNewSubcommand());
        this.register(new PlotQuitSubcommand());
        this.register(new PlotUnclaimSubcommand());
    }
}
