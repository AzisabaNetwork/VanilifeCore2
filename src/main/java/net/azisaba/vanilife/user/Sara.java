package net.azisaba.vanilife.user;

import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum Sara
{
    DEFAULT(0, Component.text().build(), NamedTextColor.WHITE),
    $100YEN(1, Component.text("[100円皿] ").color(NamedTextColor.BLUE), NamedTextColor.BLUE, true),
    $500YEN(2, Component.text("[500円皿] ").color(NamedTextColor.AQUA), NamedTextColor.AQUA, true),
    $1000YEN(3, Component.text("[1000円皿] ").color(NamedTextColor.DARK_GREEN), NamedTextColor.DARK_GREEN, true),
    $2000YEN(4, Component.text("[2000円皿] ").color(NamedTextColor.LIGHT_PURPLE), NamedTextColor.LIGHT_PURPLE, true),
    $5000YEN(5, Component.text("[5000円皿] ").color(NamedTextColor.DARK_PURPLE), NamedTextColor.DARK_PURPLE, true),
    $10000YEN(6, Component.text("[10000円皿] ").color(NamedTextColor.GOLD), NamedTextColor.GOLD, true),
    $50000YEN(7, Component.text("[50000円皿] ").color(NamedTextColor.DARK_RED), NamedTextColor.GOLD, true),
    GAMING(8, ComponentUtility.getAsGaming("[Gaming] "), NamedTextColor.AQUA, true),
    NITRO(9, ComponentUtility.getAsGaming("[Nitro] "), NamedTextColor.LIGHT_PURPLE, true),
    MOD(10, Component.text("[MOD] ").color(NamedTextColor.YELLOW), NamedTextColor.YELLOW),
    ADMIN(11, Component.text("[ADMIN] ").color(NamedTextColor.RED), NamedTextColor.RED),
    OWNER(12, Component.text("[OWNER] ").color(NamedTextColor.RED), NamedTextColor.GRAY);

    public final int level;
    public final Component role;
    private final TextColor color;
    private final boolean rank;

    Sara(int level, Component role, TextColor color)
    {
        this(level, role, color, false);
    }

    Sara(int level, Component role, TextColor color, boolean rank)
    {
        this.level = level;
        this.role = role;
        this.color = color;
        this.rank = rank;
    }

    public TextColor getColor()
    {
        return this.color;
    }

    public boolean isRank()
    {
        return this.rank;
    }
}
