package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnjailCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender))
        {
            sender.sendMessage(Component.text("You do not have sufficient permissions to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        Language lang = (sender instanceof Player player) ? Language.getInstance(player) : Language.getInstance("ja-jp");

        if (lang == null)
        {
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

            if (uuid == null)
            {
                sender.sendMessage(lang.translate("msg.not-found.player", "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            if (! UserUtility.exists(uuid))
            {
                sender.sendMessage(lang.translate("msg.not-found.user", "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            User target = User.getInstance(args[0]);

            if (! target.isJailed())
            {
                sender.sendMessage(Component.text("このプレイヤーは現在 Jail されていません").color(NamedTextColor.RED));
                return;
            }

            target.unjail();
            sender.sendMessage(Component.text(args[0] + " の Jail を解除しました").color(NamedTextColor.RED));

            Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("Jail 解除通知")
                    .addField("対象者", String.format("%s (%s)", target.getPlaneName(), target.getId()), false)
                    .addField("実行者", sender instanceof Player player ? String.format("%s (%s)", player.getName(), player.getUniqueId()) : sender.getName(), false)
                    .build()).queue();
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1 && UserUtility.isAdmin(sender))
        {
            User.getInstances().stream().filter(User::isJailed).forEach(user -> suggest.add(user.getPlaneName()));
        }

        return suggest;
    }
}
