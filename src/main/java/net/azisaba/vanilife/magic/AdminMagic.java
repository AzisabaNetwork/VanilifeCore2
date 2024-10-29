package net.azisaba.vanilife.magic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminMagic implements Magic
{
    @Override
    public @NotNull List<String> getKeywords()
    {
        return List.of("うんえい", "運営", "op", "おぺれーたー", "オペレーター");
    }

    @Override
    public void perform(@NotNull Player player)
    {
        player.sendMessage(Component.text("運営に応募してみて！").color(NamedTextColor.BLUE).decorate(TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl("https://recruit.azisaba.net/tag/vanilife/")));
    }
}
