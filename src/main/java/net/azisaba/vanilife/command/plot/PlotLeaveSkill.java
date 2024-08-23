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

public class PlotLeaveSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "leave";
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

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot leave <plot>").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(args[0]);

        if (plot == null)
        {
            sender.sendMessage(Component.text(String.format("%s は未定義の Plot です", args[0])).color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (plot.getOwner() == user)
        {
            sender.sendMessage(Component.text("Plot のマスターはこれを実行できません").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("削除したい場合は、代わりに /plot delete を使用してください…").color(NamedTextColor.YELLOW));
            return;
        }

        if (! plot.isMember(user))
        {
            sender.sendMessage(Component.text("あなたはこの Plot のメンバーではありません").color(NamedTextColor.RED));
            return;
        }

        plot.removeMember(user);
        sender.sendMessage(Component.text(String.format("%s を離脱しました！", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            return new ArrayList<>();
        }

        ArrayList<String> suggest = new ArrayList<>();
        User user = User.getInstance(player);

        if (args.length == 1)
        {
            Plot.getInstances().stream().filter(i -> i.isMember(user)).forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
