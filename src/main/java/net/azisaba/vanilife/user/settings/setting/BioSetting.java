package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BioSetting extends Setting<String>
{
    public BioSetting(@NotNull User user)
    {
        super(user);

        if (! this.user.getStorage().has(this.getKey()))
        {
            this.write(this.getDefault());
        }
    }

    @Override
    public @NotNull String getName()
    {
        return "bio";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return new ItemStack(Material.BOOK);
    }

    @Override
    public @NotNull List<Component> getDetails()
    {
        return List.of();
    }

    @Override
    public String getDefault()
    {
        return null;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event)
    {
        new Typing((Player) event.getWhoClicked())
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Language.translate("settings.bio.pls-send-bio", this.player).color(NamedTextColor.GREEN));
                this.player.sendMessage(Language.translate("settings.bio.pls-send-bio.details", this.player).color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(":"))
                {
                    this.player.sendMessage(Language.translate("settings.bio.canceled", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (string.equals("!"))
                {
                    user.setBio(null);
                    this.player.sendMessage(Language.translate("settings.bio.deleted", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (120 < string.length())
                {
                    this.player.sendMessage(Language.translate("settings.bio.limit-over", this.player).color(NamedTextColor.RED));
                    return;
                }

                user.setBio(string);
                this.player.sendMessage(Language.translate("settings.bio.changed", this.player).color(NamedTextColor.GREEN));
            }
        };
    }
}
