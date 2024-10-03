package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.gomenne.ConvertRequest;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.user.Skin;
import net.azisaba.vanilife.ui.Language;
import net.azisaba.vanilife.user.mail.Mail;
import net.azisaba.vanilife.report.Report;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.UserStatus;
import net.azisaba.vanilife.util.ReportUtility;
import net.azisaba.vanilife.util.StringUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class DiscordListener extends ListenerAdapter
{
    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        Vanilife.SERVER_PUBLIC = event.getJDA().getGuildById(Vanilife.getPluginConfig().getString("discord.server.public"));
        Vanilife.SERVER_PRIVATE = event.getJDA().getGuildById(Vanilife.getPluginConfig().getString("discord.server.private"));

        Vanilife.CHANNEL_ANNOUNCE = Vanilife.SERVER_PRIVATE.getNewsChannelById(Vanilife.getPluginConfig().getString("discord.channel.announce"));
        Vanilife.CHANNEL_CONSOLE = Vanilife.SERVER_PRIVATE.getTextChannelById(Vanilife.getPluginConfig().getString("discord.channel.console"));
        Vanilife.CHANNEL_HISTORY = Vanilife.SERVER_PRIVATE.getTextChannelById(Vanilife.getPluginConfig().getString("discord.channel.history"));
        Vanilife.CHANNEL_VOICE = Vanilife.SERVER_PRIVATE.getVoiceChannelById(Vanilife.getPluginConfig().getString("discord.channel.voice-chat"));

        Vanilife.ROLE_SUPPORT = Vanilife.SERVER_PRIVATE.getRoleById(Vanilife.getPluginConfig().getString("discord.role.support"));
        Vanilife.ROLE_MCONSOLE = Vanilife.SERVER_PRIVATE.getRoleById(Vanilife.getPluginConfig().getString("discord.role.mconsole"));
        Vanilife.ROLE_DEVELOPER = Vanilife.SERVER_PRIVATE.getRoleById(Vanilife.getPluginConfig().getString("discord.role.developer"));

        Vanilife.category = Vanilife.SERVER_PUBLIC.getCategoryById(Vanilife.getPluginConfig().getString("discord.category"));

        for (VoiceChannel channel : Vanilife.category.getVoiceChannels())
        {
            if (! channel.getName().startsWith("VC-"))
            {
                continue;
            }

            for (Member member : channel.getMembers())
            {
                Vanilife.SERVER_PUBLIC.kickVoiceMember(member).queue();
            }

            channel.delete().queue();
        }

        Language.mount();
        User.mount();
        Skin.mount();
        Bukkit.getScheduler().runTask(Vanilife.getPlugin(), Housing::mount);
        ReportUtility.mount();
        ConvertRequest.mount();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event)
    {
        if (event.getButton().getId() == null)
        {
            return;
        }

        if (! event.getMember().getRoles().contains(Vanilife.ROLE_SUPPORT))
        {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("実行に必要な権限が不足しています")
                    .setDescription("権限設定が誤りであると思われる場合は、管理者にお問い合わせください")
                    .setColor(new Color(255, 85, 85));

            event.replyEmbeds(builder.build()).queue();
            return;
        }

        if (event.getButton().getId().equals("vanilife:mute"))
        {
            Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
                String name = event.getMessage().getEmbeds().getFirst().getAuthor().getName();

                User user = User.getInstance(UUID.fromString(event.getMessage().getEmbeds().getFirst().getFooter().getText()));
                user.setStatus(UserStatus.MUTED);

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(String.format("%s のミュートに成功しました", name))
                        .addField("実行者", event.getMember().getEffectiveName(), true)
                        .setColor(new Color(85, 255, 85));

                event.replyEmbeds(builder.build()).queue();
            });
        }

        if (event.getButton().getId().equals("vanilife:unmute"))
        {
            Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
                String name = event.getMessage().getEmbeds().getFirst().getAuthor().getName();

                User user = User.getInstance(UUID.fromString(event.getMessage().getEmbeds().getFirst().getFooter().getText()));
                user.setStatus(UserStatus.DEFAULT);

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(String.format("%s のミュートを解除しました", name))
                        .addField("実行者", event.getMember().getEffectiveName(), true)
                        .setColor(new Color(85, 255, 85));

                event.replyEmbeds(builder.build()).queue();
            });
        }

        if (event.getButton().getId().startsWith("vanilife:coreprotect."))
        {
            MessageEmbed message = event.getMessage().getEmbeds().getFirst();
            MessageEmbed.Footer footer = message.getFooter();

            if (footer == null)
            {
                return;
            }

            String footerText = footer.getText();

            if (footerText == null)
            {
                return;
            }

            Map<String, String> parameters = StringUtility.parameters(footerText);

            int page = Integer.parseInt(parameters.get("page"));
            World world = Bukkit.getWorld(parameters.get("world"));
            int x = Integer.parseInt(parameters.get("x"));
            int y = Integer.parseInt(parameters.get("y"));
            int z = Integer.parseInt(parameters.get("z"));

            page = (event.getButton().getId().endsWith("back")) ? Math.max(page - 1, 0) : page + 1;

            event.getMessage().editMessageEmbeds(Report.getCoreProtectViewer(new Location(world, x, y, z), page).build()).queue();
            event.deferEdit().queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        Message reply = event.getMessage();

        if (reply.getReferencedMessage() == null)
        {
            return;
        }

        Report report = Report.getInstance(reply.getReferencedMessage());

        if (report == null)
        {
            return;
        }

        if (! event.getMember().getRoles().contains(Vanilife.ROLE_SUPPORT))
        {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("実行に必要な権限が不足しています")
                    .setDescription("権限設定が誤りであると思われる場合は、管理者にお問い合わせください")
                    .setColor(new Color(255, 85, 85));

            Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(builder.build()).queue();
            return;
        }

        new Mail(User.getInstance("azisaba"), report.getSender(), "サポートが発行されました",
                String.format("お待たせいたしました。お寄せいただいたレポートにサポートが発行されたのでご確認をお願いします。\n\n%s\n\nレポート:\n%s\n\n担当者: %s", reply.getContentRaw(), report.getDetails(), event.getMember().getNickname()));
        reply.addReaction(Emoji.fromUnicode("U+2705")).queue();
    }
}
