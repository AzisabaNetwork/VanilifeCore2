package net.azisaba.vanilife.command.discord;

import net.azisaba.vanilife.Vanilife;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.FileUpload;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import okhttp3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MetubotCommand extends DiscordCommandImpl
{
    @Override
    public String getName()
    {
        return "metubot";
    }

    @Override
    public void install(Guild server)
    {
        server.upsertCommand(this.getName(), "ベータ版: (*'▽') metubotを使用してキャプチャを取得します。")
                .addOption(OptionType.STRING, "world", "ワールド名", true)
                .addOption(OptionType.INTEGER, "x", "x座標", true)
                .addOption(OptionType.INTEGER, "y", "y座標", true)
                .addOption(OptionType.INTEGER, "z", "z座標", true)
                .addOption(OptionType.NUMBER, "yaw", "yaw 視点")
                .addOption(OptionType.NUMBER, "pitch", "pitch 視点").queue();
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event)
    {
        if (! event.getMember().getRoles().contains(Vanilife.ROLE_SUPPORT))
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("実行に必要な権限が不足しています")
                    .setDescription("権限設定が誤りであると思われる場合は、管理者にお問い合わせください")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        Player player = Bukkit.getPlayer(Vanilife.METUBOT_PROVIDER);

        if (player == null)
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("プロバイダーが現在オフラインです")
                    .setDescription("プロバイダーがサーバーに接続していないため、キャプチャを取得することができません")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        World world = Bukkit.getWorld(event.getOption("world").getAsString());

        if (world == null)
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("キャプチャの取得に失敗しました")
                    .setDescription(String.format("ワールド %s を解決することができませんでした", event.getOption("world").getAsString()))
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        int x = event.getOption("x").getAsInt();
        int y = event.getOption("y").getAsInt();
        int z = event.getOption("z").getAsInt();

        Location location = new Location(world, x, y, z);

        if (event.getOption("yaw") != null)
        {
            location.setYaw((float) event.getOption("yaw").getAsDouble());
        }

        if (event.getOption("pitch") != null)
        {
            location.setPitch((float) event.getOption("pitch").getAsDouble());
        }

        event.replyEmbeds(new EmbedBuilder().setDescription("キャプチャを問い合わせ中…").setColor(new Color(85, 255, 85)).build()).queue();

        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
            player.teleport(new Location(world, x, y, z));
            Request request = new Request.Builder().url(Vanilife.METUBOT_SERVER).build();

            Vanilife.httpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e)
                {
                    Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get capture: %s", e.getMessage())).color(NamedTextColor.RED));
                    event.replyEmbeds(new EmbedBuilder().setTitle("キャプチャの取得に失敗しました").setDescription(e.getMessage()).setColor(new Color(255, 85, 85)).build()).queue();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                {
                    if (! response.isSuccessful())
                    {
                        event.replyEmbeds(new EmbedBuilder().setTitle("キャプチャの取得に失敗しました").setDescription(response.message()).setColor(new Color(255, 85, 85)).build()).queue();
                        return;
                    }

                    File tempFile = File.createTempFile(String.format("%s-%s-%s-%s", world.getName(), x, y, z), ".png");
                    Files.write(tempFile.toPath(), response.body().bytes());

                    Vanilife.channel.sendMessageEmbeds(new EmbedBuilder()
                            .setTitle("キャプチャを取得しました")
                            .setFooter("この機能はベータ版です、適切なキャプチャではない可能性があります")
                            .addField("座標", location.toString(), true)
                            .addField("Capture Server", Vanilife.METUBOT_SERVER, true)
                            .addField("Capture Provider", Vanilife.METUBOT_PROVIDER.toString(), true)
                            .build()).addFiles(FileUpload.fromData(tempFile)).queue();

                    tempFile.delete();
                }
            });
        });
    }
}
