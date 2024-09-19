package net.azisaba.vanilife.command.discord;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.mconsole.MconsoleSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MconsoleCommand extends DiscordCommand
{
    @Override
    @NotNull
    public String getName()
    {
        return "mconsole";
    }

    @Override
    public void install(@NotNull Guild server)
    {
        if (server != Vanilife.privateServer)
        {
            return;
        }

        server.upsertCommand(this.getName(), "任意のMinecraft コマンドを実行します")
                .addOption(OptionType.STRING, "command", "Minecraft コマンド", true).queue();
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event)
    {
        if (! event.getMember().getRoles().contains(Vanilife.ROLE_MCONSOLE))
        {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("実行に必要な権限が不足しています")
                    .setDescription("権限設定が誤りであると思われる場合は、管理者にお問い合わせください")
                    .setColor(new Color(255, 85, 85));

            event.replyEmbeds(builder.build()).queue();
            return;
        }

        String command = event.getOption("command").getAsString();

        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
            MconsoleSender sender = new MconsoleSender();

            boolean successful = Bukkit.dispatchCommand(sender, command);

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(successful ? "コマンドは正常に実行されました" : "コマンドの実行に失敗しました")
                    .setDescription(String.join("\n", sender.getOutputs()))
                    .setFooter(command)
                    .setColor(successful ? new Color(85, 255, 85) : new Color(255, 85, 85));

            sender.getOutputs().clear();
            event.replyEmbeds(builder.build()).queue();
        });
    }
}
