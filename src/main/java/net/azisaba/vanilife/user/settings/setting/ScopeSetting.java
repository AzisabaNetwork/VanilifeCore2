package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.SettingsUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ScopeSetting extends SwitchSetting<String>
{
    protected Scope scope;

    public ScopeSetting(@NotNull User user)
    {
        super(user);

        if (! this.user.getStorage().has(this.getKey()))
        {
            this.write(this.getDefault());
        }

        this.value = this.read().getAsString();
        this.scope = Scope.valueOf(this.value);
    }

    @Override
    public @NotNull ItemStack getStateIcon()
    {
        return new ItemStack(switch (this.scope)
        {
            case PUBLIC -> Material.LIME_DYE;
            case FRIEND -> Material.YELLOW_DYE;
            case PRIVATE -> Material.GRAY_DYE;
        });
    }

    @Override
    public String getDefault()
    {
        return Scope.PUBLIC.toString();
    }

    @Override
    public @NotNull LinkedHashMap<String, String> getOptions()
    {
        return new LinkedHashMap<>(Map.of(Scope.PUBLIC.toString(), ((TextComponent) Language.translate(Scope.PUBLIC.getName(), this.user)).content(),
                Scope.FRIEND.toString(), ((TextComponent) Language.translate(Scope.FRIEND.getName(), this.user)).content(),
                Scope.PRIVATE.toString(), ((TextComponent) Language.translate(Scope.PRIVATE.getName(), this.user)).content()));
    }

    @Override
    public void onLeftClick(@NotNull InventoryClickEvent event)
    {
        this.scope = switch (this.scope)
        {
            case PUBLIC -> Scope.FRIEND;
            case FRIEND -> Scope.PRIVATE;
            default -> Scope.PUBLIC;
        };

        this.value = this.scope.toString();

        this.save();
        new SettingsUI(this.user.getAsPlayer(), this.user.getSettings());
    }

    public boolean isWithinScope(User user)
    {
        return (this.scope == Scope.PUBLIC) || (this.scope == Scope.FRIEND && this.user.isFriend(user)) || this.user == user;
    }

    @Override
    public void save()
    {
        this.write(this.scope.toString());
    }

    public enum Scope
    {
        PUBLIC("settings.scope.public"),
        FRIEND("settings.scope.friend"),
        PRIVATE("settings.scope.private");

        private final String name;

        Scope(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
