package net.azisaba.vanilife.user;

public enum UserStatus
{
    DEFAULT(0),
    MUTED(1),
    JAILED(2);

    private final int level;

    UserStatus(int level)
    {
        this.level = level;
    }

    public int level()
    {
        return this.level;
    }
}
