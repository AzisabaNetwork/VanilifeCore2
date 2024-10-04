package net.azisaba.vanilife.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MojangAPI
{
    public static final Map<String, String> CACHE_ID = new HashMap<>();

    public static String getId(String name)
    {
        if (MojangAPI.CACHE_ID.containsKey(name))
        {
            return MojangAPI.CACHE_ID.get(name);
        }

        Request request = new Request.Builder().url(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name)).build();

        try (Response response = Vanilife.httpclient.newCall(request).execute())
        {
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            String id = json.get("id").getAsString();

            MojangAPI.CACHE_ID.put(name, id);
            return id;
        }
        catch (IOException e)
        {
            Vanilife.sendExceptionReport(e);
            return null;
        }
    }

    public static final Map<String, JsonObject> CACHE_PROFILE = new HashMap<>();

    public static JsonObject getProfile(String id)
    {
        if (MojangAPI.CACHE_PROFILE.containsKey(id))
        {
            return MojangAPI.CACHE_PROFILE.get(id);
        }

        Request request = new Request.Builder().url(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s", id)).build();

        try (Response response = Vanilife.httpclient.newCall(request).execute())
        {
            JsonObject profile = JsonParser.parseString(response.body().string()).getAsJsonObject();
            MojangAPI.CACHE_PROFILE.put(id, profile);
            return profile;
        }
        catch (IOException e)
        {
            Vanilife.sendExceptionReport(e);
            return null;
        }
    }

    public static String getSkin(String textures)
    {
        JsonObject decodedTextures = JsonParser.parseString(new String(Base64.getDecoder().decode(textures))).getAsJsonObject();
        return decodedTextures.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
    }
}
