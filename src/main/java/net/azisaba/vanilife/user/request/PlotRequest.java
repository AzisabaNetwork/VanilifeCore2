package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.CLI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotRequest extends Request
{
    private final Plot plot;

    public PlotRequest(@NotNull Player from, @NotNull Plot to)
    {
        super(from, to.getOwner().getAsPlayer());

        this.plot = to;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(String.format("Plot %s に 参加申請を送信しました！承認の有効期限は ", this.plot.getName())).color(NamedTextColor.YELLOW)
                        .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                        .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Component.text("").append(from.displayName()).append(Component.text(String.format(" から Plot %s への参加申請が届きました！", this.plot.getName())).color(NamedTextColor.YELLOW)));
        this.to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/plot invite %s", from.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /plot invite %s を実行", from.getName()))))));
        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
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
        return PlotRequest.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();
        this.plot.addMember(this.fromUser);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)
                .append(Component.text(String.format(" 秒が経過したため、%s への Plot 申請は無効になりました", this.plot.getName())).color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
