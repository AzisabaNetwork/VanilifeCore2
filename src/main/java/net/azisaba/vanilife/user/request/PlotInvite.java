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

public class PlotInvite extends Request
{
    private final Plot plot;

    public PlotInvite(@NotNull Plot from, @NotNull Player to)
    {
        super(from.getOwner().asPlayer(), to);

        this.plot = from;

        final int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.plot-invite.invited", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "plot=" + plot.getName(), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.plot-invite.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName()), "plot=" + plot.getName()));
        this.to.sendMessage(Language.translate("msg.plot-invite.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand(String.format("//plot join %s", this.plot.getName()))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=//plot join " + plot.getName())))));
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
        return PlotInvite.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();

        this.plot.addMember(User.getInstance(this.from));

        this.from.sendMessage(Language.translate("msg.plot.joined", this.from, "plot=" + this.plot.getName()));
        this.to.sendMessage(Language.translate("msg.plot.accept", this.to, "name=" + ComponentUtility.asString(this.toUser.getName()), "plot=" + this.plot.getName()));
    }

    @Override
    public void onTimeOver()
    {
        super.onTimeOver();
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.plot-invite.time-over", this.from, "limit=" + (this.getTicks() / 20), "name=" + ComponentUtility.asString(this.toUser.getName())));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
    }
}
