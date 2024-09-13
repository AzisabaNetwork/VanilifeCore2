package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
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
            sender.sendMessage(Component.text("第一引数は255文字以内に収める必要があります").color(NamedTextColor.RED));
            return true;
        }

        if (User.getInstance(player).getStatus() == UserStatus.MUTED)
        {
            sender.sendMessage(Component.text("あなたは現在ミュートされています").color(NamedTextColor.RED));
            return true;
        }

        Vanilife.channel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("フィードバック")
                .setDescription(Vanilife.ROLE_DEVELOPER.getAsMention() + args[0])
                .addField("送信者", String.format("%s (%s)", player.getName(), player.getUniqueId()), false)
                .setColor(new Color(216, 197, 226))
                .build()).queue();

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

        sender.sendMessage(Component.text("フィードバックをありがとうございました！").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        sender.sendMessage(Component.text("お寄せいただいたフィードバックは開発者が直接確認させていただき、ユーザー体験向上に努めて参ります。").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        sender.sendMessage(Component.text("今後ともばにらいふ！をよろしくお願いいたします。").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
