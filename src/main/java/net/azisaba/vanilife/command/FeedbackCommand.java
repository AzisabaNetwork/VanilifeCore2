package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
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
import java.util.List;

public class FeedbackCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /feedback <details>").color(NamedTextColor.RED));
            return true;
        }

        if (255 < args[0].length())
        {
            sender.sendMessage(Language.translate("cmd.feedback.limit-over", player).color(NamedTextColor.RED));
            return true;
        }

        if (User.getInstance(player).getStatus() == UserStatus.MUTED)
        {
            sender.sendMessage(Language.translate("msg.muted", player).color(NamedTextColor.RED));
            return true;
        }

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("フィードバック")
                .addField("送信者", String.format("%s (%s)", player.getName(), player.getUniqueId()), false)
                .setColor(new Color(216, 197, 226))
                .build()).queue();

        Vanilife.CHANNEL_CONSOLE.sendMessage(":envelope_with_arrow: " + Vanilife.ROLE_DEVELOPER.getAsMention() + " 1件の新しいフィードバックを受信しました").queue();

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

        sender.sendMessage(Language.translate("cmd.feedback.thank-you.1", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        sender.sendMessage(Language.translate("cmd.feedback.thank-you.2", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        sender.sendMessage(Language.translate("cmd.feedback.thank-you.3", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
