package net.azisaba.vanilife.user.subscription;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Product
@SingletonSubscription
public interface Emote extends ISubscription
{
    @NotNull String getEmoteName();

    void use(@NotNull Player player);
}
