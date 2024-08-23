package net.azisaba.vanilife.command.mola;

import net.azisaba.vanilife.command.AbstractSkillCommand;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MolaCommand extends AbstractSkillCommand
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

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
            sender.sendMessage(Component.text("あなたは現在 ").color(NamedTextColor.YELLOW).append(Component.text(String.format("%s Mola", user.getMola())).color(NamedTextColor.LIGHT_PURPLE)).append(Component.text(" を所持しています").color(NamedTextColor.YELLOW)));
            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
