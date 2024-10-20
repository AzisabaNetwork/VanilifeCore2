package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.DomainRegisterUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubdomainIssuanceRequest extends Request
{
    private final Domain domain;
    private final String subdomain;

    public SubdomainIssuanceRequest(@NotNull Player from, @NotNull Domain to, @NotNull String subdomain)
    {
        super(from, to.getRegistrant().asPlayer());
        this.domain = to;
        this.subdomain = subdomain;

        int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.subdomain.requested", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.subdomain.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName()), "subdomain=" + Domain.reverse(this.subdomain)));
        this.to.sendMessage(Language.translate("msg.subdomain.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/accept " + this.id))).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=/accept " + this.id))));
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
        return SubdomainIssuanceRequest.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();
        DomainRegisterUI.register(this.from, this.subdomain);

        this.from.sendMessage(Language.translate("msg.subdomain.accepted", this.from, "name=" + this.from.getName(), "subdomain=" + Domain.reverse(this.subdomain)).color(NamedTextColor.GREEN));
        this.to.sendMessage(Language.translate("msg.subdomain.accepted", this.to, "name=" + this.from.getName(), "subdomain=" + Domain.reverse(this.subdomain)).color(NamedTextColor.GREEN));
    }
}
