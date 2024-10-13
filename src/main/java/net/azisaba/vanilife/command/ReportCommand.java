package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.report.ReportDataContainer;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.ReportUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.util.Watch;
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

public class ReportCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1 && args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /report [player] <details>").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        if (user.getStatus() != UserStatus.DEFAULT)
        {
            player.sendMessage(Language.translate("msg.muted", player).color(NamedTextColor.RED));
            return true;
        }

        String details = (args.length == 1) ? args[0] : args[1];

        if (250 < details.length())
        {
            sender.sendMessage(Language.translate("cmd.report.limit-over", player).color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 1)
        {
            new ReportUI(player, new ReportDataContainer(user, null, args[0]));
        }
        else
        {
            Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

                if (uuid == null)
                {
                    sender.sendMessage(Language.translate("msg.not-found.player", player, "name=" + args[0]).color(NamedTextColor.RED));
                    return;
                }

                if (! UserUtility.exists(uuid))
                {
                    sender.sendMessage(Language.translate("msg.not-found.user", player, "name=" + args[0]).color(NamedTextColor.RED));
                    return;
                }

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> new ReportUI(player, new ReportDataContainer(user, User.getInstance(args[0]), args[1])));
            });
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> ! Watch.isWatcher(p))
                    .forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
