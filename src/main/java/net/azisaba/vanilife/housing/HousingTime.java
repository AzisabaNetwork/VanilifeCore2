package net.azisaba.vanilife.housing;

public enum HousingTime
{
    DAY(1000),
    NOON(6000),
    NIGHT(13000),
    MIDNIGHT(18000);

    private final int time;

    HousingTime(int time)
    {
        this.time = time;
    }

    public int getTime()
    {
        return this.time;
    }
}
