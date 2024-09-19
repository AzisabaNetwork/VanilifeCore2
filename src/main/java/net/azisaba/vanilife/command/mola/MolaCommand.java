package net.azisaba.vanilife.command.mola;

import net.azisaba.vanilife.command.skill.SkillCommand;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
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
            sender.sendMessage(Component.text(CLI.getSpaces(1)).append(Language.translate("subscription.feature", player)));

            if (user.getSubscriptions().isEmpty())
            {
                sender.sendMessage(Component.text(CLI.getSpaces(1) + "ここには何もありません！").color(NamedTextColor.GRAY));
            }

            for (ISubscription subscription : user.getSubscriptions())
            {
                sender.sendMessage(Component.text(CLI.getSpaces(2)).append(subscription.getDisplayName(user.getSettings().LANGUAGE.getLanguage()).color(NamedTextColor.YELLOW)));

                for (Component row : subscription.getDetails(user.getSettings().LANGUAGE.getLanguage()))
                {
                    sender.sendMessage(Component.text(CLI.getSpaces(3)).append(row));
                }
            }

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_AQUA));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
