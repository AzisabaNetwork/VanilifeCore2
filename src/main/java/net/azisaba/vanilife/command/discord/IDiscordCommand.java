package net.azisaba.vanilife.command.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public interface IDiscordCommand
{
    @NotNull String getName();

    void install(@NotNull Guild server);

    void onCommand(@NotNull SlashCommandInteractionEvent event);
}
