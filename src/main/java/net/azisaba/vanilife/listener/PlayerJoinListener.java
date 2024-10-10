package net.azisaba.vanilife.listener;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.gomenne.ConvertRequest;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.ui.CLI;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.Skin;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.MojangAPI;
import net.azisaba.vanilife.util.PlayerUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.*;

public class PlayerJoinListener implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void setName(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        User user = User.getInstance(player);
        Sara sara = UserUtility.calculateSara(player);

        if (user.getSara().level < sara.level)
        {
            user.setSara(sara);
        }

        player.displayName(user.getName());
        player.playerListName(Component.text("@{Azisaba.Vanilife.User." + player.getUniqueId() + "}"));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void setLanguage(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore())
        {
            return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                User user = User.getInstance(player);

                Language lang = switch (PlayerUtility.getCountry(player))
                {
                    case "JP" -> Language.getInstance("ja-jp");
                    default -> Language.getInstance("en-us");
                };

                user.getSettings().LANGUAGE.setLanguage(lang);
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());
    }

    public static final Map<Player, ProfileProperty> texturesMap = new HashMap<>();

    @EventHandler
    public void setSkin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        PlayerProfile profile = player.getPlayerProfile();
        ProfileProperty textures = profile.getProperties().stream().filter(property -> property.getName().equals("textures")).toList().getFirst();
        PlayerJoinListener.texturesMap.put(player, new ProfileProperty(textures.getName(), textures.getValue(), textures.getSignature()));

        User user = User.getInstance(player);
        Skin skin = user.getSkin();

        if (skin == null && Skin.getInstances().stream().noneMatch(s -> MojangAPI.getSkin(s.getTexture()).equals(MojangAPI.getSkin(textures.getValue()))))
        {
            String name = player.getName();

            if (8 < name.length())
            {
                name = name.substring(0, 8);
            }

            skin = new Skin(name, user, textures.getValue(), textures.getSignature());
            user.addSkin(skin);
        }

        if (skin != null)
        {
            skin.use(player);
        }
    }

    @EventHandler
    public void setOnlinePlayers(PlayerJoinEvent event)
    {
        Vanilife.jda.getPresence().setActivity(Activity.customStatus(Bukkit.getOnlinePlayers().size() + " 人がばにらいふ！ をプレイ中！"));
    }

    @EventHandler
    public void giveLoginBonus(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();

        if (user.getLastLogin() != null)
        {
            last.setTime(user.getLastLogin());
        }

        if (now == last || ! (now.get(Calendar.YEAR) == last.get(Calendar.YEAR) && now.get(Calendar.MONTH) == last.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) == last.get(Calendar.DAY_OF_MONTH)))
        {
            user.setLoginStreak((now.get(Calendar.DAY_OF_MONTH) == last.get(Calendar.DAY_OF_MONTH) + 1) ? user.getLoginStreak() + 1 : 0);

            int streak = user.getLoginStreak() + 1;
            int bonus = 10 * Math.min(streak, 10);

            user.setMola(user.getMola() + bonus);

            player.sendMessage(Language.translate("msg.login-bonus", player, "mola=" + bonus, "streak=" + streak));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        user.setLastLogin(new Date());
    }

    @EventHandler
    public void giveStarterKit(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore())
        {
            return;
        }

        ItemStack bread = new ItemStack(Material.BREAD);
        bread.setAmount(16);
        PlayerUtility.giveItemStack(player, bread);
    }

    @EventHandler
    public void sendJoinMessage(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        event.joinMessage(null);

        if (UserStatus.MUTED.level() <= user.getStatus().level())
        {
            return;
        }

        if (Sara.$1000YEN.level <= user.getSara().level && user.getSara().isRank())
        {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Component.text(" >").color(NamedTextColor.AQUA)
                    .append(Component.text(">").color(NamedTextColor.RED))
                    .append(Component.text("> ").color(NamedTextColor.GREEN))
                    .append(Component.text().append(user.getName(p)).appendSpace().append(Language.translate("msg.join", p).color(NamedTextColor.GOLD)))
                    .append(Component.text(" <").color(NamedTextColor.GREEN))
                    .append(Component.text("<").color(NamedTextColor.RED))
                    .append(Component.text("<").color(NamedTextColor.AQUA))));
        }
        else if (player.hasPlayedBefore())
        {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Component.text("+ ").color(NamedTextColor.GREEN).append(user.getName(p)).appendSpace().append(Language.translate("msg.join", p).color(NamedTextColor.GRAY))));
        }
        else
        {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Component.text("+ ").color(NamedTextColor.LIGHT_PURPLE).append(user.getName(p)).appendSpace().append(Language.translate("msg.join-first", p).color(NamedTextColor.GRAY))));

            player.sendMessage(Component.text().build());
            player.sendMessage(Component.text("Wiki: ").color(NamedTextColor.GRAY)
                    .append(Component.text("https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ")
                            .color(NamedTextColor.BLUE)
                            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
                            .clickEvent(ClickEvent.openUrl("https://wiki.azisaba.net/wiki/ばにらいふ２！:メインページ"))));
            player.sendMessage(Component.text().build());

            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        int unread = user.getMails().stream().filter(m -> ! m.isRead()).toList().size();

        if (0 < unread)
        {
            player.sendMessage(Language.translate("msg.new-mail", player, "unread=" + unread)
                    .color(NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Language.translate("msg.click-to-run", player, "command=/mail")))
                    .clickEvent(ClickEvent.runCommand("/mail")));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }
    }

    @EventHandler
    public void sendJoinHistory(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        Vanilife.CHANNEL_HISTORY.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(player.getName() + " (" + player.getUniqueId() + ")", null, String.format("https://api.mineatar.io/face/%s", player.getUniqueId().toString().replace("-", "")))
                .setDescription(player.getName() + " が参加しました")
                .setColor(Color.GREEN).build()).queue();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void sendUpdate(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        final String version = Vanilife.getPluginVersion();

        if (user.getStorage().has("version"))
        {
            String userVersion = user.read("version").getAsString();

            if (! userVersion.equals(version))
            {
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

                player.sendMessage(Component.text().build());
                player.sendMessage(Component.text(version.toUpperCase() + " RELEASED!").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
                player.sendMessage(Component.text().build());
            }
        }

        user.write("version", version);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void sendImeRequests(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (! UserUtility.isModerator(player))
        {
            return;
        }

        int unconfirmed = ConvertRequest.getInstances().size();

        if (unconfirmed < 1)
        {
            return;
        }

        player.sendMessage(Component.text("通知: 未処理の変換リクエストが " + unconfirmed + " 件届いています！").color(NamedTextColor.GREEN)
                .append(Component.text("こちらをクリックして確認").color(NamedTextColor.GOLD).hoverEvent(HoverEvent.showText(Component.text("クリックして /gomenne requests を実行"))).clickEvent(ClickEvent.runCommand("/vanilife:gomenne requests"))));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void sendReview(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (! UserUtility.isModerator(player))
        {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            int review = 0;
            int materials = 0;

            int one = 0;
            int two = 0;
            int three = 0;
            int four = 0;
            int five = 0;

            for (User user : User.getInstances())
            {
                if (user.read("review.score") == null || user.read("review.score").getAsInt() < 0)
                {
                    continue;
                }

                int score = user.read("review.score").getAsInt();

                switch (score)
                {
                    case 1:
                        one += 1;
                        break;
                    case 2:
                        two += 1;
                        break;
                    case 3:
                        three += 1;
                        break;
                    case 4:
                        four += 1;
                        break;
                    case 5:
                        five += 1;
                        break;
                }

                review += score;
                materials ++;
            }

            double oneRate = 0;
            double twoRate = 0;
            double threeRate = 0;
            double fourRate = 0;
            double fiveRate = 0;

            if (0 < materials)
            {
                review = review / materials;

                oneRate = 0 < one ? ((double) materials / one) * 100 : 0;
                twoRate = 0 < two ? ((double) materials / two) * 100 : 0;
                threeRate = 0 < three ? ((double) materials / three) * 100 : 0;
                fourRate = 0 < four ? ((double) materials / four) * 100 : 0;
                fiveRate = 0 < five ? ((double) materials / five) * 100 : 0;
            }

            TextComponent.Builder stars = Component.text();

            for (int i = 1; i <= 5; i ++)
            {
                stars.append(Component.text("★").color(i <= review ? NamedTextColor.YELLOW : NamedTextColor.DARK_GRAY));
            }

            stars.append(Component.text(" " + review));

            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_GRAY));
            player.sendMessage(Component.text(CLI.getSpaces(12)).append(Component.text("ユーザー評価")));
            player.sendMessage(stars.build());
            player.sendMessage(Component.text().build());
            player.sendMessage(Component.text("★1: ").color(NamedTextColor.YELLOW).append(Component.text(oneRate + "% (" + one + "件)").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("★2: ").color(NamedTextColor.YELLOW).append(Component.text(twoRate + "% (" + two + "件)").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("★3: ").color(NamedTextColor.YELLOW).append(Component.text(threeRate + "% (" + three + "件)").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("★4: ").color(NamedTextColor.YELLOW).append(Component.text(fourRate + "% (" + four + "件)").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("★5: ").color(NamedTextColor.YELLOW).append(Component.text(fiveRate + "% (" + five + "件)").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text(CLI.SEPARATOR).color(NamedTextColor.DARK_GRAY));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkVwm(PlayerLoginEvent event)
    {
        if (Vanilife.publisher)
        {
            return;
        }

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.kickMessage(Component.text("Server does not allow connections…").color(NamedTextColor.RED));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkStatus(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        if (UserUtility.isModerator(user))
        {
            user.setStatus(UserStatus.DEFAULT);
        }

        if (! user.isJailed())
        {
            player.setGameMode(GameMode.SURVIVAL);
            return;
        }

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(VanilifeWorldManager.getJail().getSpawnLocation());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void loadInstance(PlayerJoinEvent event)
    {
        User.getInstance(event.getPlayer());
    }

    @EventHandler
    public void sortPlayerList(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        User user = User.getInstance(player);
        Sara sara = user.getSara();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(sara.name());

        scoreboard.getTeams().stream()
                .filter(t -> t.hasPlayer(player))
                .forEach(t -> t.removePlayer(player));

        if (team == null)
        {
            team = scoreboard.registerNewTeam(sara.name());
            team.prefix(Component.text(sara.level));
        }

        team.addPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void teleportPlayer(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getInstance(player);

        Housing housing = Housing.getInstance(player.getLocation());

        if (housing != null && ! housing.canVisit(user))
        {
            VanilifeWorld world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

            if (world != null)
            {
                player.teleport(world.getLocation(player));
                return;
            }
        }

        VanilifeWorld world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

        if (world == null)
        {
            return;
        }

        if (player.hasPlayedBefore() && (VanilifeWorld.getInstance(player.getWorld()) != null || user.inHousing()))
        {
            return;
        }

        world.getTeleporter().teleport(player);
    }
}
