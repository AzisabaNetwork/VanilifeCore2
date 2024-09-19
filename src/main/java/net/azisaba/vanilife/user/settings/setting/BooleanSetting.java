package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.SettingsUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BooleanSetting extends Setting<Boolean>
{
    protected boolean value;

    public BooleanSetting(User user)
    {
        super(user);

        if (! this.user.getStorage().has(this.getKey()))
        {
            this.write(this.getDefault());
        }

        this.value = this.read().getAsBoolean();
    }

    @Override
    public @NotNull List<Component> getDetails()
    {
        return new ArrayList<>(List.of(Language.translate("settings.boolean.state", this.user).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append((this.value ? Language.translate("ui.true", this.user) : Language.translate("ui.false", this.user)).color(this.value ? NamedTextColor.GREEN : NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false)));
    }

    @Override
    public Boolean getDefault()
    {
        return false;
    }

    public boolean isValid()
    {
        return this.value;
    }

    @Override
    public void onClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
        this.value = ! this.value;
        this.write(this.value);
        new SettingsUI(this.user.getAsPlayer(), this.user.getSettings());
    }
}
