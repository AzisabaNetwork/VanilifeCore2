package net.azisaba.vanilife.vwm;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationTranslator
{
    public static Location translate(Location from, World to)
    {
        if (to.getEnvironment() == World.Environment.THE_END)
        {
            return to.getSpawnLocation();
        }

        int ratio = (from.getWorld().getEnvironment() == World.Environment.NORMAL) ? 8 : 1 / 8;

        double x = from.getX() * ratio;
        double y = from.getY() * ratio;
        double z = from.getZ() * ratio;

        return new Location(to, x, y, z);
    }
}
