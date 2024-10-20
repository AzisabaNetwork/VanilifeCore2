package net.azisaba.vanilife.housing.pack;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MusicPack implements IHousingPack
{
    @Override
    public @NotNull String getName()
    {
        return "music";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.MUSIC_DISC_STRAD;
    }

    @Override
    public int getCost()
    {
        return 150;
    }

    @Override
    public @NotNull List<Material> getMaterials()
    {
        return List.of(Material.JUKEBOX,
                Material.NOTE_BLOCK,
                Material.BELL);
    }
}
