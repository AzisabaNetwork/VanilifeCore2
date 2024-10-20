package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.Watch;
import net.azisaba.vanilife.vc.VoiceChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerListRunnable extends BukkitRunnable
{
    private final String SEPARATOR = "------------------------------------------------";

    private final List<Component> animation = List.of(Component.text("_"),
            Component.text("A_"),
            Component.text("Az_"),
            Component.text("Azi_"),
            Component.text("Azis_"),
            Component.text("Azisa_"),
            Component.text("Azisab_"),
            Component.text("Azisaba_"),
            Component.text("Azisaba _"),
            Component.text("Azisaba N_"),
            Component.text("Azisaba Ne_"),
            Component.text("Azisaba Net_"),
            Component.text("Azisaba Netw_"),
            Component.text("Azisaba Netwo_"),
            Component.text("Azisaba Networ_"),
            Component.text("Azisaba Network"),
            Component.text("Azisaba Network").color(NamedTextColor.BLACK),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_BLUE),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_GREEN),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_AQUA),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_RED),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_PURPLE),
            Component.text("Azisaba Network").color(NamedTextColor.GOLD),
            Component.text("Azisaba Network").color(NamedTextColor.GRAY),
            Component.text("Azisaba Network").color(NamedTextColor.DARK_GRAY),
            Component.text("Azisaba Network").color(NamedTextColor.BLUE),
            Component.text("Azisaba Network").color(NamedTextColor.GREEN),
            Component.text("Azisaba Network").color(NamedTextColor.AQUA),
            Component.text("Azisaba Network").color(NamedTextColor.RED),
            Component.text("Azisaba Network").color(NamedTextColor.LIGHT_PURPLE),
            Component.text("Azisaba Network").color(NamedTextColor.YELLOW),
            Component.text("Azisaba Network"),
            Component.text("Azisaba_Network"),
            Component.text("Azisab___etwork"),
            Component.text("Azisa_____twork"),
            Component.text("Azis_______work"),
            Component.text("Azi_________ork"),
            Component.text("Az___________rk"),
            Component.text("A_____________k"),
            Component.text("_______________"),
            Component.text("_____________"),
            Component.text("___________"),
            Component.text("_________"),
            Component.text("_______"),
            Component.text("_____"),
            Component.text("___"),
            Component.text("_"),
            Component.text("ｂ").decorate(TextDecoration.UNDERLINED),
            Component.text("ば"),
            Component.text("ば").append(Component.text("ｎ").decorate(TextDecoration.UNDERLINED)),
            Component.text("ばに"),
            Component.text("ばに").append(Component.text("ｒ").decorate(TextDecoration.UNDERLINED)),
            Component.text("ばにら"),
            Component.text("ばにら").append(Component.text("い").decorate(TextDecoration.UNDERLINED)),
            Component.text("ばにらい"),
            Component.text("ばにらい").append(Component.text("ｆ").decorate(TextDecoration.UNDERLINED)),
            Component.text("ばにらいふ"),
            Component.text("ばにらいふ２"),
            Component.text("ばにらいふ２！"),
            Component.text("ばにらいふ２！").color(NamedTextColor.BLACK),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_BLUE),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_GREEN),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_AQUA),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_RED),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_PURPLE),
            Component.text("ばにらいふ２！").color(NamedTextColor.GOLD),
            Component.text("ばにらいふ２！").color(NamedTextColor.GRAY),
            Component.text("ばにらいふ２！").color(NamedTextColor.DARK_GRAY),
            Component.text("ばにらいふ２！").color(NamedTextColor.BLUE),
            Component.text("ばにらいふ２！").color(NamedTextColor.GREEN),
            Component.text("ばにらいふ２！").color(NamedTextColor.AQUA),
            Component.text("ばにらいふ２！").color(NamedTextColor.RED),
            Component.text("ばにらいふ２！").color(NamedTextColor.LIGHT_PURPLE),
            Component.text("ばにらいふ２！").color(NamedTextColor.YELLOW),
            Component.text("ばにらいふ２_"),
            Component.text("ばにらいふ_"),
            Component.text("ばにらい_"),
            Component.text("ばにら_"),
            Component.text("ばに_"),
            Component.text("ば_"),
            Component.text("_"));

    private int phase = 0;

    @Override
    public void run()
    {
        this.phase = this.phase + 1 < this.animation.size() ? this.phase + 1 : 0;

        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = User.getInstance(player);

            int ping = player.getPing();
            double tps = Math.round(Bukkit.getServer().getTPS()[0] * 100.0) / 100.0;

            player.sendPlayerListHeader(Language.translate("ui.tab.title", player, "name=" + ComponentUtility.asString(Component.text(user.getPlaneName()).color(user.getSara().getColor()))).appendNewline()
                    .append(Component.text(this.SEPARATOR).color(NamedTextColor.DARK_GRAY)).appendNewline()
                    .append(Component.text("PING: ").color(NamedTextColor.GRAY).append(Component.text(ping).color(NamedTextColor.GREEN)).append(Component.text(" ms").color(NamedTextColor.GRAY))).appendNewline()
                    .append(Component.text("TPS: ").color(NamedTextColor.GRAY).append(Component.text(tps).color(switch (Math.min((int) Math.round(tps), 20))
                    {
                        case 20 -> NamedTextColor.GREEN;
                        case 19 -> NamedTextColor.AQUA;
                        case 18 -> NamedTextColor.YELLOW;
                        default -> NamedTextColor.RED;
                    }))).append(Component.text(" / 20.00").color(NamedTextColor.GRAY)).appendNewline()
                    .append(Language.translate("ui.tab.online", player, "online=" + Bukkit.getOnlinePlayers().stream().filter(p -> ! Watch.isWatcher(p)).toList().size())).appendNewline()
                    .append(Language.translate("ui.tab.wallet", player, "mola=" + user.getMola())).appendNewline()
                    .append(Language.translate("ui.tab.location", player).color(NamedTextColor.GRAY).append(Component.text("vanilife2").color(NamedTextColor.WHITE))).appendNewline()
                    .append(Component.text(this.SEPARATOR).color(NamedTextColor.DARK_GRAY)));

            player.sendPlayerListFooter(this.animation.get(this.phase));

            Component name = user.getName().appendSpace().append(Component.text("[" + user.getTrustRank().getName() + "]").color(user.getTrustRank() != TrustRank.TRUSTED ? NamedTextColor.GRAY : NamedTextColor.GOLD));
            Component icon = null;

            if (user.inHousing() && user.read("settings.housing.activity").getAsBoolean())
            {
                icon = Component.text("🌏").color(NamedTextColor.AQUA);
            }

            if (Afk.isAfk(player))
            {
                icon = Component.text("🌙").color(NamedTextColor.GOLD);
            }

            if (VoiceChat.getInstance(user) != null)
            {
                icon = Component.text(" 🔊").color(NamedTextColor.WHITE);
            }

            if (icon != null)
            {
                name = name.appendSpace().append(icon);
            }

            player.playerListName(name);
        });
    }
}
