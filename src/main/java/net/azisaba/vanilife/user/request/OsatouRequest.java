package net.azisaba.vanilife.user.request;

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

public class OsatouRequest extends Request
{
    public OsatouRequest(@NotNull Player from, @NotNull Player to)
    {
        super(from, to);

        final int limit = (int) (this.getTicks() / 20);

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
        this.from.sendMessage(Language.translate("msg.osatou.requested", this.from, "name=" + ComponentUtility.getAsString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
        this.to.sendMessage(Language.translate("msg.osatou.received", this.to, "name=" + ComponentUtility.getAsString(this.fromUser.getName())));
        this.to.sendMessage(Language.translate("msg.osatou.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("/osatou %s", from.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=/osatou " + from.getName())))));
        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.LIGHT_PURPLE));
    }

    @Override
    public long getTicks()
    {
        return 20L * 60;
    }

    @Override
    public @NotNull Class<? extends IRequest> getClazz()
    {
        return OsatouRequest.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();

        this.getToUser().setOsatou(this.fromUser);

        this.from.sendMessage(Language.translate("msg.osatou.hold", this.from, "name=" + ComponentUtility.getAsString(this.toUser.getName())));
        this.from.playSound(this.from, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);

        this.to.sendMessage(Language.translate("msg.osatou.hold", this.to, "name=" + ComponentUtility.getAsString(this.fromUser.getName())));
        this.to.playSound(this.to, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.osatou.time-over", this.from, "limit=" + (this.getTicks() / 20), "name=" + ComponentUtility.getAsString(this.toUser.getName())));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
