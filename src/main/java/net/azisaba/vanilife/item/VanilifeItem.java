package net.azisaba.vanilife.item;

import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class VanilifeItem implements IVanilifeItem
{
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

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(Vanilife.getPlugin(), "name"), PersistentDataType.STRING, this.getName());

        if (this.hasEnchantment())
        {
            itemMeta.addEnchant(Enchantment.INFINITY, 1, false);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void use(@NotNull Player player, @NotNull ItemStorage storage) {}
}
