package net.azisaba.vanilife.command.mola;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MolaGetSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "get";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /mola get <player>").color(NamedTextColor.RED));
            return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
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

                User user = User.getInstance(args[0]);
                sender.sendMessage(Component.text(String.format("%s は %s Mola を所持しています", args[0], user.getMola())).color(NamedTextColor.GREEN));
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> suggest = new ArrayList<>();

        if (args.length == 1)
        {
            Bukkit.getOnlinePlayers().forEach(p -> suggest.add(p.getName()));
        }

        return suggest;
    }
}
