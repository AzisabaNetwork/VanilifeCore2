package net.azisaba.vanilife.user.subscription;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Product
@SingletonSubscription
public interface IStampSubscription extends ISubscription
{
    void rendering(@NotNull Location location);
}
