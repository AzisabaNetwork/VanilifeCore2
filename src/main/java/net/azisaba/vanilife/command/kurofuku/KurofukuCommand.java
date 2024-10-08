package net.azisaba.vanilife.command.kurofuku;

import net.azisaba.vanilife.command.subcommand.ParentCommand;

public class KurofukuCommand extends ParentCommand
{
    @Override
    protected void register()
    {
        this.register(new KurofukuAddSubcommand());
        this.register(new KurofukuListSubcommand());
        this.register(new KurofukuRemoveCommand());
    }
}
