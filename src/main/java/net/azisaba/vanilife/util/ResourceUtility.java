package net.azisaba.vanilife.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ResourceUtility
{
    public static File getResource(String name)
    {
        return new File(Vanilife.getPlugin().getDataFolder(), name);
    }

    public static YamlConfiguration getYamlResource(String name)
    {
        return YamlConfiguration.loadConfiguration(ResourceUtility.getResource(name));
    }

    public static JsonElement getJsonResource(String name)
    {
        try
        {
            return JsonParser.parseReader(new FileReader(ResourceUtility.getResource(name)));
        }
        catch (FileNotFoundException e)
        {
            Vanilife.sendExceptionReport(e);
            throw new RuntimeException(e);
        }
    }

    public static void save(String name, YamlConfiguration resource)
    {
        try
        {
            resource.save(ResourceUtility.getResource(name));
        }
        catch (IOException e)
        {
            Vanilife.sendExceptionReport(e);
            throw new RuntimeException(e);
        }
    }

    public static void save(String name, JsonElement resource)
    {
        try (FileWriter writer = new FileWriter(new File(Vanilife.getPlugin().getDataFolder(), name)))
        {
            writer.write(new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(resource));
        }
        catch (IOException e)
        {
            Vanilife.sendExceptionReport(e);
            throw new RuntimeException(e);
        }
    }
}
