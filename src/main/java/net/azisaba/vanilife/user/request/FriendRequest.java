package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.ui.CLI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class FriendRequest extends RequestImpl
{
    public FriendRequest(Player from, Player to)
    {
        super(from, to);

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text("").append(to.displayName())
                .append(Component.text(" に Friend 申請を送信しました！承認の有効期限は ").color(NamedTextColor.YELLOW)
                        .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                        .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW))));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Component.text("").append(from.displayName()).append(Component.text(" から Friend 申請が届きました！").color(NamedTextColor.YELLOW)));
        this.to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/friend %s", from.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /friend %s を実行", from.getName()))))));
        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }

    @Override
    public long getTicks()
    {
        return 20L * 60;
    }

    @Override
    public Class<? extends IRequest> getClazz()
    {
        return FriendRequest.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();

        this.getFromUser().friend(this.getToUser());

        this.from.sendMessage(this.toUser.getName().append(Component.text(" と Friend になりました！").color(NamedTextColor.YELLOW)));
        this.to.sendMessage(this.fromUser.getName().append(Component.text(" と Friend になりました！").color(NamedTextColor.YELLOW)));
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)
                .append(Component.text(String.format(" 秒が経過したため、%s への Friend 申請は無効になりました", this.to.getName())).color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
