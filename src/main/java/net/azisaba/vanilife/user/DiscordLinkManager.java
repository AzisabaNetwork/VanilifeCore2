package net.azisaba.vanilife.user;

import net.azisaba.vanilife.Vanilife;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DiscordLinkManager
{
    private static final Map<String, User> registry = new HashMap<>();

    public static User link(@NotNull String token, @NotNull net.dv8tion.jda.api.entities.User discord)
    {
        User user = DiscordLinkManager.registry.get(token);

        if (user == null)
        {
            return null;
        }

        user.setDiscord(discord);
        DiscordLinkManager.registry.remove(token);
        return user;
    }

    @NotNull
    public static String newToken(@NotNull User user)
    {

        DiscordLinkManager.registry.entrySet().removeIf(entry -> entry.getValue() == user);

        String token;
        final String characters = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnoprstuvwxyz12345678";

        do
        {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 8; i ++)
            {
                sb.append(characters.charAt(Vanilife.random.nextInt(characters.length())));
            }

            token = sb.toString();
        }
        while (DiscordLinkManager.registry.containsKey(token));

        DiscordLinkManager.registry.put(token, user);

        final String t = token;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                DiscordLinkManager.registry.remove(t);
            }
        }.runTaskLater(Vanilife.getPlugin(), 20L * 60 * 3);

        return token;
    }

    public static boolean isToken(String token)
    {
        return DiscordLinkManager.registry.containsKey(token);
    }
}
