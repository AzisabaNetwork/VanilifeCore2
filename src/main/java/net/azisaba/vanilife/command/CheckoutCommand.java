package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        int i = 0;
        int total = 0;

        for (User user : User.getInstances())
        {
            for (ISubscription subscription : new ArrayList<>(user.getSubscriptions()))
            {
                subscription.checkout(user);
                i ++;
                total += subscription.getCost();
            }
        }

        sender.sendMessage(Component.text(i + " 件のサブスクリプションを処理しました").color(NamedTextColor.GREEN));

        Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("チェックアウト")
                .setDescription(i + "件のサブスクリプションを処理しました")
                .setColor(Color.GREEN)
                .addField("責任者", sender.getName(), false)
                .addField("総額", total + " Mola", false)
                .build()).queue();

        Vanilife.CHANNEL_CONSOLE.sendMessage(":envelope_with_arrow: " + Vanilife.ROLE_DEVELOPER.getAsMention() + " 支払処理を実行しました").queue();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
