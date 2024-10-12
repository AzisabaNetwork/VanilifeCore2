package net.azisaba.vanilife.chat;

import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface IChat
{
    @NotNull UUID getId();

    @NotNull List<User> getMembers();

    @NotNull List<User> getOnline();

    boolean isMember(User user);

    boolean isMember(Player player);

    @NotNull Component format(@NotNull User sender, @NotNull User listener, @NotNull Component body);

    void send(@NotNull User sender, @NotNull String message);
}
