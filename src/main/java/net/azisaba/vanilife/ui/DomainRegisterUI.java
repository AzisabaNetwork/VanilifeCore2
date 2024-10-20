package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.aww.Domain;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.SubdomainIssuanceRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DomainRegisterUI extends AnvilUI
{
    public static void register(@NotNull Player player, @NotNull String domain)
    {
        if (! Domain.isValid(domain))
        {
            player.sendMessage(Language.translate("ui.domain-register.invalid", player).color(NamedTextColor.RED));
            return;
        }

        if (Domain.getInstance(domain) != null)
        {
            player.sendMessage(Language.translate("ui.domain-register.already", player).color(NamedTextColor.RED));
            return;
        }

        if (domain.contains(".") && Domain.getInstance(Domain.registry(domain)) == null)
        {
            player.sendMessage(Language.translate("ui.domain-register.not-found", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (! UserUtility.isAdmin(user) && ! domain.contains("."))
        {
            player.sendMessage(Language.translate("ui.domain-register.top-level", player).color(NamedTextColor.RED));
            return;
        }

        if (user.getMola() < 400 && domain.contains("."))
        {
            player.sendMessage(Language.translate("msg.shortage", player, "need=" + (400 - user.getMola())).color(NamedTextColor.RED));
            player.closeInventory();
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        Domain.register(domain, User.getInstance(player));
        player.sendMessage(Language.translate("ui.domain-register.registered", player, "domain=" + domain).color(NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

        if (domain.contains("."))
        {
            user.setMola(user.getMola() - 400);
        }
    }

    public DomainRegisterUI(@NotNull Player player)
    {
        super(player, Language.translate("ui.domain-register.title", player));
    }

    @Override
    public @NotNull String getPlaceholder()
    {
        return ((TextComponent) Language.translate("ui.domain-register.placeholder", this.player)).content();
    }

    @Override
    protected void onTyped(@NotNull String string)
    {
        string = string.toLowerCase();

        String domain = Domain.reverse(string);
        Domain parent = Domain.getInstance(Domain.parent(domain));

        if (parent != null && ! parent.isTopLevel() && ! parent.getRegistrant().getId().equals(this.player.getUniqueId()))
        {
            if (! parent.getRegistrant().isOnline())
            {
                this.player.sendMessage(Language.translate("ui.domain-register.offline", this.player).color(NamedTextColor.RED));
                return;
            }

            new SubdomainIssuanceRequest(this.player, parent, domain);
            return;
        }

        DomainRegisterUI.register(this.player, domain);
    }
}
