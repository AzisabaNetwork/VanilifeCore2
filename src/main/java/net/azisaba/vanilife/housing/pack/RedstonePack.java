package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RedstonePack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "redstone";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.REDSTONE_TORCH;
    }

    @Override
    public int getCost()
    {
        return 180;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.REDSTONE,
                Material.REDSTONE_WIRE,
                Material.REDSTONE_TORCH,
                Material.REDSTONE_WALL_TORCH,
                Material.REDSTONE_BLOCK,
                Material.REPEATER,
                Material.COMPARATOR,
                Material.TARGET,
                Material.WAXED_COPPER_BULB,
                Material.WAXED_EXPOSED_COPPER_BULB,
                Material.WAXED_WEATHERED_COPPER_BULB,
                Material.WAXED_OXIDIZED_COPPER_BULB,
                Material.LEVER,
                Material.OAK_BUTTON,
                Material.STONE_BUTTON,
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                Material.SCULK_SENSOR,
                Material.CALIBRATED_SCULK_SENSOR,
                Material.SCULK_SHRIEKER,
                Material.STRING,
                Material.TRIPWIRE,
                Material.TRIPWIRE_HOOK,
                Material.DAYLIGHT_DETECTOR,
                Material.LIGHTNING_ROD,
                Material.PISTON,
                Material.STICKY_PISTON,
                Material.SLIME_BLOCK,
                Material.HONEY_BLOCK,
                Material.DISPENSER,
                Material.DROPPER,
                Material.CRAFTER,
                Material.HOPPER,
                Material.CHISELED_BOOKSHELF,
                Material.OBSERVER,
                Material.CAULDRON,
                Material.RAIL,
                Material.POWERED_RAIL,
                Material.DETECTOR_RAIL,
                Material.ACTIVATOR_RAIL,
                Material.REDSTONE_LAMP);
    }
}
