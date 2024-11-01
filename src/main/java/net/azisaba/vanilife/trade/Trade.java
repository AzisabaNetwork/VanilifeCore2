package net.azisaba.vanilife.trade;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.ui.TradeUI;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trade
{
    public static final List<Integer> CONTROL = new ArrayList<>(List.of(9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48));
    public static final List<Integer> PREVIEW = new ArrayList<>(List.of(14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53));

    private final User user1;
    private final Player player1;
    private final TradeUI ui1;
    private final List<ItemStack> stacks1 = new ArrayList<>();
    private Agree agree1 = Agree.NONE;

    private final User user2;
    private final Player player2;
    private final TradeUI ui2;
    private final List<ItemStack> stacks2 = new ArrayList<>();
    private Agree agree2 = Agree.NONE;

    private final TradeSyncRunnable tradeSyncRunnable;
    private boolean cancelled = false;

    public Trade(Player player1, Player player2)
    {
        this.user1 = User.getInstance(player1);
        this.player1 = player1;

        this.user2 = User.getInstance(player2);
        this.player2 = player2;

        this.ui1 = new TradeUI(this.player1, this);
        this.ui2 = new TradeUI(this.player2, this);

        this.tradeSyncRunnable = new TradeSyncRunnable(this);
        this.tradeSyncRunnable.runTaskTimer(Vanilife.getPlugin(), 0L, 10L);
    }

    public User getUser1()
    {
        return this.user1;
    }

    public Player getPlayer1()
    {
        return this.player1;
    }

    public User getUser2()
    {
        return this.user2;
    }

    public Player getPlayer2()
    {
        return this.player2;
    }

    public TradeUI getUi1()
    {
        return this.ui1;
    }

    public TradeUI getUi2()
    {
        return this.ui2;
    }

    public boolean getAgree()
    {
        return this.agree1.level + this.agree2.level == 4;
    }

    public Agree getAgree(Player player)
    {
        return (this.player1 == player) ? this.agree1 : this.agree2;
    }

    public Agree getAgree1()
    {
        return this.agree1;
    }

    public Agree getAgree2()
    {
        return this.agree2;
    }

    public void setAgree(Player player, Agree agree)
    {
        ItemStack agreeStack = new ItemStack(agree.favicon);
        ItemMeta agreeMeta = agreeStack.getItemMeta();

        if (player == this.player1)
        {
            agreeMeta.displayName(Language.translate(agree.translate, this.player2).decoration(TextDecoration.ITALIC, false));
            agreeStack.setItemMeta(agreeMeta);

            this.agree1 = agree;
            this.ui2.getInventory().setItem(6, agreeStack);
        }

        if (player == this.player2)
        {
            agreeMeta.displayName(Language.translate(agree.translate, this.player1).decoration(TextDecoration.ITALIC, false));
            agreeStack.setItemMeta(agreeMeta);

            this.agree2 = agree;
            this.ui1.getInventory().setItem(6, agreeStack);
        }
    }

    public @NotNull List<ItemStack> getStacks1()
    {
        return this.stacks1;
    }

    public @NotNull List<ItemStack> getStacks2()
    {
        return this.stacks2;
    }

    public void setStacks(@NotNull Player player, @NotNull List<ItemStack> stacks)
    {
        if (this.getAgree(player) != Agree.NONE)
        {
            return;
        }

        TradeUI syncTo = null;

        if (player == this.player1)
        {
            this.stacks1.clear();
            this.stacks1.addAll(stacks);
            syncTo = this.ui2;
        }

        if (player == this.player2)
        {
            this.stacks2.clear();
            this.stacks2.addAll(stacks);
            syncTo = this.ui1;
        }

        for (int i : Trade.PREVIEW)
        {
            syncTo.getInventory().clear(i);
        }

        for (int i = 0; i < stacks.size(); i ++)
        {
            syncTo.getInventory().setItem(Trade.PREVIEW.get(i), stacks.get(i));
        }
    }

    private void giveItems(@NotNull Player player, @NotNull List<ItemStack> stacks)
    {
        Inventory inventory = player.getInventory();
        List<ItemStack> remainingStacks = new ArrayList<>();

        stacks.forEach(stack -> {
            if (stack != null && 0 < stack.getAmount())
            {
                ItemStack[] leftover = inventory.addItem(stack).values().toArray(new ItemStack[0]);
                remainingStacks.addAll(Arrays.asList(leftover));
            }
        });

        remainingStacks.forEach(stack -> {
            if (stack != null && 0 < stack.getAmount())
            {
                player.getWorld().dropItem(player.getLocation(), stack);
            }
        });
    }

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void broadcast(@NotNull String key, String... args)
    {
        this.player1.sendMessage(Language.translate(key, this.player1, args));
        this.player2.sendMessage(Language.translate(key, this.player2, args));
    }

    public void trade()
    {
        if (! this.getAgree())
        {
            return;
        }

        this.giveItems(this.player1, this.stacks2);
        this.giveItems(this.player2, this.stacks1);

        this.player1.closeInventory();
        this.player2.closeInventory();

        this.broadcast("trade.hold");

        StringBuilder sb1 = new StringBuilder();
        this.stacks1.forEach(s -> sb1.append(String.format("%s: × %s", s.getType().toString().toLowerCase(), s.getAmount())));

        StringBuilder sb2 = new StringBuilder();
        this.stacks2.forEach(s -> sb2.append(String.format("%s: × %s\n", s.getType().toString().toLowerCase(), s.getAmount())));

        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setTitle("1件の Trade が成立しました")
                .addField(String.format("%s (%s)", this.player1.getName(), this.player1.getUniqueId()), sb1.toString(), true)
                .addField(String.format("%s (%s)", this.player2.getName(), this.player2.getUniqueId()), sb2.toString(), true)
                .setFooter("(*'▽')")
                .setColor(new Color(85, 255, 85)).build()).queue();
    }

    public void cancel()
    {
        if (this.cancelled)
        {
            return;
        }

        this.cancelled = true;

        this.player1.closeInventory();
        this.player2.closeInventory();

        this.stacks1.forEach(s -> this.player1.getInventory().addItem(s));
        this.stacks2.forEach(s -> this.player2.getInventory().addItem(s));

        this.tradeSyncRunnable.cancel();

        this.broadcast("trade.cancelled");
    }

    public enum Agree
    {
        NONE(0, Material.RED_DYE, "trade.reject"),
        CHECK(1, Material.YELLOW_DYE, "trade.check"),
        SET(2, Material.LIME_DYE, "trade.set");

        public final int level;
        public final Material favicon;
        public final String translate;

        Agree(int level, Material favicon, String translate)
        {
            this.level = level;
            this.favicon = favicon;
            this.translate = translate;
        }
    }
}
