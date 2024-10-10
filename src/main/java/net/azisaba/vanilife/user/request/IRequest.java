package net.azisaba.vanilife.user.request;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IRequest
{
    @NotNull Player getFrom();

    @NotNull Player getTo();

    long getTicks();

    @NotNull Class<? extends IRequest> getClazz();

    void onAccept();

    void onReject();

    void onTimeOver();

    boolean match(Class<? extends IRequest> clazz, Player sender);
}
