package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.TrustRank;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Afk;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.Watch;
import net.azisaba.vanilife.vc.VoiceChat;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerListRunnable extends BukkitRunnable
{
    private final String SEPARATOR = "------------------------------------------------";

    private final List<Component> animation = List.of(
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("G").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("IMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GI").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("MMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIM").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("MICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMM").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("ICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMI").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("CK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMIC").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("K & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(" & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("& WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK &").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(" WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & W").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("EB UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WE").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("B UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(" UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("UPDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB U").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("PDATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UP").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("DATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPD").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("ATE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDA").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("TE - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDAT").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("E - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(" - 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("- 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("- 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE -").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(" 2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("2.1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - 2").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(".1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - 2.").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("1.0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - 2.1").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text(".0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - 2.1.").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).append(Component.text("0").color(NamedTextColor.GREEN)),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD),
            Component.text("GIMMICK & WEB UPDATE - 2.1.0").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
            Component.text("H").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("ALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HA").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("LLOWEEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HAL").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("LOWEEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALL").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("OWEEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLO").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("WEEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOW").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("EEN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWE").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("EN 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEE").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("N 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text(" 2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN ").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("2024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN 2").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("024").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN 20").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("24").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN 202").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text("4").color(NamedTextColor.DARK_PURPLE)),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            Component.text("HALLOWEEN 2024").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD));

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

            if (player.getWorld().equals(VanilifeWorldManager.getUnderworld()))
            {
                icon = Component.text("🎃").color(NamedTextColor.GOLD);
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
