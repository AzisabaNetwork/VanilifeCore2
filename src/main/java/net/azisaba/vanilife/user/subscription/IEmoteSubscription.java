package net.azisaba.vanilife.user.subscription;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Product
@SingletonSubscription
public interface IEmoteSubscription extends ISubscription
{
    @NotNull String getEmoteName();

    void use(@NotNull Player player);
}
