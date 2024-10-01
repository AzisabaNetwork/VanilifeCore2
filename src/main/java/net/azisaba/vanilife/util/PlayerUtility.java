package net.azisaba.vanilife.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.azisaba.vanilife.Vanilife;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class PlayerUtility
{
    public static String getIpAddress(@NotNull Player player)
    {
        InetSocketAddress address = player.getAddress();

        if (address == null)
        {
            return null;
        }

        String ip = address.getAddress().getHostAddress();

        if (ip.equals("127.0.0.1"))
        {
            Request request = new Request.Builder().url("https://api.ipify.org/").build();

            try (Response response = Vanilife.httpclient.newCall(request).execute())
            {
                if (response.isSuccessful())
                {
                    return response.body().string();
                }
            }
            catch (IOException ignored) {}
        }

        return ip;
    }

    public static @NotNull String getCountry(@NotNull String ip)
    {
        String url = "http://ipinfo.io/" + ip + "/json";

        Request request = new Request.Builder().url(url).build();

        try (Response response = Vanilife.httpclient.newCall(request).execute())
        {
            if (response.isSuccessful() && response.body() != null)
            {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return json.get("country").getAsString();
            }
        }
        catch (IOException ignored) {}

        return "JP";
    }

    public static @NotNull String getCountry(@NotNull Player player)
    {
        String ip = PlayerUtility.getIpAddress(player);
        return (ip == null) ? "JP" : PlayerUtility.getCountry(ip);
    }

    public static void giveItemStack(@NotNull Player player, @NotNull List<ItemStack> stacks)
    {
        Inventory inventory = player.getInventory();

        List<ItemStack> remaining = new ArrayList<>();

        stacks.forEach(stack -> {
            if (stack != null && 0 < stack.getAmount())
            {
                remaining.addAll(Arrays.asList(inventory.addItem(stack).values().toArray(new ItemStack[0])));
            }
        });

        remaining.forEach(stack -> {
            player.getWorld().dropItem(player.getLocation(), stack);
        });
    }

    public static void giveItemStack(@NotNull Player player, @NotNull ItemStack stack)
    {
        PlayerUtility.giveItemStack(player, Collections.singletonList(stack));
    }
}
