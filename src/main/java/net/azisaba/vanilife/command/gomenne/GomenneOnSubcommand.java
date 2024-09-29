package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.ui.Language;
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

public class GomenneOnSubcommand implements ISubcommand
{
    @Override
    public @NotNull String getName()
    {
        return "on";
    }

    @Override
    public @NotNull Sara getRequirement()
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
            sender.sendMessage(Component.text("Correct syntax: /" + label + " on").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.read("settings.ime").getAsBoolean())
        {
            player.sendMessage(Language.translate("cmd.gomenne.on.already", player).color(NamedTextColor.RED));
            return;
        }

        user.write("settings.ime", true);
        player.sendMessage(Language.translate("cmd.gomenne.on", player));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
