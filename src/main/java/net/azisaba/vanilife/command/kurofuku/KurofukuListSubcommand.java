package net.azisaba.vanilife.command.kurofuku;

import net.azisaba.vanilife.command.subcommand.ISubcommand;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KurofukuListSubcommand implements ISubcommand
{
    @Override
    public @NotNull String getName()
    {
        return "list";
    }

    @Override
    public @NotNull Sara getRequirement()
    {
        return Sara.MOD;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + command.getName() + " list").color(NamedTextColor.RED));
            return;
        }

        List<User> kurofukus = User.getInstances().stream().filter(User::isKurofuku).toList();

        sender.sendMessage(Component.text("黒服 (" + kurofukus.size() + "):").color((! kurofukus.isEmpty()) ? NamedTextColor.WHITE : NamedTextColor.RED));

        for (User kurofuku : kurofukus)
        {
            sender.sendMessage(Component.text("- ").color(NamedTextColor.DARK_GRAY)
                    .append(((sender instanceof Player player) ? kurofuku.getName(player) : kurofuku.getName()).color(NamedTextColor.GREEN)));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return List.of();
    }
}
