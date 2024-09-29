package net.azisaba.vanilife.command.gomenne;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.gomenne.ConvertRequest;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.StringUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GomenneRequestSubcommand implements ISubcommand
{
    @Override
    public @NotNull String getName()
    {
        return "request";
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

        if (args.length != 2)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " request <yomi> <kaki>").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user.getStatus() != UserStatus.DEFAULT)
        {
            sender.sendMessage(Language.translate("msg.muted", player).color(NamedTextColor.RED));
            return;
        }

        if (! StringUtility.isHiragana(args[0]))
        {
            sender.sendMessage(Language.translate("cmd.gomenne.request.cant", player).color(NamedTextColor.RED));
            return;
        }

        if (16 < args[0].length() || 16 < args[1].length())
        {
            sender.sendMessage(Language.translate("cmd.gomenne.request.limit-over", player).color(NamedTextColor.RED));
            return;
        }

        new ConvertRequest(user, args[0], args[1]);
        sender.sendMessage(Language.translate("cmd.gomenne.request.requested", player).color(NamedTextColor.GREEN));
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
