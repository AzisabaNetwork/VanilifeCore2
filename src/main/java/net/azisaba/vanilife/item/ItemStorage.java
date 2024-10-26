package net.azisaba.vanilife.item;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ItemStorage
{
    private final PersistentDataContainer container;
    
    public ItemStorage(@NotNull ItemStack stack)
    {
        this.container = stack.getItemMeta().getPersistentDataContainer();
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
    
    public <P, C> void write(@NotNull String key, C value, @NotNull PersistentDataType<P, C> type)
    {
        NamespacedKey namespacedKey = new NamespacedKey(Vanilife.getPlugin(), key);

        if (value == null)
        {
            this.container.remove(namespacedKey);
            return;
        }

        this.container.set(namespacedKey, type, value);
    }

    public <P, C> C read(@NotNull String key, @NotNull PersistentDataType<P, C> type)
    {
        return this.container.get(new NamespacedKey(Vanilife.getPlugin(), key), type);
    }

    public byte readByte(@NotNull String key)
    {
        return this.read(key, PersistentDataType.BYTE);
    }

    public short readShort(@NotNull String key)
    {
        return this.read(key, PersistentDataType.SHORT);
    }

    public int readInteger(@NotNull String key)
    {
        return this.read(key, PersistentDataType.INTEGER);
    }

    public long readLong(@NotNull String key)
    {
        return this.read(key, PersistentDataType.LONG);
    }

    public float readFloat(@NotNull String key)
    {
        return this.read(key, PersistentDataType.FLOAT);
    }

    public double readDouble(@NotNull String key)
    {
        return this.read(key, PersistentDataType.DOUBLE);
    }

    public boolean readBoolean(@NotNull String key)
    {
        return this.read(key, PersistentDataType.BOOLEAN);
    }

    public String readString(@NotNull String key)
    {
        return this.read(key, PersistentDataType.STRING);
    }
}
