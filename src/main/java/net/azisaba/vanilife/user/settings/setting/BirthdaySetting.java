package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Date;

public class BirthdaySetting extends ScopeSetting
{
    public BirthdaySetting(@NotNull User user)
    {
        super(user);
    }

    @Override
    public @NotNull String getName()
    {
        return "birthday";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        return new ItemStack(Material.CAKE);
    }

    @Override
    public void onRightClick(InventoryClickEvent event)
    {
        new Typing((Player) event.getWhoClicked())
        {
            @Override
            public void init()
            {
                this.player.sendMessage(Language.translate("settings.birthday.pls-send-birthday", this.player).color(NamedTextColor.GREEN));
                this.player.sendMessage(Language.translate("settings.birthday.pls-send-birthday.details", this.player).color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(":"))
                {
                    this.player.sendMessage(Language.translate("settings.birthday.cancelled", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (string.equals("!"))
                {
                    user.setBirthday(null);
                    this.player.sendMessage(Language.translate("settings.birthday.deleted", this.player).color(NamedTextColor.RED));
                    return;
                }

                if (Vanilife.pattern1.matcher(string).matches())
                {
                    try
                    {
                        Date birthday = Vanilife.sdf3.parse(string);

                        if (birthday.after(new Date()))
                        {
                            this.player.sendMessage(Language.translate("settings.birthday.invalid", this.player, "birthday=" + string).color(NamedTextColor.RED));
                            return;
                        }

                        User.getInstance(this.player).setBirthday(birthday);
                    }
                    catch (ParseException e)
                    {
                        throw new RuntimeException(e);
                    }

                    this.player.sendMessage(Language.translate("settings.birthday.changed", this.player).color(NamedTextColor.GREEN));
                }
                else
                {
                    this.player.sendMessage(Language.translate("settings.birthday.invalid-format", this.player).color(NamedTextColor.RED));
                }

            }
        };
    }
}
