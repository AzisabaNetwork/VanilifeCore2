package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.trade.Trade;
import net.azisaba.vanilife.ui.CLI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TradeRequest extends Request
{
    public TradeRequest(@NotNull Player from, @NotNull Player to)
    {
        super(from, to);

        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        from.sendMessage(Component.text("").append(to.displayName())
                .append(Component.text(" を Trade に招待しました！承認の有効期限は ").color(NamedTextColor.YELLOW)
                .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW))));
        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        to.sendMessage(Component.text("").append(from.displayName()).append(Component.text(" があなたを Trade に招待しました！").color(NamedTextColor.YELLOW)));
        to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/trade %s", from.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /trade %s を実行", from.getName()))))));
        to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }

    @Override
    public long getTicks()
    {
        return 20L * 60;
    }

    @Override
    @NotNull
    public Class<? extends IRequest> getClazz()
    {
        return TradeRequest.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();
        new Trade(this.from, this.to);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)
                .append(Component.text(String.format(" 秒が経過したため、%s への Trade 申請は無効になりました", this.to.getName())).color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
