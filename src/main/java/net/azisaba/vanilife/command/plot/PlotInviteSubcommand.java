package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.request.PlotInvite;
import net.azisaba.vanilife.user.request.PlotRequest;
import net.azisaba.vanilife.user.request.TradeRequest;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlotInviteSubcommand implements ISubcommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "invite";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.DEFAULT;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: //plot invite <player> <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[1]);

        if (plot == null)
        {
            sender.sendMessage(Language.translate("cmd.plot.not-found", player).color(NamedTextColor.RED));
            return;
        }

        if (player.getName().equals(args[0]))
        {
            sender.sendMessage(Language.translate("cmd.plot.invite.cant-yourself", player).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Language.translate("cmd.plot.permission-error", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (Bukkit.getPlayerExact(args[0]) == null)
        {
            sender.sendMessage(Language.translate("msg.offline", player, "name=" + args[0]).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        User member = User.getInstance(args[0]);

        if (plot.getOwner().getRequests().stream().anyMatch(r -> r.match(PlotRequest.class, member.getAsPlayer())))
        {
            plot.getOwner().getRequests().stream().filter(r -> r.match(PlotRequest.class, member.getAsPlayer())).toList().getFirst().onAccept();
            return;
        }

        if (plot.getMembers().contains(member))
        {
            sender.sendMessage(Language.translate("cmd.plot.invite.already", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (! UserUtility.isAdmin(sender) && plot.getOwner().getRequests().stream().anyMatch(r -> r.match(TradeRequest.class, player)))
        {
            sender.sendMessage(Language.translate("cmd.plot.invite.already-sent", player).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        new PlotInvite(plot, Bukkit.getPlayer(args[0]));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
