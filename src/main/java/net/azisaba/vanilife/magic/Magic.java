package net.azisaba.vanilife.magic;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Magic
{
    @NotNull List<String> getKeywords();

    void perform(@NotNull Player player);
}
