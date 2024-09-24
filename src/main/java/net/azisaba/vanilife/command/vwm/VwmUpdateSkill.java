package net.azisaba.vanilife.command.vwm;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VwmUpdateSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "update";
    }

    @Override
    @NotNull
    public Sara getRequirement()
    {
        return Sara.ADMIN;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /vwm update").color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text("vw のアップデートを実行しています…").color(NamedTextColor.GREEN));

        Calendar now = Calendar.getInstance();
        String basename = String.format("%s-%s", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        String name = basename;

        int i = 0;

        while (VanilifeWorld.getInstance(name) != null)
        {
            if (56 < i)
            {
                name = Vanilife.sdf4.format(new Date());
                break;
            }

            name = String.format(basename + "." + (++ i));
        }

        VanilifeWorldManager.running = true;
        Bukkit.getOnlinePlayers().forEach(p -> p.kick(Component.text("アップデートを実行しています…\n完了までしばらくお待ちください").color(NamedTextColor.RED)));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("vanilife:vwm create %s", name));

        VanilifeWorld oldWorld = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion() - 2);

        if (oldWorld != null)
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("vanilife:vwm delete %s", oldWorld.getName()));
        }

        sender.sendMessage(Component.text("アップデートを完了しました").color(NamedTextColor.GREEN));
        VanilifeWorldManager.running = false;
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
