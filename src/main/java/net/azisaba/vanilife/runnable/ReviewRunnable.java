package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.Afk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReviewRunnable extends BukkitRunnable
{
    public static boolean canReview(User user)
    {
        if (user == null)
        {
            return false;
        }

        if (user.getStatus() != UserStatus.DEFAULT)
        {
            return false;
        }

        if (user.read("review.timestamp") == null || user.read("review.score").getAsInt() <= 0)
        {
            return true;
        }

        if (! user.isOnline() || Afk.isAfk(user.asPlayer()))
        {
            return false;
        }

        try
        {
            Date now = new Date();
            Date timestamp = Vanilife.sdf3.parse(user.read("review.timestamp").getAsString());

            return 5 <= TimeUnit.DAYS.convert(Math.abs(now.getTime() - timestamp.getTime()), TimeUnit.MILLISECONDS);
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    public static boolean canReview(Player player)
    {
        if (player == null)
        {
            return false;
        }

        return ReviewRunnable.canReview(User.getInstance(player));
    }

    @Override
    public void run()
    {
        List<Player> reviewers = Bukkit.getOnlinePlayers().stream().filter(player -> ReviewRunnable.canReview(player)).collect(Collectors.toList());

        if (reviewers.isEmpty())
        {
            return;
        }

        Player reviewer = reviewers.get(Vanilife.random.nextInt(reviewers.size()));

        reviewer.sendMessage(Component.text().build());
        reviewer.sendMessage(Language.translate("review.request", reviewer));
        reviewer.sendMessage(Component.text().append(Component.text("☆").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("☆1"))).clickEvent(ClickEvent.runCommand("/review 1")))
                .append(Component.text("☆").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("☆2"))).clickEvent(ClickEvent.runCommand("/review 2")))
                .append(Component.text("☆").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("☆3"))).clickEvent(ClickEvent.runCommand("/review 3")))
                .append(Component.text("☆").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("☆4"))).clickEvent(ClickEvent.runCommand("/review 4")))
                .append(Component.text("☆").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).hoverEvent(HoverEvent.showText(Component.text("☆5"))).clickEvent(ClickEvent.runCommand("/review 5"))));
        reviewer.sendMessage(Component.text().build());

        reviewer.playSound(reviewer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

        User user = User.getInstance(reviewer);
        user.write("review.timestamp", Vanilife.sdf3.format(new Date()));
        user.write("review.score", -1);
    }
}
