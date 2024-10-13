package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
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
        super(from, to.getOwner().asPlayer());

        final int limit = (int) this.getTicks() / 20;

        this.plot = to;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.plot.requested", this.from, "plot=" + this.plot.getName(), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.plot.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName()), "plot=" + this.plot.getName()));
        this.to.sendMessage(Language.translate("msg.plot.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("//plot invite %s %s", from.getName(), this.plot.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=//plot invite " + this.from.getName())))));
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
    public void onAccept()
    {
        super.onAccept();

        this.plot.addMember(User.getInstance(this.from));

        this.from.sendMessage(Language.translate("msg.plot.accept", this.to, "name=" + this.to.getName(), "plot=" + this.plot.getName()));
        this.to.sendMessage(Language.translate("msg.plot.joined", this.from, "plot=" + this.plot.getName()));
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.plot.time-over", this.from, "limit=" + (this.getTicks() / 20), "plot=" + this.plot.getName()));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
