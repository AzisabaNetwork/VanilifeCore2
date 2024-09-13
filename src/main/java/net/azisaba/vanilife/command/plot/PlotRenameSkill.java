package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlotRenameSkill implements ICommandSkill
{
    @Override
    @NotNull
    public String getName()
    {
        return "rename";
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
            sender.sendMessage(Component.text("Correct syntax: /plot rename <name>").color(NamedTextColor.RED));
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

        plot.setName(args[0]);
        sender.sendMessage(Component.text(String.format("Plot名を %s に変更しました", args[0])).color(NamedTextColor.GREEN));
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
