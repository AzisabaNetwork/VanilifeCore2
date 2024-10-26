package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
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

    public void write(@NotNull String key, Serializable value)
    {
        switch (value)
        {
            case Byte b -> this.write(key, b, PersistentDataType.BYTE);
            case Short s -> this.write(key, s, PersistentDataType.SHORT);
            case Integer i -> this.write(key, i, PersistentDataType.INTEGER);
            case Long l -> this.write(key, l, PersistentDataType.LONG);
            case Float f -> this.write(key, f, PersistentDataType.FLOAT);
            case Double d -> this.write(key, d, PersistentDataType.DOUBLE);
            case Boolean b -> this.write(key, b, PersistentDataType.BOOLEAN);
            case String s -> this.write(key, s, PersistentDataType.STRING);
            case null -> this.write(key, null, PersistentDataType.STRING);
            default -> throw new RuntimeException(value.getClass().getName() + " is an unsupported data type.");
        }
    }

    protected <P, C> void write(@NotNull String key, C value, @NotNull PersistentDataType<P, C> type)
    {
        PersistentDataContainer container = this.getLocation().getWorld().getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(Vanilife.getPlugin(), this.address + key);

        if (value == null)
        {
            container.remove(namespacedKey);
            return;
        }

        container.set(namespacedKey, type, value);
    }

    protected  <P, C> C read(@NotNull String key, PersistentDataType<P, C> type)
    {
        return this.getLocation().getWorld().getPersistentDataContainer().get(new NamespacedKey(Vanilife.getPlugin(), this.address + key), type);
    }

    protected byte readByte(@NotNull String key)
    {
        return this.read(key, PersistentDataType.BYTE);
    }

    protected short readShort(@NotNull String key)
    {
        return this.read(key, PersistentDataType.SHORT);
    }

    protected int readInteger(@NotNull String key)
    {
        return this.read(key, PersistentDataType.INTEGER);
    }

    protected long readLong(@NotNull String key)
    {
        return this.read(key, PersistentDataType.LONG);
    }

    protected float readFloat(@NotNull String key)
    {
        return this.read(key, PersistentDataType.FLOAT);
    }

    protected double readDouble(@NotNull String key)
    {
        return this.read(key, PersistentDataType.DOUBLE);
    }

    protected boolean readBoolean(@NotNull String key)
    {
        return this.read(key, PersistentDataType.BOOLEAN);
    }

    protected String readString(@NotNull String key)
    {
        return this.read(key, PersistentDataType.STRING);
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
