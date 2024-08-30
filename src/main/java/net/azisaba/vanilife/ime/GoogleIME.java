package net.azisaba.vanilife.ime;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleIME
{
    private static final String API_URL = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=";

    public static String convert(String src)
    {
        Request request = new Request.Builder().url(GoogleIME.API_URL + URLEncoder.encode(src, StandardCharsets.UTF_8)).build();

        try (Response response = Vanilife.httpClient.newCall(request).execute())
        {
            return response.isSuccessful() ? JsonParser.parseString(response.body().string()).getAsJsonArray().get(0).getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString() : src;
        }
        catch (IOException | JsonParseException e)
        {
            return src;
        }
    }
}
