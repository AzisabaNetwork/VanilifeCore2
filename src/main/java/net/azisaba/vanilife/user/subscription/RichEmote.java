package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class RichEmote implements Emote, Listener
{
    @Override
    public @NotNull String getName()
    {
        return "rich_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "rich";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.DIAMOND;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.rich_emote.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 180;
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event)
    {
        Item item = event.getItem();
        PersistentDataContainer container = item.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Vanilife.getPlugin(), "pickup");

        if (! container.has(key, PersistentDataType.BOOLEAN))
        {
            return;
        }

        boolean pickup = container.get(key, PersistentDataType.BOOLEAN);
        event.setCancelled(pickup || event.isCancelled());
    }

    @Override
    public void use(@NotNull Player player)
    {
        new BukkitRunnable()
        {
            private int i;

            @Override
            public void run()
            {
                if (this.i >= 10)
                {
                    this.cancel();
                    return;
                }

                Location location = player.getLocation().add(0, 3, 0);

                ItemStack diamond = new ItemStack(Material.DIAMOND);
                Item item = location.getWorld().dropItem(location, diamond);
                item.setPickupDelay(Integer.MAX_VALUE);
                item.setCanPlayerPickup(false);
                item.setCanMobPickup(false);
                PersistentDataContainer container = item.getPersistentDataContainer();
                container.set(new NamespacedKey(Vanilife.getPlugin(), "pickup"), PersistentDataType.BOOLEAN, true);

                Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), item::remove, 30L);

                this.i ++;
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0, 1L);
    }
}
