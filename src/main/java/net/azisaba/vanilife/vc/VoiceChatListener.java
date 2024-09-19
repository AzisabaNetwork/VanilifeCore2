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

        if (event.getChannelJoined() == null || ! event.getChannelJoined().getId().equals(Vanilife.voiceChatChannel.getId()))
        {
            return;
        }

        Member member = event.getMember();
        User user = User.getInstance(member.getUser());

        if (user == null)
        {
            Vanilife.publicServer.kickVoiceMember(member).queue();
            return;
        }

        if (! user.isOnline())
        {
            Vanilife.publicServer.kickVoiceMember(member).queue();;
            return;
        }

        VoiceChat vc = VoiceChat.search(user.getAsPlayer());
        vc.connect(user);
    }
}
