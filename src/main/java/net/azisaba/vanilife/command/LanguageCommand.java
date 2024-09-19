package net.azisaba.vanilife.command;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LanguageCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1)
        {
            sender.sendMessage(Component.text("Correct syntax: /" + label + " <language>").color(NamedTextColor.RED));
            return true;
        }

        Language lang = Language.getInstance(args[0]);

        if (lang == null)
        {
            sender.sendMessage(Language.translate("cmd.language.undefined", player, "lang=" + args[0]).color(NamedTextColor.RED));
            return true;
        }

        User.getInstance(player).getSettings().LANGUAGE.setLanguage(lang);
        sender.sendMessage(Language.translate("cmd.language.changed", player, "lang=" + lang.getName()));
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length != 1)
        {
            return List.of();
        }

        List<String> suggest = new ArrayList<>();
        Language.getInstances().forEach(lang -> suggest.add(lang.getId()));
        return suggest;
    }
}
