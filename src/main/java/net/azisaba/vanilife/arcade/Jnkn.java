package net.azisaba.vanilife.arcade;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.JnknUI;
import net.azisaba.vanilife.ui.Language;
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

        this.broadcast("msg.jnkn.ready");
        this.broadcast(Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);

        Player winner = (hand1.weaker == hand2) ? player1 : player2;
        Player loser = (hand1.weaker == hand2) ? player2 : player1;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                broadcast("msg.jnkn.go");

                if (hand1 == hand2)
                {
                    broadcast((Player) null, (Player) null);
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

        this.broadcast("msg.jnkn.cancelled");
    }

    private void broadcast(Player winner, Player loser)
    {
        this.broadcast(Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f);

        broadcast(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
        this.player1.sendMessage(Language.translate("jnkn.title", this.player1).color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        this.player2.sendMessage(Language.translate("jnkn.title", this.player2).color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        broadcast(Component.text().build());

        if (winner == null || loser == null)
        {
            this.player1.sendMessage(Language.translate("msg.jnkn.result", this.player1).color(NamedTextColor.GRAY).append(Language.translate("jnkn.result.favour", this.player1).decorate(TextDecoration.BOLD)));
            this.player2.sendMessage(Language.translate("msg.jnkn.result", this.player2).color(NamedTextColor.GRAY).append(Language.translate("jnkn.result.favour", this.player2).decorate(TextDecoration.BOLD)));

            this.player1.sendMessage(Language.translate("msg.jnkn.me", this.player1).color(NamedTextColor.GRAY).append(Language.translate(this.hand1.name, this.player1).color(NamedTextColor.GREEN)));
            this.player1.sendMessage(Language.translate("msg.jnkn.partner", this.player1).color(NamedTextColor.GRAY).append(Language.translate(this.hand2.name, this.player2).color(NamedTextColor.GREEN)));
            this.player1.showTitle(Title.title(Language.translate("jnkn.result.favour", this.player1).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), Component.text().build()));

            this.player2.sendMessage(Language.translate("msg.jnkn.me", this.player2).color(NamedTextColor.GRAY).append(Language.translate(this.hand2.name, this.player2).color(NamedTextColor.GREEN)));
            this.player2.sendMessage(Language.translate("msg.jnkn.partner", this.player2).color(NamedTextColor.GRAY).append(Language.translate(this.hand1.name, this.player2).color(NamedTextColor.GREEN)));
            this.player2.showTitle(Title.title(Language.translate("jnkn.result.favour", this.player1).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), Component.text().build()));

            broadcast(Component.text(CLI.SEPARATOR).color(NamedTextColor.BLUE));
            return;
        }

        winner.sendMessage(Language.translate("msg.jnkn.result", winner).color(NamedTextColor.GRAY).append(Language.translate("jnkn.result.victory", winner).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
        winner.sendMessage(Language.translate("msg.jnkn.me", winner).color(NamedTextColor.GRAY).append(Language.translate(this.getHand(winner).name, winner).color(NamedTextColor.GREEN)));
        winner.sendMessage(Language.translate("msg.jnkn.partner", winner).color(NamedTextColor.GRAY).append(Language.translate(this.getHand(loser).name, winner).color(NamedTextColor.GREEN)));
        winner.showTitle(Title.title(Component.text("VICTORY!").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD), Component.text().build()));

        loser.sendMessage(Language.translate("msg.jnkn.result", loser).color(NamedTextColor.GRAY).append(Language.translate("jnkn.result.defeat", loser).color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)));
        loser.sendMessage(Language.translate("msg.jnkn.me", loser).color(NamedTextColor.GRAY).append(Language.translate(this.getHand(loser).name, loser).color(NamedTextColor.GREEN)));
        loser.sendMessage(Language.translate("msg.jnkn.partner", loser).color(NamedTextColor.GRAY).append(Language.translate(this.getHand(winner).name, loser).color(NamedTextColor.GREEN)));
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

    private void broadcast(@NotNull String key, String... args)
    {
        this.player1.sendMessage(Language.translate(key, this.player1, args));
        this.player2.sendMessage(Language.translate(key, this.player2, args));
    }

    private void broadcast(@NotNull Sound sound, float volume, float pitch)
    {
        this.player1.playSound(this.player1, sound, volume, pitch);
        this.player2.playSound(this.player2, sound, volume, pitch);
    }

    public enum Hand
    {
        G("jnkn.hand.rock"),
        C("jnkn.hand.scissors"),
        P("jnkn.hand.paper"),
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
