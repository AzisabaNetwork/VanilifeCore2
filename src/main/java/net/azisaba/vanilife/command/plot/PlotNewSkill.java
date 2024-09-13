package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.PlotSubscription;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotNewSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "new";
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

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot new <name>").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.getMola() < Vanilife.MOLA_PLOT_NEW)
        {
            sender.sendMessage(Component.text(String.format("Mola が足りません！あと %s Mola 必要です！", Vanilife.MOLA_PLOT_NEW - user.getMola())).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (Plot.getInstance(args[0]) != null)
        {
            sender.sendMessage(Component.text(String.format("Plot名 %s は既に使用されています", args[0])).color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        if (16 < args[0].length())
        {
            sender.sendMessage(Component.text("Plot名は16文字以内で設定してください").color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        Chunk chunk = player.getChunk();
        int chunkX = chunk.getX() * 16 + 8;
        int chunkZ = chunk.getZ() * 16 + 8;

        if (Math.sqrt(chunkX * chunkX + chunkZ * chunkZ) <= 100)
        {
            sender.sendMessage(Component.text("スポーン地点から 100m 以内のチャンクを取得することはできません").color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        Plot plot = Plot.getInstance(chunk);

        if (plot != null)
        {
            sender.sendMessage(Component.text("このチャンクは既に別の Plot で取得されています").color(NamedTextColor.RED));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.1f);
            return;
        }

        plot = new Plot(args[0], player.getChunk());
        plot.setOwner(user);
        user.subscribe(new PlotSubscription(plot));
        user.setMola(user.getMola() - Vanilife.MOLA_PLOT_NEW);
        sender.sendMessage(Component.text(String.format("%s Mpla を消費して %s として Plot を新しく作成しました", Vanilife.MOLA_PLOT_NEW, args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
