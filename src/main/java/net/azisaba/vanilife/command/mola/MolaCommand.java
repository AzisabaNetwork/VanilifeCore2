package net.azisaba.vanilife.command.mola;

import net.azisaba.vanilife.command.skill.SkillCommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MolaCommand extends SkillCommand
{
    @Override
    public String getName()
    {
        return "mola";
    }

    @Override
    protected void registerSkills()
    {
        this.registerSkill(new MolaGetSkill());
        this.registerSkill(new MolaGiveSkill());
        this.registerSkill(new MolaSetSkill());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            if (! (sender instanceof Player player))
            {
                sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
                return true;
            }

            User user = User.getInstance(player);
            List<Plot> plots = Plot.getInstances().stream().filter(p -> p.getOwner() == user).toList();
            List<Chunk> chunks = new ArrayList<>();

            plots.forEach(p -> chunks.addAll(p.getChunks()));

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_AQUA));
            sender.sendMessage(Component.text(CLI.getSpaces(1) + "YOUR MOLA").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
            sender.sendMessage(Component.text());
            sender.sendMessage(Component.text(CLI.getSpaces(1) + user.getMola()).color(NamedTextColor.LIGHT_PURPLE).append(Component.text(" Mola").color(NamedTextColor.GRAY)));
            sender.sendMessage(Component.text());
            sender.sendMessage(Component.text(CLI.getSpaces(1) + "SUBSCRIPTIONS").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
            sender.sendMessage(Component.text("すべての Subscription は、毎月1日に自動的に引き落とされます"));
            sender.sendMessage(Component.text(CLI.getSpaces(2) + "Plot").color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text(CLI.getSpaces(3) + String.format("あなたは現在、 %s Chunk を所有しています:", chunks.size())));
            sender.sendMessage(Component.text(CLI.getSpaces(3) + (20 * chunks.size()) + " Mola ").color(NamedTextColor.GREEN).append(Component.text("(").color(NamedTextColor.GRAY).append(Component.text("➡").color(NamedTextColor.DARK_GRAY).append(Component.text(String.format("20 Mola × %s)", chunks.size())).color(NamedTextColor.GRAY)))));

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_AQUA));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
