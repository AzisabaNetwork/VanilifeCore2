package net.azisaba.vanilife.user.request;

import net.azisaba.vanilife.chat.GroupChat;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatInvite extends Request
{
    private final GroupChat chat;

    public ChatInvite(@NotNull GroupChat from, @NotNull Player to)
    {
        super(from.getOwner().asPlayer(), to);

        this.chat = from;

        final int limit = (int) this.getTicks() / 20;

        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.from.sendMessage(Language.translate("msg.chat.invited", this.from, "name=" + ComponentUtility.asString(this.toUser.getName()), "limit=" + limit));
        this.from.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        this.to.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.to.sendMessage(Language.translate("msg.chat.received", this.to, "name=" + ComponentUtility.asString(this.fromUser.getName()), "chat=" + this.chat.getName()));
        this.to.sendMessage(Language.translate("msg.chat.received.details", this.to, "limit=" + limit)
                .append(Language.translate("msg.click-to-accept", this.to).color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("//chat join " + this.chat.getName())).hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", this.to, "command=//chat join " + this.chat.getName())))));
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
        return Request.class;
    }

    @Override
    public void onAccept()
    {
        super.onAccept();

        this.chat.addMember(this.toUser);
        this.chat.getOnline().forEach(member -> member.asPlayer().sendMessage(Component.text(this.chat.getName() + " > ").color(NamedTextColor.BLUE).append(this.toUser.getName(member)).append(Language.translate("msg.chat.join", member).color(NamedTextColor.GRAY))));
    }
}
