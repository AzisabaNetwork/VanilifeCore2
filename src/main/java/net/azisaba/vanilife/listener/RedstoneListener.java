package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.gimmick.Gimmick;
import net.azisaba.vanilife.gimmick.Gimmicks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

public class RedstoneListener implements Listener
{
    @EventHandler
    public void onGimmickBlockPlace(PlayerInteractEvent event)
    {
        Block click = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack stack = event.getItem();

        if (click == null ||
                event.getBlockFace() != BlockFace.UP ||
                event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                player.getGameMode() == GameMode.ADVENTURE ||
                stack == null ||
                stack.getType() != Material.COMMAND_BLOCK)
        {
            return;
        }

        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();

        String type = container.get(new NamespacedKey(Vanilife.getPlugin(), "gimmick.type"), PersistentDataType.STRING);
        Class<? extends Gimmick> clazz = Gimmicks.registry.get(type);

        if (clazz == null)
        {
            return;
        }

        Block block = click.getRelative(BlockFace.UP);
        block.setType(Material.COMMAND_BLOCK);
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_METAL_PLACE, 1.0f, 1.0f);

        if (player.getGameMode() != GameMode.CREATIVE)
        {
            stack.setAmount(stack.getAmount() - 1);
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

    @EventHandler
    public void onGimmickBlockBreak(PlayerInteractEvent event)
    {
        Block click = event.getClickedBlock();
        Player player = event.getPlayer();
        Gimmick gimmick = Gimmick.getInstance(click);

        if (gimmick == null ||
                event.getAction() != Action.LEFT_CLICK_BLOCK ||
                player.getGameMode() == GameMode.ADVENTURE)
        {
            return;
        }

        click.setType(Material.AIR);

        if (player.getGameMode() != GameMode.CREATIVE)
        {
            click.getWorld().dropItem(click.getLocation(), gimmick.getDrop());
        }

        click.getWorld().playSound(click.getLocation(), Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);
        gimmick.kill();
    }

    @EventHandler
    public void onGimmickBlockUse(PlayerInteractEvent event)
    {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null)
        {
            return;
        }

        Gimmick gimmick = Gimmick.getInstance(block);

        if (gimmick == null || event.getAction() != Action.RIGHT_CLICK_BLOCK || player.isSneaking())
        {
            return;
        }

        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> gimmick.use(event), 5L);
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event)
    {
        if (event.getOldCurrent() != 0 || event.getNewCurrent() <= 0)
        {
            return;
        }

        Gimmick gimmick = Gimmick.getInstance(event.getBlock());

        if (gimmick == null)
        {
            return;
        }

        Location location = gimmick.getLocation();

        Player player = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getLocation().distance(location) <= 10)
                .min(Comparator.comparingDouble((Player p) -> p.getLocation().distance(location)))
                .orElse(null);

        if (player == null)
        {
            return;
        }

        gimmick.run(player);
    }
}
