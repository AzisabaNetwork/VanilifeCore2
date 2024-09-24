package net.azisaba.vanilife.vwm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.util.ResourceUtility;

public class VanilifeWorldManager
{
    private static JsonArray config = ResourceUtility.getJsonResource("vwm/vwm.json").getAsJsonArray();

    public static boolean running;

    private static int latestVersion;

    public static int getLatestVersion()
    {
        return VanilifeWorldManager.latestVersion;
    }

    public static void setLatestVersion(int latestVersion)
    {
        VanilifeWorldManager.latestVersion = latestVersion;
    }

    public static JsonArray getConfig()
    {
        return VanilifeWorldManager.config;
    }

    public static void setConfig(JsonArray config)
    {
        VanilifeWorldManager.config = config;
        ResourceUtility.save("vwm/vwm.json", VanilifeWorldManager.config);
    }

    public static JsonObject getWorldProperties(String name)
    {
        for (JsonElement properties : VanilifeWorldManager.config)
        {
            if (! (properties instanceof JsonObject))
            {
                continue;
            }

            if (properties.getAsJsonObject().get("name").getAsString().equals(name))
            {
                return properties.getAsJsonObject();
            }
        }

        return null;
    }

    public static JsonObject getWorldProperties(VanilifeWorld world)
    {
        return VanilifeWorldManager.getWorldProperties(world.getName());
    }

    public static void setWorldProperties(String name, JsonObject properties)
    {
        VanilifeWorldManager.config.remove(VanilifeWorldManager.getWorldProperties(name));

        if (properties != null)
        {
            VanilifeWorldManager.config.add(properties);
        }

        VanilifeWorldManager.setConfig(VanilifeWorldManager.config);
    }

    public static void setWorldProperties(VanilifeWorld world, JsonObject properties)
    {
        VanilifeWorldManager.setWorldProperties(world.getName(), properties);
    }

    public static void mount()
    {
        for (JsonElement world : VanilifeWorldManager.getConfig())
        {
            JsonObject properties = world.getAsJsonObject();
            VanilifeWorld vw = new VanilifeWorld(properties.get("name").getAsString());
            VanilifeWorldManager.setLatestVersion(Math.max(vw.getVersion(), VanilifeWorldManager.getLatestVersion()));
        }
    }
}
