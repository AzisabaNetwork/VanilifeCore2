package net.azisaba.vanilife.objective;

import org.jetbrains.annotations.NotNull;

public class LinkDiscordObjective implements Objective
{
    @Override
    public @NotNull String getName()
    {
        return "link_discord";
    }

    @Override
    public int getLevel()
    {
        return 2;
    }

    @Override
    public int getReward()
    {
        return 4;
    }
}
