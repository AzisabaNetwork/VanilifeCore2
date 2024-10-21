package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Product
@SingletonSubscription
public class FireworkEmote implements Emote, Listener
{
    private final List<Firework> fireworks = new ArrayList<>();

    public FireworkEmote()
    {
        Bukkit.getPluginManager().registerEvents(this, Vanilife.getPlugin());
    }

    @Override
    public @NotNull String getName()
    {
        return "firework_emote";
    }

    @Override
    public @NotNull String getEmoteName()
    {
        return "firework";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.FIREWORK_ROCKET;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of();
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    private @NotNull Color getColor()
    {
        return Color.fromRGB(Vanilife.random.nextInt(256), Vanilife.random.nextInt(256), Vanilife.random.nextInt(256));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (! (event.getDamager() instanceof Firework firework))
        {
            return;
        }

        event.setCancelled(this.fireworks.stream().anyMatch(f -> f.getEntityId() == firework.getEntityId()));
    }

    @Override
    public void use(@NotNull Player player)
    {
        World world = player.getWorld();

        Firework firework = (Firework) world.spawnEntity(player.getLocation().add(0, 1.4, 0), EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(this.getColor())
                .with(FireworkEffect.Type.BALL)
                .trail(true)
                .flicker(true)
                .build();

        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
        firework.detonate();

        this.fireworks.add(firework);
        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> this.fireworks.remove(firework), 20L * 4);
    }
}
