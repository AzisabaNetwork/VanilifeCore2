package net.azisaba.vanilife.command;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.ReferralUI;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.referral.Referral;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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

public class ReferralCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0)
        {
            new ReferralUI(player);
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /referral [player]").color(NamedTextColor.RED));
            return true;
        }

        User user = User.getInstance(player);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            UUID referee = Bukkit.getPlayerUniqueId(args[0]);

            if (referee == null)
            {
                sender.sendMessage(Language.translate("msg.not-found.player", player, "name=" + args[0]).color(NamedTextColor.RED));
                return;
            }

            Referral referral = Referral.getInstance(user, referee);

            if (referral != null)
            {
                sender.sendMessage(Language.translate("cmd.referral.token", player)
                        .append(Component.text(referral.getToken())
                                .color(NamedTextColor.GREEN)
                                .hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-copy", player)))
                                .clickEvent(ClickEvent.copyToClipboard(referral.getToken()))));
                return;
            }

            referral = new Referral(user, referee);

            sender.sendMessage(Component.text().build());
            sender.sendMessage(Language.translate("cmd.referral.issued", player, "referee=" + args[0]).color(NamedTextColor.AQUA));
            sender.sendMessage(Language.translate("cmd.referral.token", player)
                    .append(Component.text(referral.getToken())
                            .color(NamedTextColor.GREEN)
                            .hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-copy", player)))
                            .clickEvent(ClickEvent.copyToClipboard(referral.getToken()))));
            sender.sendMessage(Component.text().build());

            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        return List.of();
    }
}
