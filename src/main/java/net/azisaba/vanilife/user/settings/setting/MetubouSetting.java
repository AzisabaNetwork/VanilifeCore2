package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MetubouSetting extends BooleanSetting
{
    public MetubouSetting(User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "metubou";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return HeadUtility.getPlayerHead("metubou");
    }
}
