package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.arcade.Jnkn;
import net.azisaba.vanilife.ui.CLI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JnknRequest extends Request
{
    public JnknRequest(@NotNull Player from, @NotNull Player to)
    {
        super(from, to);

        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        from.sendMessage(Component.text("").append(to.displayName())
                .append(Component.text(" にジャンケンリクエストを送信しました！承認の有効期限は ").color(NamedTextColor.YELLOW)
                        .append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED))
                        .append(Component.text(" 秒です。").color(NamedTextColor.YELLOW))));
        from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        to.sendMessage(Component.text("").append(from.displayName()).append(Component.text(" からジャンケンリクエストが届きました！").color(NamedTextColor.YELLOW)));
        to.sendMessage(Component.text("承認の有効期限は ").color(NamedTextColor.YELLOW).append(Component.text(this.getTicks() / 20).color(NamedTextColor.RED)).append(Component.text(" 秒です。"))
                .append(Component.text("こちらをクリックして参加！").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/jnkn %s", from.getName()))).hoverEvent(HoverEvent.showText(Component.text(String.format("クリックして /jnkn %s を実行", from.getName()))))));
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
        return JnknRequest.class;
    }

    @Override
    public void onAllow()
    {
        super.onAllow();

        new Jnkn(this.from, this.to);
    }
}
