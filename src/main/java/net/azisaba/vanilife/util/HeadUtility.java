package net.azisaba.vanilife.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonObject;
import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HeadUtility
{
    public static final String YOUTUBE = "https://textures.minecraft.net/texture/e7c9eec58d666b8e7754deb3aaebd6c27c2441c2dbb649dd4234878d30dd85b1";
    public static final String TWITTER = "https://textures.minecraft.net/texture/cd3887ea3dea392ce1f5a7146ba6737803de36802d5b40915bdb172b48b8650b";
    public static final String DISCORD = "https://textures.minecraft.net/texture/3c30f0c195be1ab123949e446559c79eb9fe36fbd14933d90b80765c078e4c87";

    public static ItemStack getPlayerHead(String name)
    {
        ItemStack headStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) headStack.getItemMeta();

        if (Bukkit.getPlayerExact(name) != null)
        {
            headMeta.setOwningPlayer(Bukkit.getPlayer(name));
        }
        else
        {
            JsonObject profile = MojangAPI.getProfile(MojangAPI.getId(name));
            String textures = profile.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

            headMeta.setPlayerProfile(HeadUtility.getPlayerProfile(MojangAPI.getSkin(textures)));
        }

        headStack.setItemMeta(headMeta);
        return headStack;
    }

    public static PlayerProfile getPlayerProfile(String url)
    {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try
        {
            URL urlObject = new URL(url);
            textures.setSkin(urlObject);
            profile.setTextures(textures);
            return profile;
        }
        catch (MalformedURLException e)
        {
            Vanilife.sendExceptionReport(e);
            Vanilife.getPluginLogger().error(Component.text(e.getMessage()).color(NamedTextColor.RED));
            return null;
        }
    }
}
