package net.azisaba.vanilife.vc;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VoiceChatListener extends ListenerAdapter
{
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event)
    {
        Member member = event.getMember();

        if (member.getUser().isBot())
        {
            return;
        }

        if (event.getOldValue() != null)
        {
            VoiceChannel channel = (VoiceChannel) event.getOldValue();
            VoiceChat vc = VoiceChat.getInstance(channel);

            if (vc != null)
            {
                vc.disconnect(User.getInstance(event.getMember().getUser()));
            }

            return;
        }

        if (event.getChannelJoined() == null || ! event.getChannelJoined().getId().equals(Vanilife.CHANNEL_VOICE.getId()))
        {
            return;
        }

        User user = User.getInstance(member.getUser());

        if (user == null)
        {
            Vanilife.SERVER_PUBLIC.kickVoiceMember(member).queue();
            member.getUser().openPrivateChannel().queue(dm -> {
                dm.sendMessage("**:sound: VRVoiceChat について**\n" +
                        "VRVoiceChat は、ばにらいふ２！サーバー内で近くにいるプレイヤーと通話ができる機能です。\n" +
                        "使用するには、設定 (/settings) の「Discord」から「Discord アカウントをリンク (右クリック)」を選択して**Discord アカウントと紐づけ**をし、\n" +
                        Vanilife.CHANNEL_VOICE.getAsMention() + " に参加する必要があります。\n" +
                        "この機能についての詳細は [アジ鯖 Wiki](https://wiki.azisaba.net/wiki/ばにらいふ２！:VRVoiceChat) で確認できます。").queue();
            });
            return;
        }

        if (! user.isOnline())
        {
            Vanilife.SERVER_PUBLIC.kickVoiceMember(member).queue();
            member.getUser().openPrivateChannel().queue(dm -> {
                dm.sendMessage("**:warning: VRVoiceChat を使用するにはサーバーにログインしてください**\n" +
                        "VRVoiceChat はマインクラフトサーバー内での座標を使用して付近のプレイヤーとの通話を実現しています。\n" +
                        "**マインクラフトサーバーに接続**していない場合、この機能は利用できません。\n" +
                        "\n紐づけ中: `" + user.getPlaneName() + "`\n" +
                        "紐づけを解除するにはアジ鯖 Discord で `/vanilife-unlink` またはゲーム内の `/settings` の「Discord」から「リンクの解除 (右クリック)」を選択\n\n" +
                        "この機能についての詳細は [アジ鯖 Wiki](https://wiki.azisaba.net/wiki/ばにらいふ２！:VRVoiceChat) で確認できます。").queue();
            });
            return;
        }

        VoiceChat vc = VoiceChat.search(user.asPlayer());
        vc.connect(user);
    }
}
