package net.azisaba.vanilife.objective;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Objective
{
    @NotNull String getName();

    default @NotNull Component getName(@NotNull Language lang)
    {
        return lang.translate("objective." + this.getName());
    }

    int getLevel();

    int getReward();
}
