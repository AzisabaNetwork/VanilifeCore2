package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MailSetting extends ScopeSetting
{
    public MailSetting(@NotNull User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "mail";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return new ItemStack(Material.PAPER);
    }
}
