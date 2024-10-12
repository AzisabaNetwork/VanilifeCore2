package net.azisaba.vanilife.objective;

import org.jetbrains.annotations.NotNull;

public class StartHousingObjective implements Objective
{
    @Override
    public @NotNull String getName()
    {
        return "start_housing";
    }

    @Override
    public int getLevel()
    {
        return 3;
    }

    @Override
    public int getReward()
    {
        return 5;
    }
}
