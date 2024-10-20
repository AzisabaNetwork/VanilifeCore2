package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.aww.WebPage;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AzireSubscription implements ISubscription
{
    private final WebPage page;

    public AzireSubscription(@NotNull WebPage page)
    {
        this.page = page;
    }

    @Override
    public @NotNull String getName()
    {
        return "azire";
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.COD;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.azire.details.1", "domain=" + this.page.getDomain().getUrl(), "page=" + this.page.getName()),
                Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 100;
    }

    @Override
    public void onShortage(@NotNull User user)
    {
        user.unsubscribe(this);
        this.page.getDomain().removePage(this.page);
        this.page.getDomain().getRegistrant().sendNotification(String.format("%s/%s を維持できませんでした", this.page.getDomain().getUrl(), this.page.getName()),
                String.format("%s でホストしていた %s は維持費の引き落としに失敗したため、削除されました。", this.page.getDomain().getUrl(), this.page.getName()));
    }

    @Override
    public void onPayment(@NotNull User user)
    {
        this.page.getDomain().getRegistrant().sendNotification("[領収書] Web ページ維持費", String.format("%s/%s の維持費として下記生に領収いたしました。\nドメイン維持費: %s Mola", this.page.getDomain().getUrl(), this.page.getName(), this.getCost()));
    }
}
