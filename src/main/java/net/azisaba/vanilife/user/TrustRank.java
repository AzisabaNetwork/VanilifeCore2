package net.azisaba.vanilife.user;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public enum TrustRank
{
    NUISANCE(-1, "Nuisance", TextColor.color(186, 186, 186), -1),
    VISITOR(0, "Visitor", TextColor.color(186, 186, 186), 0),
    NEW(1, "New", TextColor.color(22, 120, 255), 9),
    USER(2, "User", TextColor.color(42, 207, 91), 25),
    KNOWN(3, "Known", TextColor.color(255, 123, 66), 140),
    TRUSTED(4, "Trusted", TextColor.color(129, 67, 230), 240);

    private final int level;
    private final String name;
    private final TextColor color;
    private final int required;

    TrustRank(int level, @NotNull String name, @NotNull TextColor color, int required)
    {
        this.level = level;
        this.name = name;
        this.color = color;
        this.required = required;
    }

    public int getLevel()
    {
        return this.level;
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public @NotNull TextColor getColor()
    {
        return this.color;
    }

    public int getRequired()
    {
        return this.required;
    }
}
