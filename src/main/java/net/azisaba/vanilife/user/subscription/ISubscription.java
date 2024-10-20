package net.azisaba.vanilife.user.subscription;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public interface ISubscription
{
    @NotNull String getName();

    @NotNull default Component getDisplayName(@NotNull Language lang)
    {
        return lang.translate("subscription." + this.getName() + ".name");
    }

    @NotNull Material getIcon();

    @NotNull List<Component> getDetails(@NotNull Language lang);

    int getCost();

    default void onShortage(@NotNull User user)
    {
        user.unsubscribe(this);
        user.sendNotification(ComponentUtility.asString(Language.translate("mail.unpaid.subject", user, "subscription=" + ComponentUtility.asString(this.getDisplayName(Language.getInstance(user))))),
                ComponentUtility.asString(Language.translate("mail.unpaid.message", user,
                        "charge=" + this.getCost(),
                        "shortfall=" + Math.abs(this.getCost() - user.getMola()),
                        "balance=" + user.getMola())));

        user.setTrust(user.getTrust() - 5);
    }

    default void onPayment(@NotNull User user)
    {
        user.sendNotification(ComponentUtility.asString(Language.translate("mail.receipt.subject", user, "subscription=" + ComponentUtility.asString(this.getDisplayName(Language.getInstance(user))))),
                ComponentUtility.asString(Language.translate("mail.receipt.message", user,
                        "subscription=" + ComponentUtility.asString(this.getDisplayName(Language.getInstance(user))),
                        "month=" + (Calendar.getInstance().get(Calendar.MONTH) + 1),
                        "charge=" + this.getCost())));

        user.setTrust(user.getTrust() + 3);
    }

    default void checkout(@NotNull User user)
    {
        if (this.getCost() <= user.getMola())
        {
            user.setMola(user.getMola() - this.getCost());
            this.onPayment(user);
        }
        else
        {
            this.onShortage(user);
        }
    }
}
