package net.azisaba.vanilife.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.ResourceUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Language
{
    private static final List<Language> instances = new ArrayList<>();

    public static Language getInstance(@NotNull String id)
    {
        List<Language> filteredInstances = Language.instances.stream().filter(i -> i.getId().equals(id)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static @NotNull Language getInstance(@NotNull User user)
    {
        return user.getSettings().LANGUAGE.getLanguage();
    }

    public static @NotNull Language getInstance(@NotNull Player player)
    {
        return Language.getInstance(User.getInstance(player));
    }

    public static List<Language> getInstances()
    {
        return Language.instances;
    }

    public static Component translate(@NotNull String key, @NotNull Language language, String... args)
    {
        return language.translate(key, args);
    }

    public static Component translate(@NotNull String key, @NotNull Player player, String... args)
    {
        return Language.translate(key, Language.getInstance(player), args);
    }

    public static Component translate(@NotNull String key, @NotNull User user, String... args)
    {
        return Language.translate(key, Language.getInstance(user), args);
    }

    public static boolean has(@NotNull String key, @NotNull Language language)
    {
        return language.has(key);
    }

    public static boolean has(@NotNull String key, @NotNull Player player)
    {
        return Language.has(key, Language.getInstance(player));
    }

    public static boolean has(@NotNull String key, @NotNull User user)
    {
        return Language.has(key, Language.getInstance(user));
    }

    public static void mount()
    {
        File directory = new File(Vanilife.getPlugin().getDataFolder(), "/lang");

        if (! directory.exists())
        {
            directory.mkdirs();
        }

        File[] languages = directory.listFiles();

        if (languages == null)
        {
            return;
        }

        for (File language : languages)
        {
            if (! language.isFile())
            {
                continue;
            }

            new Language(language.getName());
        }
    }

    private final String id;
    private final String name;
    private final String texture;
    private final Map<String, String> translation = new HashMap<>();

    private Language(@NotNull String path)
    {
        final JsonObject resource = ResourceUtility.getJsonResource("/lang/" + path).getAsJsonObject();

        this.id = path.lastIndexOf('.') == -1 ? path : path.substring(0, path.lastIndexOf('.'));
        this.name = resource.get("name").getAsString();
        this.texture = resource.get("texture").getAsString();

        resource.getAsJsonObject("translation").asMap().forEach((String key, JsonElement translation) -> this.translation.put(key, translation.getAsString()));

        Language.instances.add(this);
    }

    public @NotNull String getId()
    {
        return this.id;
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public @NotNull String getTexture()
    {
        return this.texture;
    }

    public @NotNull Component translate(@NotNull String key, String... args)
    {
        String src = this.translation.getOrDefault(key, key);

        for (String arg : Arrays.stream(args).filter(a -> a.split("=").length == 2).toList())
        {
            String name = arg.split("=")[0];
            String value = arg.split("=")[1];

            src = src.replace("${" + name + "}", value);
        }

        return ComponentUtility.getAsComponent(src);
    }

    public boolean has(@NotNull String key)
    {
        return this.translation.containsKey(key);
    }
}
