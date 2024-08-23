package net.azisaba.vanilife.mconsole;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MconsoleSender implements ConsoleCommandSender
{
    private static final MconsoleSender instance = new MconsoleSender();

    public static MconsoleSender getInstance()
    {
        return MconsoleSender.instance;
    }

    private final ArrayList<String> outputs = new ArrayList<>();

    public ArrayList<String> getOutputs()
    {
        return this.outputs;
    }

    @Override
    public void sendMessage(@NotNull String message)
    {
        Bukkit.getConsoleSender().sendMessage(message);
        this.outputs.add(ChatColor.stripColor(message));
    }

    @Override
    public void sendMessage(@NotNull String... messages)
    {
        Bukkit.getConsoleSender().sendMessage(messages);
        List.of(messages).forEach(m -> this.sendMessage(ChatColor.stripColor(m)));
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message)
    {
        Bukkit.getConsoleSender().sendMessage(sender, message);
        this.outputs.add(ChatColor.stripColor(message));
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages)
    {
        Bukkit.getConsoleSender().sendMessage(sender, messages);
        List.of(messages).forEach(m -> this.sendMessage(ChatColor.stripColor(m)));
    }

    @Override
    public @NotNull Server getServer()
    {
        return Bukkit.getServer();
    }

    @Override
    public @NotNull String getName()
    {
        return Bukkit.getConsoleSender().getName();
    }

    @NotNull
    @Override
    public Spigot spigot()
    {
        return Bukkit.getConsoleSender().spigot();
    }

    @Override
    public @NotNull Component name()
    {
        return Bukkit.getConsoleSender().name();
    }

    @Override
    public boolean isConversing()
    {
        return Bukkit.getConsoleSender().isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String input)
    {
        Bukkit.getConsoleSender().acceptConversationInput(input);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation)
    {
        return Bukkit.getConsoleSender().beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation)
    {
        Bukkit.getConsoleSender().abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details)
    {
        Bukkit.getConsoleSender().abandonConversation(conversation, details);
    }

    @Override
    public void sendRawMessage(@NotNull String message)
    {
        Bukkit.getConsoleSender().sendRawMessage(message);
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message)
    {
        Bukkit.getConsoleSender().sendRawMessage(sender, message);
    }

    @Override
    public boolean isPermissionSet(@NotNull String name)
    {
        return Bukkit.getConsoleSender().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm)
    {
        return Bukkit.getConsoleSender().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name)
    {
        return Bukkit.getConsoleSender().hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm)
    {
        return Bukkit.getConsoleSender().hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks)
    {
        return Bukkit.getConsoleSender().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment)
    {
        Bukkit.getConsoleSender().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions()
    {
        Bukkit.getConsoleSender().recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return Bukkit.getConsoleSender().getEffectivePermissions();
    }

    @Override
    public boolean isOp()
    {
        return Bukkit.getConsoleSender().isOp();
    }

    @Override
    public void setOp(boolean value)
    {
        Bukkit.getConsoleSender().setOp(value);
    }
}
