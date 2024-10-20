package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.user.referral.Referral;
import net.azisaba.vanilife.util.PlayerUtility;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReferralUI extends AnvilUI
{
    public ReferralUI(@NotNull Player player)
    {
        super(player, Language.translate("ui.referral.title", player));
    }

    @Override
    public @NotNull String getPlaceholder()
    {
        return ((TextComponent) Language.translate("ui.referral.placeholder", this.player)).content();
    }

    @Override
    protected void onTyped(@NotNull String string)
    {
        Referral referral = Referral.getInstance(string);

        if (referral == null || referral.getReferee() != this.player.getUniqueId())
        {
            this.player.sendMessage(Language.translate("ui.referral.not-found", this.player).color(NamedTextColor.RED));
            return;
        }

        referral.use();
        PlayerUtility.giveItemStack(this.player, new ItemStack(Material.COOKED_BEEF, 64));
        this.player.sendMessage(Language.translate("ui.referral.used", this.player).color(NamedTextColor.GOLD));
        this.player.playSound(this.player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
    }
}
