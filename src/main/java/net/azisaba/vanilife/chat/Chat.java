package net.azisaba.vanilife.chat;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Chat implements IChat
{
    protected static final List<Chat> instances = new ArrayList<>();

    public static Chat getInstance(@NotNull UUID id)
    {
        return Chat.instances.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    protected final UUID id;

    public Chat(@NotNull UUID id)
    {
        this.id = id;
        Chat.instances.add(this);
    }

    @Override
    public @NotNull UUID getId()
    {
        return this.id;
    }

    @Override
    public @NotNull List<User> getOnline()
    {
        return this.getMembers().stream().filter(User::isOnline).toList();
    }

    @Override
    public boolean isMember(User user)
    {
        return this.getMembers().contains(user);
    }

    @Override
    public boolean isMember(Player player)
    {
        if (player == null) return false;
        return this.isMember(User.getInstance(player));
    }

    @Override
    public void send(@NotNull User sender, @NotNull String message)
    {
        if (! this.isMember(sender)) return;

        final Component body = ComponentUtility.asChat(sender.asPlayer(), message);

        ComponentUtility.getMentions(message).stream()
                .filter(mention -> this.isMember(mention) && ! mention.isBlock(sender) && mention.isOnline() && mention.read("settings.chat").getAsBoolean())
                .forEach(mention -> {
                    mention.asPlayer().playSound(mention.asPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                    mention.asPlayer().sendActionBar(Language.translate("msg.mentioned", mention, "name=" + ComponentUtility.asString(sender.getName())));
                });

        this.getOnline().stream()
                .filter(online -> ! online.isBlock(sender))
                .forEach(listener -> listener.asPlayer().sendMessage(this.format(sender, listener, body)));
    }
}
