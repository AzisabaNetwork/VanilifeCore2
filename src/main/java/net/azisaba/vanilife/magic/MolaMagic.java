package net.azisaba.vanilife.magic;

import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MolaMagic implements Magic
{
    @Override
    public @NotNull List<String> getKeywords()
    {
        return List.of("もら", "モラ", "mola", "ぽいんと", "ポイント", "ください", "くれ", "わたせ", "渡せ", "ほしい", "欲しい");
    }

    @Override
    public void perform(@NotNull Player player)
    {
        User user = User.getInstance(player);
        user.setMola(user.getMola() + 4);

        player.sendMessage(Component.text("4 Mola を受け取った").color(NamedTextColor.LIGHT_PURPLE));
    }
}
