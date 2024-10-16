package net.azisaba.vanilife.chat;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DirectChat extends Chat
{
    public static @NotNull DirectChat getInstance(@NotNull User user1, @NotNull User user2)
    {
        return DirectChat.getInstances().stream().filter(i -> i.isMember(user1) && i.isMember(user2)).findFirst().orElseGet(() -> new DirectChat(user1, user2));
    }

    public static List<DirectChat> getInstances()
    {
        return Chat.instances.stream()
                .filter(i -> i instanceof DirectChat)
                .map(chat -> (DirectChat) chat)
                .collect(Collectors.toList());
    }

    private final List<User> members = new ArrayList<>();

    public DirectChat(@NotNull User user1, @NotNull User user2)
    {
        super(UUID.randomUUID());

        this.members.add(user1);
        this.members.add(user2);
    }

    public User getPartner(@NotNull User user)
    {
        if (! this.isMember(user))
        {
            return null;
        }

        return this.members.getFirst() == user ? this.members.get(1) : this.members.getFirst();
    }

    public User getPartner(@NotNull Player player)
    {
        return this.getPartner(User.getInstance(player));
    }

    @Override
    public @NotNull List<User> getMembers()
    {
        return this.members;
    }

    @Override
    public @NotNull Component format(@NotNull User sender, @NotNull User listener, @NotNull Component body)
    {
        return Component.text()
                .append(Component.text(sender == listener ? "DM (" + this.getPartner(listener).getPlaneName() + ") > " : "DM > ").color(NamedTextColor.BLUE))
                .append(sender == listener ? Language.translate("msg.you", listener).color(NamedTextColor.GRAY) : sender.getName(listener))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .resetStyle()
                .append(body)
                .build();
    }
}
