package net.azisaba.vanilife.user.request;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IRequest
{
    @NotNull Player getFrom();

    @NotNull Player getTo();

    long getTicks();

    @NotNull Class<? extends IRequest> getClazz();

    void onAllow();

    void onDisallow();

    void onTimeOver();

    boolean auth(Class<? extends IRequest> clazz, Player player);
}
