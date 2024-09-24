package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotSubscription implements ISubscription
{
    private final Plot plot;

    public PlotSubscription(Plot plot)
    {
        this.plot = plot;
    }

    @Override
    public @NotNull String getName()
    {
        return "plot";
    }

    @Override
    public @NotNull Component getDisplayName(@NotNull Language lang)
    {
        return Component.text(this.plot.getName() + " (Plot)").color(NamedTextColor.YELLOW);
    }

    @Override
    public @NotNull Material getIcon()
    {
        return Material.GRASS_BLOCK;
    }

    @Override
    public @NotNull List<Component> getDetails(@NotNull Language lang)
    {
        return List.of(lang.translate("subscription.plot.details.1", "chunks=" + this.plot.getChunks().size()),
                Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN).append(Component.text(" (").color(NamedTextColor.GRAY).append(Component.text("➡").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD)).append(Component.text("40 Mola × " + this.plot.getChunks().size() + ")").color(NamedTextColor.GRAY))));
    }

    @Override
    public int getCost()
    {
        return this.plot.getChunks().size() * 40;
    }

    public Plot getPlot()
    {
        return this.plot;
    }

    @Override
    public void onShortage(@NotNull User user)
    {
        this.plot.delete();
        this.plot.getOwner().sendNotice(String.format("Plot %s を維持できませんでした", this.plot.getName()), String.format("あなたが所有していた %s の維持費は Mola が不足していたために、引き落としされませんでした。\nつきましては %s が削除されましたので、通知させていただきました。\n\n請求額: %s Mola\n引き落とし時残高: %s Mola\n不足額: %s Mola\n\n以上、ご確認よろしくお願いします。", plot.getName(), plot.getName(), this.getCost(), this.plot.getOwner().getMola(), Math.abs(this.getCost() - this.plot.getOwner().getMola())));
    }

    @Override
    public void onPayment(@NotNull User user)
    {
        this.plot.getOwner().sendNotice(String.format("[領収書] %s 維持費", plot.getName()), String.format("%s の維持費として下記生に領収いたしました。\nPlot 維持費: %s Mola (40 × %s チャンク)", this.plot.getName(), this.getCost(), this.plot.getChunks().size()));
    }
}
