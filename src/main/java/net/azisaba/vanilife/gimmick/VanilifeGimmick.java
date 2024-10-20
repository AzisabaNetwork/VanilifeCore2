package net.azisaba.vanilife.gimmick;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public abstract class VanilifeGimmick extends Gimmick
{
    public VanilifeGimmick(@NotNull Block block)
    {
        super(block);
        this.write("type", this.getType());
    }
}
