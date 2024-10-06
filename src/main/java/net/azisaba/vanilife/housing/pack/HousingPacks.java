package net.azisaba.vanilife.housing.pack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HousingPacks
{
    private static final List<IHousingPack> registry = new ArrayList<>();

    public static final OakPack OAK = (OakPack) HousingPacks.register(new OakPack());

    public static final SprucePack SPRUCE = (SprucePack) HousingPacks.register(new SprucePack());

    public static final BirchPack BIRCH = (BirchPack) HousingPacks.register(new BirchPack());

    public static final JunglePack JUNGLE = (JunglePack) HousingPacks.register(new JunglePack());

    public static final AcaciaPack ACACIA = (AcaciaPack) HousingPacks.register(new AcaciaPack());

    public static final DarkOakPack DARK_OAK = (DarkOakPack) HousingPacks.register(new DarkOakPack());

    public static final MangrovePack MANGROVE = (MangrovePack) HousingPacks.register(new MangrovePack());

    public static final CherryPack CHERRY = (CherryPack) HousingPacks.register(new CherryPack());

    public static final BambooPack BAMBOO = (BambooPack) HousingPacks.register(new BambooPack());

    public static final NetherPack NETHER = (NetherPack) HousingPacks.register(new NetherPack());

    public static final StonePack1 STONE1 = (StonePack1) HousingPacks.register(new StonePack1());

    public static final StonePack2 STONE2 = (StonePack2) HousingPacks.register(new StonePack2());

    public static final TerrainPack TERRAIN = (TerrainPack) HousingPacks.register(new TerrainPack());

    public static final CrafterPack CRAFTER = (CrafterPack)HousingPacks.register(new CrafterPack());

    public static final ColorPack COLOR = (ColorPack) HousingPacks.register(new ColorPack());

    public static final MidnightPack MIDNIGHT = (MidnightPack) HousingPacks.register(new MidnightPack());

    public static List<IHousingPack> registry()
    {
        return HousingPacks.registry;
    }

    public static IHousingPack register(@NotNull IHousingPack pack)
    {
        HousingPacks.registry.add(pack);
        return pack;
    }

    public static IHousingPack valueOf(String name)
    {
        List<IHousingPack> filteredRegistry = HousingPacks.registry.stream().filter(i -> i.getName().equals(name)).toList();
        return filteredRegistry.isEmpty() ? null : filteredRegistry.getFirst();
    }
}
