package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class Request implements IRequest
{
    protected final Player from;
    protected final User fromUser;

    protected final Player to;
    protected final User toUser;

    protected BukkitRunnable runnable;

    public Request(@NotNull Player from, @NotNull Player to)
    {
        if (from == to)
        {
            from.sendMessage(Component.text("自分自身に招待を送信することはできません").color(NamedTextColor.RED));

            this.from = null;
            this.fromUser = null;

            this.to = null;
            this.toUser = null;
            return;
        }

        this.from = from;
        this.to = to;

        this.fromUser = User.getInstance(from);
        this.toUser = User.getInstance(to);

        this.toUser.getRequests().add(this);

        this.runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                onTimeOver();
            }
        };

        this.runnable.runTaskLater(Vanilife.getPlugin(), this.getTicks());
    }

    @Override
    @NotNull
    public Player getFrom()
    {
        return this.from;
    }

    @NotNull
    public User getFromUser()
    {
        return this.fromUser;
    }

    @Override
    @NotNull
    public Player getTo()
    {
        return this.to;
    }

    public User getToUser()
    {
        return this.toUser;
    }

    @Override
    public void onAllow()
    {
        this.toUser.getRequests().remove(this);
        this.runnable.cancel();
    }

    @Override
    public void onDisallow()
    {
        this.toUser.getRequests().remove(this);
        this.runnable.cancel();
    }

    @Override
    public void onTimeOver()
    {
        this.toUser.getRequests().remove(this);
    }

    @Override
    public boolean auth(Class<? extends IRequest> clazz, Player player)
    {
        return this.getClazz().isAssignableFrom(clazz) && this.from == player;
    }
}
