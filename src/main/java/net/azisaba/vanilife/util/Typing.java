package net.azisaba.vanilife.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Typing
{
    private static final ArrayList<Typing> instances = new ArrayList<>();

    public static Typing getInstance(Player player)
    {
        ArrayList<Typing> filteredInstances = new ArrayList<>(Typing.instances.stream().filter(i -> i.getPlayer() == player).toList());
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    protected final Player player;

    public Typing(Player player)
    {
        this.player = player;
        this.player.closeInventory();
        this.init();

        Typing currentTask = Typing.getInstance(player);

        if (currentTask != null)
        {
            Typing.instances.remove(currentTask);
        }

        Typing.instances.add(this);
    }

    public void init() {}

    public Player getPlayer()
    {
        return this.player;
    }

    public void onTyped(String string)
    {
        Typing.instances.remove(this);
    }
}
