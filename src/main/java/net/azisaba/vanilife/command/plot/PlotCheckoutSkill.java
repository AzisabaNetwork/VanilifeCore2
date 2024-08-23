package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.mail.Mail;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class PlotCheckoutSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "checkout";
    }

    @Override
    public boolean isOpCommand()
    {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot checkout").color(NamedTextColor.RED));
            return;
        }

        for (Plot plot : new ArrayList<>(Plot.getInstances()))
        {
            User owner = plot.getOwner();

            int cost = plot.getChunks().size() * 20;

            if (owner.getMola() < cost)
            {
                plot.delete();
                new Mail(User.getInstance("azisaba"), owner, String.format("Plot %s を維持できません", plot.getName()), String.format("あなたが所有していた %s の維持費は Mola が不足していたために、引き落としされませんでした。\nつきましては %s が削除されましたので、通知させていただきました。\n\n請求額: %s Mola\n引き落とし時残高: %s Mola\n不足額: %s Mola\n\n以上、ご確認よろしくお願いします。", plot.getName(), plot.getName(), cost, owner.getMola(), Math.abs(cost - owner.getMola())));
                continue;
            }

            owner.setMola(owner.getMola() - cost);
            new Mail(User.getInstance("azisaba"), owner, String.format("[領収書] %s 維持費", plot.getName()), String.format("%s の維持費として下記生に領収いたしました。\nPlot 維持費: %s Mola (20 × %s チャンク)", plot.getName(), cost, plot.getChunks().size()));
        }
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
