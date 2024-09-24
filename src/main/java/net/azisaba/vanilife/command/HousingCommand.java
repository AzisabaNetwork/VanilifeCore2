package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.ui.HousingUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.FriendRequest;
import net.azisaba.vanilife.user.request.HousingInvite;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
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

import java.util.List;
import java.util.UUID;

public class HousingCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (1 < args.length)
        {
            sender.sendMessage(Component.text("Correct syntax: /housing [player]"));
            return true;
        }

        User user = User.getInstance(player);

        VanilifeWorld world = VanilifeWorld.getInstance(player.getWorld());

        if (world != null)
        {
            world.setLocation(player, player.getLocation());
        }

        if (args.length == 0)
        {
            Housing housing = Housing.getInstance(player.getLocation());

            if (user.inHousing() && housing != null && housing.getUser() == user)
            {
                new HousingUI(player, user.getHousing());
                return true;
            }

            final String cmd = "housing " + player.getName();

            if (! user.hasHousing())
            {
                user.setHousing(new Housing(user));
            }

            Bukkit.dispatchCommand(player, cmd);
            return true;
        }

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

            User owner = User.getInstance(args[0]);
            Housing housing = Housing.getInstance(owner);

            if (housing == null)
            {
                sender.sendMessage(Language.translate("cmd.housing.not-found", player).color(NamedTextColor.RED));
                return;
            }

            if (! housing.withInScope(user) && ! UserUtility.isModerator(user) && user.getRequests().stream().noneMatch(r -> r.auth(HousingInvite.class, housing.getUser().getAsPlayer())))
            {
                sender.sendMessage(Language.translate("cmd.housing.permission-error", player).color(NamedTextColor.RED));
                return;
            }

            if (user.getRequests().stream().anyMatch(r -> r.auth(FriendRequest.class, housing.getUser().getAsPlayer())))
            {
                user.getRequests().stream().filter(r -> r.auth(HousingInvite.class, housing.getUser().getAsPlayer())).toList().getFirst().onAllow();
            }

            Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
                player.teleport(housing.getSpawn());
                player.sendActionBar(Component.text("Housing: ").color(NamedTextColor.GREEN).append(housing.getUser().getName()));
            });
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
