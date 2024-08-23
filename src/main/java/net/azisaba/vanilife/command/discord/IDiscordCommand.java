package net.azisaba.vanilife.command.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface IDiscordCommand
{
    String getName();

    void install(Guild server);

    void onCommand(SlashCommandInteractionEvent event);
}
