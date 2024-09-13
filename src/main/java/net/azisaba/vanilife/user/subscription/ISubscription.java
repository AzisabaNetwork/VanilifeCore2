package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISubscription
{
    @NotNull String getName();

    @NotNull String getDisplayName();

    @NotNull Material getFavicon();

    @NotNull List<String> getDescription();

    @NotNull List<Component> getDetails();

    int getCost();

    default void onShortage(@NotNull User user)
    {
        user.unsubscribe(this);
    }

    default void onPayment(@NotNull User user) {}

    default boolean checkout(@NotNull User user)
    {
        if (this.getCost() <= user.getMola())
        {
            user.setMola(user.getMola() - this.getCost());
            this.onPayment(user);
            return true;
        }
        else
        {
            this.onShortage(user);
            return false;
        }
    }
}
