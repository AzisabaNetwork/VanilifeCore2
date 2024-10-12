package net.azisaba.vanilife.objective;

import org.jetbrains.annotations.NotNull;

public class MakeFriendObjective implements Objective
{
    @Override
    public @NotNull String getName()
    {
        return "make_friend";
    }

    @Override
    public int getLevel()
    {
        return 1;
    }

    @Override
    public int getReward()
    {
        return 3;
    }
}
