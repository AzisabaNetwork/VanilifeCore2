package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.runnable.ReviewRunnable;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.MathUtility;
import net.azisaba.vanilife.util.Typing;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
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

        new Typing(player)
        {
            @Override
            public void init()
            {
                sender.sendMessage(Language.translate("review.why", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                sender.sendMessage(Language.translate("review.thank-you", player).color(NamedTextColor.GREEN));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

                Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("レビュー")
                        .setColor(Color.MAGENTA)
                        .addField("送信者", String.format("%s (%s)", user.getPlaneName(), user.getId()), false)
                        .addField("スコア", args[0], false)
                        .addField("評価の詳細", string, false)
                        .build()).queue();
            }
        };

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
