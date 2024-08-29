package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotClaimSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "claim";
    }

    @Override
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

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot claim").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.getMola() < Vanilife.MOLA_PLOT_CLAIM)
        {
            sender.sendMessage(Component.text(String.format("Plot の拡張には %s Mola が必要です", Vanilife.MOLA_PLOT_CLAIM)).color(NamedTextColor.RED));
            return;
        }

        Chunk chunk = player.getChunk();
        int chunkX = chunk.getX() * 16 + 8;
        int chunkZ = chunk.getZ() * 16 + 8;

        if (Math.sqrt(chunkX * chunkX + chunkZ * chunkZ) <= 100)
        {
            sender.sendMessage(Component.text("スポーン地点から 100m 以内のチャンクを取得することはできません").color(NamedTextColor.RED));
            return;
        }

        if (Plot.getInstance(chunk) != null)
        {
            sender.sendMessage(Component.text("このチャンクは既に Plot の範囲内です").color(NamedTextColor.RED));
            return;
        }

        Plot plot = null;
        int i = 0;

        for (int x = -1; x <= 1; x ++)
        {
            for (int z = -1; z <= 1; z ++)
            {
                Plot plot2 = Plot.getInstance(chunk.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z));

                if (plot2 != null)
                {
                   plot = (plot == null) ? plot2 : plot;

                   if (plot != plot2)
                   {
                       i ++;
                   }
                }
            }
        }

        if (plot == null)
        {
            sender.sendMessage(Component.text("このチャンクに隣接する Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        if (User.getInstance(player) != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        if (1 < i)
        {
            sender.sendMessage(Component.text("他の Plot と隣接するチャンクを取得することはできません").color(NamedTextColor.RED));
            return;
        }

        plot.claim(chunk);
        user.setMola(user.getMola() - Vanilife.MOLA_PLOT_CLAIM);
        sender.sendMessage(Component.text(String.format("%s Mola を消費してこのチャンクを %s に取得しました！", Vanilife.MOLA_PLOT_CLAIM, plot.getName())).color(NamedTextColor.GREEN));
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
