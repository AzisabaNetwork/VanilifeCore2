package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        boolean hasPermission = ! (sender instanceof Player player) || UserUtility.isModerator(User.getInstance(player));

        if (! hasPermission)
        {
            sender.sendMessage(Component.text("You do not have sufficient permission to execute the command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <player>").color(NamedTextColor.RED));
            return true;
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

            if (target.getStatus() != UserStatus.JAILED)
            {
                sender.sendMessage(Component.text("このプレイヤーは Jail されていません").color(NamedTextColor.RED));
                return;
            }

            target.setStatus(UserStatus.DEFAULT);

            if (target.isOnline())
            {
                VanilifeWorld world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

                if (world != null)
                {
                    world.getTeleporter().teleport(target.asPlayer());
                    target.asPlayer().setGameMode(GameMode.SURVIVAL);
                }
            }

            sender.sendMessage(Component.text(args[0] + " の Jail を解除しました").color(NamedTextColor.GREEN));

            Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("Jail 解除通知")
                    .addField("対象者", String.format("%s (%s)", args[0], target.getId()), false)
                    .addField("実行者", (sender instanceof Player player) ? String.format("%s (%s)", player.getName(), player.getUniqueId()) : sender.getName(), false)
                    .build()).queue();

            Vanilife.CHANNEL_CONSOLE.sendMessage(":unlocked: " + Vanilife.ROLE_SUPPORT.getAsMention() + " Jail の解除がありました").queue();
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            User.getInstances().stream().filter(user -> user.getStatus() == UserStatus.JAILED).forEach(user -> suggest.add(user.getPlaneName()));
        }

        return suggest;
    }
}
