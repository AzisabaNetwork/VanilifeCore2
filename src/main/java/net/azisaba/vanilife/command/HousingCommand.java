package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.objective.Objectives;
import net.azisaba.vanilife.ui.HousingUI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.user.request.HousingInvite;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

        if (user.getStatus() == UserStatus.JAILED)
        {
            return true;
        }

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
                if (user.getTrustRank().getLevel() < TrustRank.NEW.getLevel())
                {
                    player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                    sender.sendMessage(Language.translate("cmd.housing.cant-create", player).color(NamedTextColor.RED));
                    return true;
                }

                user.setHousing(new Housing(user));
            }

            if (! user.isAchieved(Objectives.START_HOUSING))
            {
                user.achieve(Objectives.START_HOUSING);
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

            if (! housing.canVisit(user) && ! UserUtility.isAdmin(user) && user.getRequests().stream().noneMatch(r -> r.match(HousingInvite.class, housing.getUser().asPlayer())))
            {
                sender.sendMessage(Language.translate("cmd.housing.permission-error", player).color(NamedTextColor.RED));
                return;
            }

            if (user.getRequests().stream().anyMatch(r -> r.match(HousingInvite.class, housing.getUser().asPlayer())))
            {
                user.getRequests().stream().filter(r -> r.match(HousingInvite.class, housing.getUser().asPlayer())).toList().getFirst().onAccept();
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
