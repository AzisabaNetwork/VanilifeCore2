package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Request implements IRequest
{
    private static final List<Request> instances = new ArrayList<>();

    public static Request getInstance(@NotNull UUID id)
    {
        return Request.instances.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    protected final UUID id = UUID.randomUUID();

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

        this.fromUser = User.getInstance(this.from);
        this.toUser = User.getInstance(this.to);

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

        Request.instances.add(this);
    }

    public @NotNull UUID getId()
    {
        return this.id;
    }

    @Override
    public @NotNull Player getFrom()
    {
        return this.from;
    }

    public @NotNull User getFromUser()
    {
        return this.fromUser;
    }

    @Override
    public @NotNull Player getTo()
    {
        return this.to;
    }

    public @NotNull User getToUser()
    {
        return this.toUser;
    }

    @Override
    public void onAccept()
    {
        this.toUser.getRequests().remove(this);
        this.runnable.cancel();
        Request.instances.remove(this);
    }

    @Override
    public void onReject()
    {
        this.toUser.getRequests().remove(this);
        this.runnable.cancel();
        Request.instances.remove(this);
    }

    @Override
    public void onTimeOver()
    {
        this.toUser.getRequests().remove(this);
        Request.instances.remove(this);
    }

    @Override
    public boolean match(Class<? extends IRequest> clazz, Player sender)
    {
        return this.getClazz().isAssignableFrom(clazz) && this.from == sender;
    }
}
