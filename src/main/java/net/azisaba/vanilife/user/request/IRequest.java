package net.azisaba.vanilife.user.request;

import org.bukkit.entity.Player;

public interface IRequest
{
    Player getFrom();

    Player getTo();

    long getTicks();

    Class<? extends IRequest> getClazz();

    void onAllow();

    void onDisallow();

    void onTimeOver();

    boolean auth(Class<? extends IRequest> clazz, Player player);
}
