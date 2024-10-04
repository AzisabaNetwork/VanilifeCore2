package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LevelUtility
{
    public static void generate(@NotNull Location location, @NotNull String name)
    {
        StructureManager manager = Bukkit.getStructureManager();

        File nbt = ResourceUtility.getResource("/structure/" + name);

        if (! nbt.exists())
        {
            return;
        }

        try
        {
            Structure structure = manager.loadStructure(nbt);
            BlockVector size = structure.getSize();
            Location center = location.clone().add(- (double) size.getBlockX() / 2, - (double) size.getBlockY() / 2, - (double) size.getBlockZ() / 2);
            structure.place(center, true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, Vanilife.random);
        }
        catch (IOException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().warn(Component.text("Failed to generate " + name + ": " + e.getMessage()).color(NamedTextColor.RED));
        }
    }

    private static void copy(@NotNull Location pos1, @NotNull Location pos2, @NotNull Location to)
    {
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int offsetX = to.getBlockX() - Math.abs(maxX - minX) / 2;
        int offsetY = to.getBlockY() - Math.abs(maxY - minY) / 2;
        int offsetZ = to.getBlockZ() - Math.abs(maxZ - minZ) / 2;

        for (int x = minX; x <= maxX; x ++)
        {
            for (int y = minY; y <= maxY; y ++)
            {
                for (int z = minZ; z <= maxZ; z ++)
                {
                    Block sourceBlock = pos1.getWorld().getBlockAt(x, y, z);
                    Material material = sourceBlock.getType();

                    Block targetBlock = to.getWorld().getBlockAt(offsetX + (x - minX), offsetY + (y - minY), offsetZ + (z - minZ));

                    Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> targetBlock.setType(material));
                }
            }
        }
    }
}
