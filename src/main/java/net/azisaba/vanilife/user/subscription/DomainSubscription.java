package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DomainSubscription implements ISubscription
{
    private final Domain domain;

    public DomainSubscription(@NotNull Domain domain)
    {
        this.domain = domain;
    }

    @Override
    public @NotNull String getName()
    {
        return "domain";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.PAPER;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.domain.details.1", "domain=" + this.domain.getUrl()),
                Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return Vanilife.MOLA_DOMAIN;
    }

    public @NotNull Domain getDomain()
    {
        return this.domain;
    }

    @Override
    public void onShortage(@NotNull User user)
    {
        user.unsubscribe(this);
        this.domain.delete();
        this.domain.getRegistrant().sendNotification(String.format("%s を維持できませんでした", this.domain.getUrl()),
                String.format("ご契約いただいていた %s は維持費の引き落としに失敗したため、削除されました。", this.domain.getUrl()));
    }

    @Override
    public void onPayment(@NotNull User user)
    {
        this.domain.getRegistrant().sendNotification("[領収書] ドメイン維持費", String.format("%s の維持費として下記生に領収いたしました。\nドメイン維持費: %s Mola", this.domain.getUrl(), this.getCost()));
    }
}
