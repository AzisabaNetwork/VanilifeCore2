package net.azisaba.vanilife.gimmick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class InitGimmick extends Gimmick
{
    public InitGimmick(@NotNull Block block)
    {
        super(block);

        Class<? extends Gimmick> clazz = Gimmicks.registry.get(this.readString("type"));

        this.kill();

        if (clazz == null)
        {
            return;
        }

        try
        {
            clazz.getConstructor(Block.class).newInstance(block);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull String getType()
    {
        return "<init>";
    }

    @Override
    public ItemStack getDrop()
    {
        return new ItemStack(Material.AIR);
    }

    @Override
    public void use(@NotNull PlayerInteractEvent event) {}

    @Override
    public void run(@NotNull Player player)
    {
        player.sendMessage(Component.text("ギミックブロックを読み込み中…").color(NamedTextColor.RED));
    }
}
