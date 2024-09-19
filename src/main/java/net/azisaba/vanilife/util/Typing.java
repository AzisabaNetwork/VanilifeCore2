package net.azisaba.vanilife.util;

import net.azisaba.vanilife.Vanilife;
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

    public String getConfirmCode(int length)
    {
        length = Math.max(length, 0);

        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnoprstuvwxyz12345678";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i ++)
        {
            sb.append(characters.charAt(Vanilife.random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    public void onTyped(String string)
    {
        Typing.instances.remove(this);
    }
}
