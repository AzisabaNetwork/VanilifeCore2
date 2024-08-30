package net.azisaba.vanilife.command.mola;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.MathUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class MolaGiveSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "give";
    }

    @Override
    public Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /mola give <player> <mola>").color(NamedTextColor.RED));
            return;
        }

        if (Bukkit.getPlayerUniqueId(args[0]) == null)
        {
            sender.sendMessage(Component.text(String.format("%s does not exist.", args[0])).color(NamedTextColor.RED));
            return;
        }

        if (!MathUtility.isInt(args[1]))
        {
            sender.sendMessage(Component.text("第二引数には整数を指定する必要があります: " + args[1]).color(NamedTextColor.RED));
        }

        User user = User.getInstance(args[0]);
        user.setMola(user.getMola() + Integer.parseInt(args[1]));
        sender.sendMessage(Component.text(String.format("%s に %s Mola を与えました", args[0], args[1])).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        ArrayList<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
