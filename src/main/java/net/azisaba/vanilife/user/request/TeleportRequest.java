package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportRequest extends Request
{
    public TeleportRequest(@NotNull Player from, @NotNull Player to)
    {
        super(from, to);

        final int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.teleport.requested", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.teleport.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName())));
        this.to.sendMessage(Language.translate("msg.teleport.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/tpa %s", from.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.from, "command=/tpa " + this.from.getName())))));
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
        return TeleportRequest.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();

        this.fromUser.setMola(this.fromUser.getMola() - Vanilife.MOLA_TPR);
        this.from.teleport(this.to.getLocation());

        this.from.sendMessage(Language.translate("msg.teleport.teleported", this.from, "cost=" + Vanilife.MOLA_TPR, "name=" + ComponentUtility.asString(this.toUser.getName())));
        this.to.sendMessage(Language.translate("msg.teleport.accept", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName())));

        this.from.playSound(this.from, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
        this.to.playSound(this.to, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.2f);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.teleport.time-over", this.from, "limit=" + (this.getTicks() / 20), "name=" + ComponentUtility.asString(this.toUser.getName())));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
