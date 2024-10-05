package net.azisaba.vanilife.arcade;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Paint implements Listener
{
    private final Map<Location, UUID> particles = new LinkedHashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation().add(player.getLocation().getDirection().multiply(5)).add(0, 1, 0);

        if (event.getAction().isLeftClick())
        {
            new HashMap<>(this.particles).keySet().stream().filter(particle -> particle.getWorld().equals(location.getWorld()) && particle.distance(player.getLocation()) < 1.3).forEach(this.particles::remove);
            new HashMap<>(this.particles).keySet().stream().filter(particle -> particle.getWorld().equals(location.getWorld()) && particle.distance(location) < 1.5).forEach(this.particles::remove);
            return;
        }

        if (event.getClickedBlock() != null)
        {
            return;
        }

        List<Brush> brushes = Arrays.stream(Brush.values()).filter(b -> b.material() == event.getMaterial()).toList();

        if (brushes.isEmpty())
        {
            return;
        }

        Brush brush = brushes.getFirst();
        User user = User.getInstance(player);

        if (user.getSara().level < brush.level().level)
        {
            player.sendMessage(Language.translate("paint.permission-error", player, "permission=" + ComponentUtility.getAsString(brush.level().role)));
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
            return;
        }

        this.spawnParticle(location, player, brush);
    }

    private void spawnParticle(@NotNull Location location, @NotNull Player player, @NotNull Brush brush)
    {
        this.particles.put(location, player.getUniqueId());

        int ink = (int) this.particles.values().stream().filter(uuid -> uuid == player.getUniqueId()).count();

        if (300 <= ink)
        {
            this.particles.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(player.getUniqueId()))
                    .findFirst()
                    .ifPresent(entry -> this.particles.remove(entry.getKey()));
        }

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> this.particles.remove(location), 20L * 60 * 3);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (! particles.containsKey(location))
                {
                    this.cancel();
                    return;
                }

                Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld().equals(location.getWorld()) && p.getLocation().distance(location) <= 20)
                        .forEach(p -> p.spawnParticle(Particle.DUST, location, 2, new Particle.DustOptions(brush.color(), 1)));
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0, 1L);
    }

    private enum Brush
    {
        BLACK(Material.BLACK_DYE, Color.BLACK, Sara.$5000YEN),
        GREEN(Material.GREEN_DYE, Color.GREEN, Sara.$1000YEN),
        PURPLE(Material.PURPLE_DYE, Color.PURPLE, Sara.$10000YEN),
        GRAY(Material.LIGHT_GRAY_DYE, Color.fromRGB(170, 170, 170), Sara.$1000YEN),
        DARK_GRAY(Material.GRAY_DYE, Color.GRAY, Sara.$2000YEN),
        BLUE(Material.BLUE_DYE, Color.BLUE, Sara.$100YEN),
        LIME(Material.LIME_DYE, Color.LIME, Sara.$2000YEN),
        AQUA(Material.LIGHT_BLUE_DYE, Color.AQUA, Sara.$10000YEN),
        RED(Material.RED_DYE, Color.RED, Sara.DEFAULT),
        PINK(Material.PINK_DYE, Color.fromRGB(255, 163, 194), Sara.$5000YEN),
        YELLOW(Material.YELLOW_DYE, Color.YELLOW, Sara.$500YEN),
        WHITE(Material.WHITE_DYE, Color.WHITE, Sara.$500YEN),
        BROWN(Material.BROWN_DYE, Color.fromRGB(166, 73, 36), Sara.$500YEN);

        private final Material material;
        private final Color color;
        private final Sara level;

        Brush(@NotNull Material material, @NotNull Color color, @NotNull Sara level)
        {
            this.material = material;
            this.color = color;
            this.level = level;
        }

        public @NotNull Material material()
        {
            return this.material;
        }

        public @NotNull Color color()
        {
            return this.color;
        }

        public @NotNull Sara level()
        {
            return this.level;
        }
    }
}
