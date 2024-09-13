package net.azisaba.vanilife.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Base64;

public class MojangAPI
{
    public static String getId(String name)
    {
        Request request = new Request.Builder().url(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name)).build();

        try (Response response = Vanilife.httpclient.newCall(request).execute())
        {
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            return json.get("id").getAsString();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static JsonObject getProfile(String id)
    {
        Request request = new Request.Builder().url(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s", id)).build();

        try (Response response = Vanilife.httpclient.newCall(request).execute())
        {
            return JsonParser.parseString(response.body().string()).getAsJsonObject();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static String getSkin(String textures)
    {
        JsonObject decodedTextures = JsonParser.parseString(new String(Base64.getDecoder().decode(textures))).getAsJsonObject();
        return decodedTextures.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
    }
}
