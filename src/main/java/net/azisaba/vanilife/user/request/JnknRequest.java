package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.arcade.Jnkn;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
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

        final int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.jnkn.requested", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.jnkn.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName())));
        this.to.sendMessage(Language.translate("msg.jnkn.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/jnkn %s", from.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=/jnkn " + this.from.getName())))));
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
        return JnknRequest.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();

        new Jnkn(this.from, this.to);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.jnkn.time-over", this.from, "limit=" + (this.getTicks() / 20), "name=" + ComponentUtility.asString(this.toUser.getName())));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
