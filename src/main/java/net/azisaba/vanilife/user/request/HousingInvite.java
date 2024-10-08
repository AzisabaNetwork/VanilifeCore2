package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HousingInvite extends Request
{
    public HousingInvite(@NotNull Housing from, @NotNull Player to)
    {
        super(from.getUser().asPlayer(), to);

        final int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.housing.requested", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.housing.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName())));
        this.to.sendMessage(Language.translate("msg.housing.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/housing " + this.from.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=/housing " + this.from.getName()))));
        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }

    @Override
    public long getTicks()
    {
        return 20L * 60;
    }

    @Override
    public @NotNull Class<? extends IRequest> getClazz()
    {
        return HousingInvite.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();
        this.from.sendMessage(Language.translate("msg.housing.accept", this.from, "name=" + ComponentUtility.asString(this.toUser.getName())));
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.housing.time-over", this.from, "limit=" + (this.getTicks() / 20)));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
