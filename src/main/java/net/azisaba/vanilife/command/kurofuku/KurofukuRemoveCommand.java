package net.azisaba.vanilife.command.kurofuku;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KurofukuRemoveCommand implements ISubcommand
{
    @Override
    public @NotNull String getName()
    {
        return "remove";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.ADMIN;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + command.getName() + " remove <player>").color(NamedTextColor.RED));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

            if (uuid == null)
            {
                sender.sendMessage(Component.text(args[0] + " は不明なプレイヤーです").color(NamedTextColor.RED));
                return;
            }

            if (! UserUtility.exists(uuid))
            {
                sender.sendMessage(Component.text(args[0] + " は不明なユーザーです").color(NamedTextColor.RED));
                return;
            }

            User target = User.getInstance(args[0]);

            if (! target.isKurofuku())
            {
                sender.sendMessage(Component.text("このプレイヤーは黒服ではありません").color(NamedTextColor.RED));
                return;
            }

            target.setKurofuku(false);
            sender.sendMessage(Component.text(args[0] + " を黒服から解除しました").color(NamedTextColor.GREEN));

            Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("黒服解除通知")
                    .setColor(Color.RED)
                    .addField("対象者", String.format("%s (%s)", args[0], target.getId()), false)
                    .addField("実行者", (sender instanceof Player player) ? String.format("%s (%s)", player.getName(), player.getUniqueId()) : sender.getName(), false)
                    .build()).queue();

            Vanilife.CHANNEL_CONSOLE.sendMessage(":dark_sunglasses:  " + Vanilife.ROLE_SUPPORT.getAsMention() + " 黒服解除がありました").queue();
        });
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            User.getInstances().stream().filter(User::isKurofuku).forEach(kurofuku -> suggest.add(kurofuku.getPlaneName()));
        }

        return suggest;
    }
}
