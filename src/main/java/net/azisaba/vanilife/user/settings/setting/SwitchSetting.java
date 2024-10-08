package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.SettingsUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class SwitchSetting<T extends Serializable> extends Setting<T> implements ISwitchSetting<T>
{
    protected T value;

    public SwitchSetting(@NotNull User user)
    {
        super(user);

        if (! this.user.getStorage().has(this.getKey()))
        {
            this.write(this.getDefault());
        }
    }

    @Override
    public @NotNull List<Component> getDetails()
    {
        List<Component> details = new ArrayList<>();
        this.getOptions().forEach((T value, String name) -> details.add(Component.text(name).color(this.value.equals(value) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        return details;
    }

    @Override
    public void onClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
    }

    @Override
    public void onLeftClick(@NotNull InventoryClickEvent event)
    {
        List<T> keys = new ArrayList<>(this.getOptions().keySet());

        int i = keys.indexOf(this.value);
        int j = (i + 1) % keys.size();

        this.value = keys.get(j);
        this.save();
        new SettingsUI(this.user.asPlayer(), this.user.getSettings());
    }

    public void save()
    {
        this.write(this.value);
    }
}
