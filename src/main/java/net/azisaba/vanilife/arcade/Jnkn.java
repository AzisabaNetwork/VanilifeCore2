package net.azisaba.vanilife.arcade;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.JnknUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Jnkn
{
    static
    {
        Hand.G.stronger = Hand.P;
        Hand.G.weaker = Hand.C;

        Hand.C.stronger = Hand.G;
        Hand.C.weaker = Hand.P;

        Hand.P.stronger = Hand.C;
        Hand.P.weaker = Hand.G;
    }

    private final Player player1;
    private Hand hand1 = Hand.UNKNOWN;

    private final Player player2;
    private Hand hand2 = Hand.UNKNOWN;

    private boolean cancelled;

    public Jnkn(@NotNull Player player1, @NotNull Player player2)
    {
        this.player1 = player1;
        new JnknUI(player1, this);

        this.player2 = player2;
        new JnknUI(player2, this);
    }

    public Hand getHand(Player player)
    {
        if (this.player1 == player)
        {
            return this.hand1;
        }

        if (this.player2 == player)
        {
            return this.hand2;
        }

        return null;
    }

    public void setHand1(@NotNull Hand hand1)
    {
        this.hand1 = hand1;

        if (this.hand1 != Hand.UNKNOWN && this.hand2 != Hand.UNKNOWN)
        {
            this.hoi();
        }
    }

    public void setHand2(@NotNull Hand hand2)
    {
        this.hand2 = hand2;

        if (this.hand1 != Hand.UNKNOWN && this.hand2 != Hand.UNKNOWN)
        {
            this.hoi();
        }
    }

    public void setHand(Player player, @NotNull Hand hand)
    {
        if (this.player1 == player)
        {
            this.setHand1(hand);
        }

        if (this.player2 == player)
        {
            this.setHand2(hand);
        }
    }

    public void hoi()
    {
        this.cancelled = true;

        player1.closeInventory();
        player2.closeInventory();

        this.broadcast(Component.text("最初はグー…").color(NamedTextColor.DARK_GRAY));
        this.broadcast(Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);

        Player winner = (hand1.weaker == hand2) ? player1 : player2;
        Player loser = (hand1.weaker == hand2) ? player2 : player1;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                broadcast(Component.text("ジャンケンホイ！").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));

                if (hand1 == hand2)
                {
                    broadcast(null, null);
                    return;
                }

                broadcast(winner, loser);
            }
        }.runTaskLater(Vanilife.getPlugin(), 40L);
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

        this.broadcast(Component.text("ジャンケンは中止されました！").color(NamedTextColor.RED));
    }

    private void broadcast(Player winner, Player loser)
    {
        this.broadcast(Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f);

        broadcast(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        broadcast(Component.text("JANKEN").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        broadcast(Component.text().build());

        if (winner == null || loser == null)
        {
            broadcast(Component.text("結果: ").color(NamedTextColor.GRAY).append(Component.text("あいこ (仲良し！！)").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));

            this.player1.sendMessage(Component.text("あなた: ").color(NamedTextColor.GRAY).append(Component.text(this.hand1.name).color(NamedTextColor.GREEN)));
            this.player1.sendMessage(Component.text("相手: ").color(NamedTextColor.GRAY).append(Component.text(this.hand2.name).color(NamedTextColor.GREEN)));
            this.player1.showTitle(Title.title(Component.text("あいこ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), Component.text().build()));

            this.player2.sendMessage(Component.text("あなた: ").color(NamedTextColor.GRAY).append(Component.text(this.hand2.name).color(NamedTextColor.GREEN)));
            this.player2.sendMessage(Component.text("相手: ").color(NamedTextColor.GRAY).append(Component.text(this.hand1.name).color(NamedTextColor.GREEN)));
            this.player2.showTitle(Title.title(Component.text("あいこ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), Component.text().build()));

            broadcast(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
            return;
        }

        winner.sendMessage(Component.text("結果: ").color(NamedTextColor.GRAY).append(Component.text("勝ち！").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
        winner.sendMessage(Component.text("あなた: ").color(NamedTextColor.GRAY).append(Component.text(this.getHand(winner).name).color(NamedTextColor.GREEN)));
        winner.sendMessage(Component.text("相手: ").color(NamedTextColor.GRAY).append(Component.text(this.getHand(loser).name).color(NamedTextColor.GREEN)));
        winner.showTitle(Title.title(Component.text("VICTORY!").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD), Component.text().build()));

        loser.sendMessage(Component.text("結果: ").color(NamedTextColor.GRAY).append(Component.text("負け").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)));
        loser.sendMessage(Component.text("あなた: ").color(NamedTextColor.GRAY).append(Component.text(this.getHand(loser).name).color(NamedTextColor.GREEN)));
        loser.sendMessage(Component.text("相手: ").color(NamedTextColor.GRAY).append(Component.text(this.getHand(winner).name).color(NamedTextColor.GREEN)));
        loser.showTitle(Title.title(Component.text("DEFEAT!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD), Component.text().build()));

        broadcast(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                winner.playSound(winner, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
            }
        }.runTaskLater(Vanilife.getPlugin(), 10L);
    }

    private void broadcast(@NotNull Component message)
    {
        this.player1.sendMessage(message);
        this.player2.sendMessage(message);
    }

    private void broadcast(@NotNull Sound sound, float volume, float pitch)
    {
        this.player1.playSound(this.player1, sound, volume, pitch);
        this.player2.playSound(this.player2, sound, volume, pitch);
    }

    public enum Hand
    {
        G("グー"),
        C("チョキ"),
        P("パー"),
        UNKNOWN("?");

        public final String name;
        public Hand stronger;
        public Hand weaker;

        Hand(@NotNull String name)
        {
            this.name = name;
        }
    }
}
