package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class NeonSubscription implements ISubscription
{
    @Override
    public @NotNull String getName()
    {
        return "neon";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.OAK_SIGN;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language language)
    {
        return List.of(language.translate("subscription.neon.details.1"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 1000;
    }
}
