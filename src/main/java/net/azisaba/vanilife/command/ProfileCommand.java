package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.ProfileUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileCommand implements CommandExecutor, TabCompleter
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
            sender.sendMessage(Component.text("Correct syntax: /profile <player>").color(NamedTextColor.RED));
            return true;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
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

                User user = User.getInstance(player);
                User view = User.getInstance(args[0]);

                if (view.isBlock(user))
                {
                    sender.sendMessage(Language.translate("cmd.profile.cant", player).color(NamedTextColor.RED));
                    return;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new ProfileUI(player, view);
                    }
                }.runTask(Vanilife.getPlugin());
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
