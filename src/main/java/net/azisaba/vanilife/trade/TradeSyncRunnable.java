package net.azisaba.vanilife.trade;

import net.azisaba.vanilife.ui.TradeUI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TradeSyncRunnable extends BukkitRunnable
{
    private final Trade trade;

    public TradeSyncRunnable(Trade trade)
    {
        this.trade = trade;
    }

    @Override
    public void run()
    {
        this.sync(this.trade.getUi1());
        this.sync(this.trade.getUi2());
    }

    private void sync(@NotNull TradeUI from)
    {
        List<ItemStack> stacks = new ArrayList<>();

        for (int i : Trade.CONTROL)
        {
            ItemStack stack = from.getInventory().getItem(i);

            if (stack != null)
            {
                stacks.add(stack);
            }
        }

        this.trade.setStacks(from.getPlayer(), stacks);
    }
}
