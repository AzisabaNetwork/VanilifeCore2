package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class LanguageSetting extends SwitchSetting<String>
{
    private Language language;

    public LanguageSetting(@NotNull User user)
    {
        super(user);
        this.value = this.read().getAsString();
        this.language = Language.getInstance(this.value);
    }

    @Override
    public @NotNull String getName()
    {
        return "language";
    }

    @Override
    public @NotNull ItemStack getIcon()
    {
        ItemStack iconStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta iconMeta = (SkullMeta) iconStack.getItemMeta();
        iconMeta.setPlayerProfile(HeadUtility.getPlayerProfile(this.language.getTexture()));
        iconStack.setItemMeta(iconMeta);
        return iconStack;
    }

    @Override
    public String getDefault()
    {
        return "ja-jp";
    }

    @Override
    public @NotNull LinkedHashMap<String, String> getOptions()
    {
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        Language.getInstances().forEach(lang -> options.put(lang.getId(), lang.getName()));
        return options;
    }

    public @NotNull Language getLanguage()
    {
        return this.language;
    }

    public void setLanguage(@NotNull Language language)
    {
        this.language = language;
        this.value = this.language.getId();
        super.save();
    }

    @Override
    public void save()
    {
        super.save();
        this.language = Language.getInstance(super.value);
    }
}
