package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotInvitation extends Request
{
    private final Plot plot;

    public PlotInvitation(@NotNull Plot from, @NotNull Player to)
    {
        super(from.getOwner().getAsOfflinePlayer().getPlayer(), to);

        this.plot = from;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text("").append(to.displayName())
                .append(Component.text(String.format(" を %s に招待しました！承認の有効期限は ", this.plot.getName())).color(NamedTextColor.YELLOW)
                        .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                        .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW))));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Component.text("").append(this.from.displayName()).append(Component.text(String.format(" があなたを %s に招待しました！", this.plot.getName())).color(NamedTextColor.YELLOW)));
        this.to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/plot join %s", this.plot.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /plot join %s を実行", this.plot.getName()))))));
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
        return PlotInvitation.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();

        this.plot.addMember(User.getInstance(this.to));

        this.from.sendMessage(this.toUser.getName().append(Component.text(String.format(" が %s に参加しました！", this.plot.getName())).color(NamedTextColor.YELLOW)));
        this.to.sendMessage(Component.text(String.format("%s に参加しました！", this.plot.getName())).color(NamedTextColor.YELLOW));
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)
                .append(Component.text(String.format(" 秒が経過したため、%s への Plot 招待は無効になりました", this.to.getName())).color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
