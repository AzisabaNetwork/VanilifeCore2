package net.azisaba.vanilife.item;

import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class VanilifeItem implements IVanilifeItem
{
    private final Map<String, Serializable> container = new HashMap<>();

    @Override
    public Component getDisplayName()
    {
        return null;
    }

    @Override
    public List<Component> getLore()
    {
        return List.of();
    }

    @Override
    public boolean hasEnchantment()
    {
        return false;
    }

    @Override
    public @NotNull ItemStack asItemStack(int amount)
    {
        ItemStack itemStack = new ItemStack(this.getTexture());
        itemStack.setAmount(amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(this.getDisplayName());
        itemMeta.lore(this.getLore());
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_UNBREAKABLE);

        if (this.hasEnchantment())
        {
            itemMeta.addEnchant(Enchantment.INFINITY, 1, false);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    protected void write(@NotNull String key, Serializable value)
    {
        if (value == null)
        {
            this.container.remove(key);
            return;
        }

        this.container.put(key, value);
    }

    protected Serializable read(@NotNull String key)
    {
        return this.container.get(key);
    }

    protected Serializable read(@NotNull String key, @NotNull Class<? extends Serializable> type)
    {
        Serializable value = this.read(key);
        return value.getClass() == type ? value : null;
    }

    protected boolean readBoolean(@NotNull String key)
    {
        return (Boolean) this.read(key, Boolean.class);
    }

    protected short readShort(@NotNull String key)
    {
        return (Short) this.read(key, Short.class);
    }

    protected int readInteger(@NotNull String key)
    {
        return (Integer) this.read(key, Integer.class);
    }

    protected long readLong(@NotNull String key)
    {
        return (Long) this.read(key, Long.class);
    }

    protected float readFloat(@NotNull String key)
    {
        return (Float) this.read(key, Float.class);
    }

    protected double readDouble(@NotNull String key)
    {
        return (Double) this.read(key, Double.class);
    }

    protected String readString(@NotNull String key)
    {
        return (String) this.read(key, String.class);
    }

    @Override
    public void use(@NotNull Player player, @NotNull ItemStorage storage) {}
}
