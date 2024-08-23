package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotSetSpawnSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "setspawn";
    }

    @Override
    public boolean isOpCommand()
    {
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot setspawn").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());
        User user = User.getInstance(player);

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        if (user != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        plot.setSpawn(player.getLocation());
        sender.sendMessage(Component.text("Plot のスポーン地点を設定しました").color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
