package net.azisaba.vanilife.command;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.azisaba.vanilife.user.subscription.Subscriptions;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnsubscribeCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /unsubscribe <subscription>").color(NamedTextColor.RED));
            return true;
        }

        ISubscription subscription = Subscriptions.valueOf(args[0]);

        if (subscription == null)
        {
            sender.sendMessage(Component.text(args[0] + " は未定義の Subscription です").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (! user.hasSubscription(subscription))
        {
            sender.sendMessage(Component.text("この Subscription を購入していません").color(NamedTextColor.RED));
            return true;
        }

        new Typing(player)
        {
            private String confirmCode;

            @Override
            public void init()
            {
                this.confirmCode = this.getConfirmCode(6);
                this.player.sendMessage(Component.text(subscription.getDisplayName() + " を解約しますか？既に支払った料金は返金されません！").color(NamedTextColor.GREEN));
                this.player.sendMessage(Component.text("確認: " + this.confirmCode + " をチャットに送信してください").color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(this.confirmCode))
                {
                    user.unsubscribe(subscription);
                    sender.sendMessage(Component.text(subscription.getDisplayName() + " を解約しました").color(NamedTextColor.GREEN));
                }
                else
                {
                    sender.sendMessage(Component.text(subscription.getDisplayName() + " の解約をキャンセルしました").color(NamedTextColor.RED));
                }
            }
        };

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
