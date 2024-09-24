package net.azisaba.vanilife.user.subscription;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Product
@SingletonSubscription
public interface IEmoteSubscription extends ISubscription
{
    @NotNull String getEmoteName();

    void use(@NotNull Location location);
}
