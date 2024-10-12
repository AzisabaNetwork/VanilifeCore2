package net.azisaba.vanilife.objective;

import org.jetbrains.annotations.NotNull;

public class SendMessageObjective implements Objective
{
    @Override
    public @NotNull String getName()
    {
        return "send_message";
    }

    @Override
    public int getLevel()
    {
        return 0;
    }

    @Override
    public int getReward()
    {
        return 2;
    }
}
