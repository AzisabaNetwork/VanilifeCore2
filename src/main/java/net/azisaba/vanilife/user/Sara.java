package net.azisaba.vanilife.user;

import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum Sara
{
    DEFAULT(0, Component.text().build(), NamedTextColor.WHITE, false, false),
    $100YEN(1, Component.text("[100円皿] ").color(NamedTextColor.BLUE), NamedTextColor.BLUE, true, false),
    $500YEN(2, Component.text("[500円皿] ").color(NamedTextColor.AQUA), NamedTextColor.AQUA, true, false),
    $1000YEN(3, Component.text("[1000円皿] ").color(NamedTextColor.DARK_GREEN), NamedTextColor.DARK_GREEN, true, false),
    $2000YEN(4, Component.text("[2000円皿] ").color(NamedTextColor.LIGHT_PURPLE), NamedTextColor.LIGHT_PURPLE, true, false),
    $5000YEN(5, Component.text("[5000円皿] ").color(NamedTextColor.DARK_PURPLE), NamedTextColor.DARK_PURPLE, true, false),
    $10000YEN(6, Component.text("[10000円皿] ").color(NamedTextColor.GOLD), NamedTextColor.GOLD, true, false),
    $50000YEN(7, Component.text("[50000円皿] ").color(NamedTextColor.DARK_RED), NamedTextColor.GOLD, true, false),
    GAMING(8, ComponentUtility.asGaming("[Gaming] "), NamedTextColor.AQUA, true, false),
    NITRO(9, ComponentUtility.asGaming("[Nitro] "), NamedTextColor.LIGHT_PURPLE, true, false),
    ADMIN(11, Component.text("[ADMIN] ").color(NamedTextColor.RED), NamedTextColor.RED, false, true),
    OWNER(12, Component.text("[OWNER] ").color(NamedTextColor.RED), NamedTextColor.RED, false, true);

    public final int level;
    public final Component role;
    private final TextColor color;

    private final boolean rank;
    private final boolean permission;

    Sara(int level, Component role, TextColor color, boolean rank, boolean permission)
    {
        this.level = level;
        this.role = role;
        this.color = color;
        this.rank = rank;
        this.permission = permission;
    }

    public TextColor getColor()
    {
        return this.color;
    }

    public boolean isRank()
    {
        return this.rank;
    }

    public boolean isPermission()
    {
        return this.permission;
    }
}
