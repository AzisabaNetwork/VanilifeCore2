package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.housing.pack.HousingPacks;
import net.azisaba.vanilife.housing.pack.IHousingPack;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.TimeSkipEvent;

public class HousingListener implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        if (! user.inHousing())
        {
            return;
        }

        Housing housing = Housing.getInstance(event.getBlock().getLocation());

        if (housing == null)
        {
            event.setCancelled(event.isCancelled() && ! (player.getGameMode() == GameMode.CREATIVE && UserUtility.isModerator(player)));
            return;
        }

        final boolean can = housing.getPacks().stream().anyMatch(pack -> pack.include(event.getBlock())) || (player.getGameMode() == GameMode.CREATIVE && UserUtility.isModerator(player));

        if (! can)
        {
            if (HousingPacks.registry().stream().anyMatch(pack -> pack.include(event.getBlock())))
            {
                IHousingPack pack = HousingPacks.registry().stream().filter(p -> p.include(event.getBlock())).toList().getFirst();
                player.sendMessage(Language.translate("housing.cant-place.how", player, "pack=" + ComponentUtility.getAsString(Language.translate("housing.pack." + pack.getName() + ".name", player))));
            }
            else
            {
                player.sendMessage(Language.translate("housing.cant-place", player).color(NamedTextColor.RED));
            }
        }

        event.setCancelled(event.isCancelled() || ! can);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        User user = User.getInstance(event.getPlayer());

        if (! user.inHousing())
        {
            return;
        }

        if (Housing.getInstance(event.getBlock().getLocation()) != null)
        {
            return;
        }

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        if (! user.inHousing())
        {
            return;
        }

        Housing from = Housing.getInstance(event.getFrom());
        Housing to = Housing.getInstance(event.getTo());

        if (! (from != null && to == null) || (player.getGameMode() == GameMode.CREATIVE && UserUtility.isModerator(player)))
        {
            return;
        }

        player.teleport(from.getSpawn());
        player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        Player player = event.getPlayer();

        if (! Housing.getWorld().equals(player.getWorld()))
        {
            return;
        }

        event.setUseBed(Event.Result.ALLOW);
    }

    @EventHandler
    public void onPlayerSetSpawn(PlayerSetSpawnEvent event)
    {
        Player player = event.getPlayer();

        if (! Housing.getWorld().equals(player.getWorld()))
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event)
    {
        World world = event.getWorld();

        if (! Housing.getWorld().equals(world))
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        event.setCancelled(event.isCancelled() || (Housing.getWorld().equals(event.getEntity().getWorld())) && event.getFoodLevel() < event.getEntity().getFoodLevel());
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event)
    {
        if (! event.getWorld().equals(Housing.getWorld()))
        {
            return;
        }

        if (event.getSkipReason() != TimeSkipEvent.SkipReason.NIGHT_SKIP)
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event)
    {
        event.setCancelled(event.isCancelled() || (event.getLocation().getWorld().equals(Housing.getWorld())) && event.getEntity() instanceof Monster);
    }
}
