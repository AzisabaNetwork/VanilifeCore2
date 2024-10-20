package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Gimmick implements IGimmick
{
    private static final List<Gimmick> instances = new ArrayList<>();

    public static Gimmick getInstance(Block block)
    {
        if (block == null)
        {
            return null;
        }

        if (block.getType() != Material.COMMAND_BLOCK)
        {
            return null;
        }

        Gimmick instance = Gimmick.instances.stream().filter(i -> i.getLocation().equals(block.getLocation())).findFirst().orElse(null);

        return instance != null ? instance : new InitGimmick(block);
    }

    private final Block block;
    private final String address;

    public Gimmick(@NotNull Block block)
    {
        this.block = block;
        this.address = String.format("gimmick.%s_%s_%s_%s.", this.getLocation().getWorld().getName(), this.getLocation().getBlockX(), this.getLocation().getBlockY(), this.getLocation().getZ());

        Gimmick.instances.add(this);
    }

    public @NotNull Block getBlock()
    {
        return this.block;
    }

    public @NotNull Location getLocation()
    {
        return this.block.getLocation();
    }

    protected void write(@NotNull String key, @NotNull String value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.STRING, value);
    }

    protected void write(@NotNull String key, boolean value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.BOOLEAN, value);
    }

    protected void write(@NotNull String key, short value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.SHORT, value);
    }

    protected void write(@NotNull String key, int value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.INTEGER, value);
    }

    protected void write(@NotNull String key, long value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.LONG, value);
    }

    protected void write(@NotNull String key, float value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.FLOAT, value);
    }

    protected void write(@NotNull String key, double value)
    {
        this.getLocation().getWorld().getPersistentDataContainer()
                .set(new NamespacedKey(Vanilife.getPlugin(), this.address + key), PersistentDataType.DOUBLE, value);
    }

    protected  <P, C> C read(@NotNull String key, PersistentDataType<P, C> type)
    {
        return this.getLocation().getWorld().getPersistentDataContainer().get(new NamespacedKey(Vanilife.getPlugin(), this.address + key), type);
    }

    protected String readString(@NotNull String key)
    {
        return this.read(key, PersistentDataType.STRING);
    }

    protected Boolean readBoolean(@NotNull String key)
    {
        return this.read(key, PersistentDataType.BOOLEAN);
    }

    protected Short readShort(@NotNull String key)
    {
        return this.read(key, PersistentDataType.SHORT);
    }

    protected Integer readInt(@NotNull String key)
    {
        return this.read(key, PersistentDataType.INTEGER);
    }

    protected Long readLong(@NotNull String key)
    {
        return this.read(key, PersistentDataType.LONG);
    }

    protected Float readFloat(@NotNull String key)
    {
        return this.read(key, PersistentDataType.FLOAT);
    }

    protected Double readDouble(@NotNull String key)
    {
        return this.read(key, PersistentDataType.DOUBLE);
    }

    public void kill()
    {
        Gimmick.instances.remove(this);

        PersistentDataContainer container = this.getLocation().getWorld().getPersistentDataContainer();

        container.getKeys().stream()
                .filter(key -> key.getNamespace().equals(Vanilife.getPlugin().getName()) && key.getKey().startsWith(this.address))
                .forEach(container::remove);
    }
}
