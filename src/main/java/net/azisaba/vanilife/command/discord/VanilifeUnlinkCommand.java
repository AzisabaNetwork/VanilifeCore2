package net.azisaba.vanilife.command.discord;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class VanilifeUnlinkCommand extends DiscordCommand
{
    @Override
    public @NotNull String getName()
    {
        return "vanilife-unlink";
    }

    @Override
    public void install(@NotNull Guild server)
    {
        if (server != Vanilife.SERVER_PUBLIC)
        {
            return;
        }

        server.upsertCommand(this.getName(), "ばにらいふ！ プロフィールと Discord アカウントの紐づけを解除します").queue();
    }

    @Override
    public void onCommand(@NotNull SlashCommandInteractionEvent event)
    {
        User user = User.getInstance(event.getUser());

        if (user == null)
        {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("紐づけが見つかりませんでした")
                    .setDescription("お使いの Discord アカウント が紐づいたばにらいふ！ プロフィールは見つかりませんでした。")
                    .setColor(new Color(255, 85, 85)).build()).queue();
            return;
        }

        user.setDiscord((net.dv8tion.jda.api.entities.User) null);

        if (user.isOnline())
        {
            Player player = user.asPlayer();
            player.sendMessage(Component.text(event.getUser().getEffectiveName() + " との紐づけを解除しました！").color(NamedTextColor.GREEN));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        event.replyEmbeds(new EmbedBuilder()
                .setTitle(event.getUser().getEffectiveName() + " と " + user.getPlaneName() + " の紐づけを解除しました")
                .setDescription(user.getPlaneName() + " との紐づけを解除しました！")
                .setColor(new Color(85, 255, 85)).build()).queue();
    }
}
