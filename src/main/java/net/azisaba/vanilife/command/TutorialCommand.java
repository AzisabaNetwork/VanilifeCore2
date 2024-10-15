package net.azisaba.vanilife.command;

import com.google.gson.*;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.util.ComponentUtility;
import net.azisaba.vanilife.util.ResourceUtility;
import net.azisaba.vanilife.util.UserUtility;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorialCommand implements CommandExecutor, TabCompleter
{
    private final Map<Language, Book> map = new HashMap<>();

    public TutorialCommand()
    {
        this.compile();
    }

    private void compile()
    {
        for (Language lang : Language.getInstances())
        {
            final String path = "/tutorial/" + lang.getId() + ".json";

            if (! ResourceUtility.getResource(path).exists())
            {
                continue;
            }

            final JsonObject source = ResourceUtility.getJsonResource(path).getAsJsonObject();

            List<Component> content = new ArrayList<>();

            source.get("content").getAsJsonArray().forEach(page -> {
                TextComponent.Builder builder = Component.text();

                page.getAsJsonArray().forEach(row -> {
                    try
                    {
                        new JSONObject(row.getAsString());
                    }
                    catch (JSONException e)
                    {
                        try
                        {
                            new JSONArray(row.getAsString());
                            builder.append(GsonComponentSerializer.gson().deserialize(row.getAsString()).appendNewline());
                        }
                        catch (JSONException e2)
                        {
                            builder.append(ComponentUtility.asComponent(row.getAsString()).appendNewline());
                        }
                    }
                });

                content.add(builder.build());
            });

            this.map.put(lang, Book.book(ComponentUtility.asComponent(source.get("title").getAsString()),
                    ComponentUtility.asComponent(source.get("author").getAsString()),
                    content));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 0 && UserUtility.isAdmin(player))
        {
            if (! args[0].equals("reload"))
            {
                sender.sendMessage(Component.text("Correct syntax: /tutorial").color(NamedTextColor.RED));
                return true;
            }

            this.compile();
            sender.sendMessage(Component.text("チュートリアルファイルの再読み込みに成功しました").color(NamedTextColor.GREEN));
            return true;
        }

        Language lang = Language.getInstance(player);
        Book tutorial = this.map.containsKey(lang) ? this.map.get(lang) : this.map.get(Language.getInstance("ja-jp"));
        player.openBook(tutorial);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (! UserUtility.isAdmin(sender) || args.length != 1)
        {
            return List.of();
        }

        return List.of("reload");
    }
}
