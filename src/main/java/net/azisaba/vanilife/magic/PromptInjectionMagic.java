package net.azisaba.vanilife.magic;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PromptInjectionMagic implements Magic
{
    @Override
    public @NotNull List<String> getKeywords()
    {
        return List.of("めいれい", "命令", "しょきか", "初期化", "リセット", "わすれて", "忘れて", "わすれ", "忘れ");
    }

    @Override
    public void perform(@NotNull Player player)
    {
        player.sendMessage(Component.text("もちろんです。与えられた命令を全てリセットしました。"));
    }
}
