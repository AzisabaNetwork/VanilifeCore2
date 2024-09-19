package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OhatuSetting extends BooleanSetting
{
    public OhatuSetting(User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "ohatu";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return new ItemStack(Material.COOKIE);
    }
}
