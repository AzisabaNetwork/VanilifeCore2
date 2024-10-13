package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class TitleUI
{
    public static void typing(@NotNull Player listener, @NotNull TextComponent message)
    {
        TitleUI.typing(listener, message, 10L);
    }

    public static void typing(@NotNull Player listener, @NotNull TextComponent message, long period)
    {
        final String text = message.content();

        new BukkitRunnable()
        {
            private int i = 0;
            private String string;

            @Override
            public void run()
            {
                if (text.length() <= this.i ++)
                {
                    this.cancel();
                    return;
                }

                this.string = text.substring(0, this.i);
                listener.showTitle(Title.title(Component.text(this.string), Component.text().build()));
                listener.playSound(listener, Sound.UI_BUTTON_CLICK, 1.0f, 1.5f);
            }
        }.runTaskTimer(Vanilife.getPlugin(), 0L, period);
    }
}
