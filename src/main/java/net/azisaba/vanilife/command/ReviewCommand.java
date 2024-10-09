package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.runnable.ReviewRunnable;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.MathUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class ReviewCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (! ReviewRunnable.canReview(player))
        {
            sender.sendMessage(Language.translate("review.cant", player).color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /review <score>"));
            return true;
        }

        if (! MathUtility.isInt(args[0]) || Integer.parseInt(args[0]) <= 0 || 5 < Integer.parseInt(args[0]))
        {
            sender.sendMessage(Language.translate("review.out-of-range", player).color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);
        user.write("review.timestamp", Vanilife.sdf3.format(new Date()));
        user.write("review.score", Integer.parseInt(args[0]));

        sender.sendMessage(Language.translate("review.thank-you", player).color(NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            return List.of();
        }

        return List.of("1", "2", "3", "4", "5");
    }
}
