package net.azisaba.vanilife.housing.world;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator
{
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome)
    {
        ChunkData chunkData = this.createChunkData(world);

        for (int x2 = 0; x2 < 16; x2 ++)
        {
            for (int z2 = 0; z2 < 16; z2 ++)
            {
                biome.setBiome(x2, z2, Biome.PLAINS);
            }
        }

        return chunkData;
    }
}
