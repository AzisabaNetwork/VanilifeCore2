package net.azisaba.vanilife.command.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordCommand extends ListenerAdapter implements IDiscordCommand
{
    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        event.getJDA().getGuilds().forEach(this::install);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
    {
        if (event.getName().equals(this.getName()))
        {
            this.onCommand(event);
        }
    }
}
