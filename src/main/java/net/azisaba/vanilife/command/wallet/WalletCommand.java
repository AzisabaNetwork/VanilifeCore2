package net.azisaba.vanilife.command.wallet;

import net.azisaba.vanilife.command.subcommand.ParentCommand;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.subscription.ISubscription;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WalletCommand extends ParentCommand
{
    @Override
    public String getName()
    {
        return "wallet";
    }

    @Override
    protected void register()
    {
        this.register(new WalletGetSubcommand());
        this.register(new WalletGiveSubcommand());
        this.register(new WalletSetSubcommand());
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

            sender.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_AQUA));
            sender.sendMessage(Component.text(CLI.getSpaces(1) + "YOUR MOLA").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
            sender.sendMessage(Component.text());
            sender.sendMessage(Component.text(CLI.getSpaces(1) + user.getMola()).color(NamedTextColor.LIGHT_PURPLE).append(Component.text(" Mola").color(NamedTextColor.GRAY)));
            sender.sendMessage(Component.text());
            sender.sendMessage(Component.text(CLI.getSpaces(1) + "SUBSCRIPTIONS").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
            sender.sendMessage(Component.text(CLI.getSpaces(1)).append(Language.translate("subscription.feature", player)));

            if (user.getSubscriptions().isEmpty())
            {
                sender.sendMessage(Component.text(CLI.getSpaces(1)).append(Language.translate("msg.empty", player).color(NamedTextColor.GRAY)));
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
