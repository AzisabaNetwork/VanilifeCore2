package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISubscription
{
    @NotNull String getName();

    @NotNull default Component getDisplayName(@NotNull Language lang)
    {
        return lang.translate("subscription." + this.getName() + ".name");
    }

    @NotNull Material getIcon();

    @NotNull List<Component> getDetails(@NotNull Language lang);

    int getCost();

    default void onShortage(@NotNull User user)
    {
        user.unsubscribe(this);
    }

    default void onPayment(@NotNull User user) {}

    default void checkout(@NotNull User user)
    {
        if (this.getCost() <= user.getMola())
        {
            user.setMola(user.getMola() - this.getCost());
            this.onPayment(user);
        }
        else
        {
            this.onShortage(user);
        }
    }
}
