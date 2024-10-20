package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.SoundGimmickUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SoundGimmick extends VanilifeGimmick
{
    private Sound sound;

    public static @NotNull ItemStack getItemStack()
    {
        ItemStack messageStack = new ItemStack(Material.COMMAND_BLOCK);

        ItemMeta messageMeta = messageStack.getItemMeta();
        messageMeta.displayName(Component.text("ギミックブロック").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)
                .appendSpace()
                .append(Component.text("(サウンド)").color(NamedTextColor.GRAY)));
        messageMeta.getPersistentDataContainer().set(new NamespacedKey(Vanilife.getPlugin(), "gimmick.type"), PersistentDataType.STRING, "sound");

        messageStack.setItemMeta(messageMeta);
        return messageStack;
    }

    public SoundGimmick(@NotNull Block block)
    {
        super(block);

        if (this.readString("sound") == null)
        {
            this.sound = Sound.INFO;
        }
        else
        {
            this.sound = Sound.valueOf(this.readString("sound"));
        }
    }

    @Override
    public @NotNull String getType()
    {
        return "sound";
    }

    @Override
    public @NotNull ItemStack getDrop()
    {
        return SoundGimmick.getItemStack();
    }

    public @NotNull Sound getSound()
    {
        return this.sound;
    }

    public void setSound(@NotNull Sound sound)
    {
        this.sound = sound;
        this.write("sound", this.sound.name());
    }

    @Override
    public void use(@NotNull PlayerInteractEvent event)
    {
        new SoundGimmickUI(event.getPlayer(), this);
    }

    @Override
    public void run(@NotNull Player player)
    {
        this.sound.play(player);
    }

    public enum Sound
    {
        BREAK(org.bukkit.Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 1.2f),
        CLICK(org.bukkit.Sound.UI_BUTTON_CLICK, 1.0f, 1.2f),
        WELCOME(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f),
        NOTICE(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f),
        INFO(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f),
        WARN(org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.1f);

        private final org.bukkit.Sound sound;
        private final float volume;
        private final float pitch;

        Sound(@NotNull org.bukkit.Sound sound, float volume, float pitch)
        {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
        }

        public void play(@NotNull Player listener)
        {
            listener.playSound(listener, this.sound, this.volume, this.pitch);
        }
    }
}
