package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DomainTransferUI extends AnvilUI
{
    private final Domain domain;

    public DomainTransferUI(@NotNull Player player, @NotNull Domain domain)
    {
        super(player, Language.translate("ui.domain-transfer.title", player));
        this.domain = domain;
    }

    @Override
    public @NotNull String getPlaceholder()
    {
        return ((TextComponent) Language.translate("ui.domain-transfer.placeholder", this.player)).content();
    }

    @Override
    protected void onTyped(@NotNull String string)
    {
        super.onTyped(string);

        User registrant = User.getInstances().stream().filter(user -> user.getPlaneName().equals(string)).findFirst().orElse(null);

        if (registrant == null)
        {
            this.player.sendMessage(Language.translate("ui.domain-transfer.not-found", this.player).color(NamedTextColor.RED));
            return;
        }

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> {
            new ConfirmUI(this.player, () -> {
                DomainTransferUI.this.domain.setRegistrant(registrant);
                this.player.sendMessage(Language.translate("ui.domain-transfer.transferred", this.player, "name=" + registrant.getPlaneName()).color(NamedTextColor.GREEN));
            }, () -> {
                this.player.sendMessage(Language.translate("ui.domain-transfer.cancelled", this.player).color(NamedTextColor.RED));
            });
        }, 10L);
    }
}
