package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.ui.CLI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TeleportRequest extends RequestImpl
{
    public TeleportRequest(Player from, Player to)
    {
        super(from, to);

        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        from.sendMessage(Component.text("").append(to.displayName())
                .append(Component.text(" に Teleport リクエストを送信しました！承認の有効期限は ").color(NamedTextColor.YELLOW)
                        .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                        .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW))));
        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        to.sendMessage(Component.text("").append(from.displayName()).append(Component.text(" から Teleport リクエストが届きました！").color(NamedTextColor.YELLOW)));
        to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/tpa %s", from.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /tpa %s を実行", from.getName()))))));
        to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }

    @Override
    public long getTicks()
    {
        return 20L * 60;
    }

    @Override
    public Class<? extends IRequest> getClazz()
    {
        return TeleportRequest.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();

        this.from.teleport(this.to.getLocation());

        this.from.sendMessage(Component.text(String.format("%s にテレポートしました！", this.to.getName())).color(NamedTextColor.GREEN));
        this.to.sendMessage(Component.text(String.format("%s のテレポートを承認しました！", this.from.getName())).color(NamedTextColor.GREEN));

        this.from.playSound(this.from, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
        this.to.playSound(this.to, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)
                .append(Component.text(String.format(" 秒が経過したため、%s への Teleport リクエストは無効になりました", this.to.getName())).color(NamedTextColor.YELLOW)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
