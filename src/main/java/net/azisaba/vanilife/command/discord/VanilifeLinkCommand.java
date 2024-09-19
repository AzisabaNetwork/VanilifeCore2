package net.azisaba.vanilife.command.discord;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.DiscordLinkManager;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class VanilifeLinkCommand extends DiscordCommand
{
    @Override
    public @NotNull String getName()
    {
        return "vanilife-link";
    }

    @Override
    public void install(@NotNull Guild server)
    {
        if (server != Vanilife.publicServer)
        {
            return;
        }

        server.upsertCommand(this.getName(), "ばにらいふ！ プロフィールと Discord アカウントを紐づけます")
                .addOption(OptionType.STRING, "token", "ばにらいふ！内で発行されたトークン", true).queue();
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event)
    {
        final String token = event.getOption("token").getAsString();

        if (! DiscordLinkManager.isToken(token))
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle(token + " は無効なトークンです")
                    .setDescription("有効期限が切れている可能性があります。\nばにらいふ！内の設定( /settings )からもう一度 Discord リンク を選択してください。")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        if (User.getInstance(event.getUser()) != null)
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("既存の紐づけが見つかりました")
                    .setDescription("別のばにらいふ！ プロフィールとの紐づけが見つかりました。\n1つの Discord アカウント につき紐づけできるのは1つのばにらいふ！ プロフィールです。\n既存の紐づけを解除するには `/vanilife-unlink` を使用してください。")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        User user = DiscordLinkManager.link(token, event.getUser());

        if (user == null)
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("おっと…Discord アカウントの紐づけに失敗しました")
                    .setDescription("処理の途中で何らかのエラーが発生したようです。\n大変申し訳ありませんが、しばらくしてからもう一度お試しください。")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        if (user.isOnline())
        {
            Player player = user.getAsPlayer();
            player.sendMessage(Component.text(event.getUser().getEffectiveName() + " と紐づけしました！").color(NamedTextColor.GREEN));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        event.replyEmbeds(new EmbedBuilder()
                .setTitle(event.getUser().getEffectiveName() + " を " + user.getPlaneName() + " に紐づけました！")
                .setDescription("Discord アカウントのリンクが正常に完了しました。\nありがとうございました。")
                .setColor(new Color(85, 255, 85)).build()).queue();
    }
}
