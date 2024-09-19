package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PreviewSetting extends BooleanSetting
{
    public PreviewSetting(User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "preview";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return new ItemStack(Material.COMPARATOR);
    }
}
