package net.azisaba.vanilife.item;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.magic.Magic;
import net.azisaba.vanilife.magic.Magics;
import net.azisaba.vanilife.ui.AnvilUI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MagicWandItem extends VanilifeItem
{
    @Override
    public @NotNull String getName()
    {
        return "hw24.magic_wand";
    }

    @Override
    public Component getDisplayName()
    {
        return Component.text("魔法の杖").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public List<Component> getLore()
    {
        return List.of(Component.text("あなたの願い事を一つだけ叶える").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public @NotNull Material getTexture()
    {
        return Material.STICK;
    }

    @Override
    public boolean hasEnchantment()
    {
        return true;
    }

    @Override
    public void use(@NotNull Player player, @NotNull ItemStorage storage)
    {
        new AnvilUI(player, Component.text("魔法の杖"))
        {
            @Override
            public @NotNull String getPlaceholder()
            {
                return "何でも聞いてください…";
            }

            @Override
            protected void onTyped(@NotNull String string)
            {
                if (string.isBlank())
                {
                    return;
                }

                Magic magic = Magics.registry.values().stream()
                        .map(m -> Map.entry(m, MagicWandItem.this.score(m, string)))
                        .filter(entry -> 0 < entry.getValue())
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey).orElse(null);

                Vanilife.CHANNEL_CONSOLE.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(":magic_wand: Magic Wand")
                        .setColor(magic != null ? Color.ORANGE : Color.RED)
                        .addField("プロンプト", string, false)
                        .addField("魔法使い", this.player.getName(), false)
                        .addField("結果", magic != null ? magic.getClass().getName() : "null", false)
                        .build()).queue();

                if (magic == null)
                {
                    this.player.sendMessage(Component.text("魔法の杖には理解できなかった…").color(NamedTextColor.RED));
                    return;
                }

                this.player.playSound(this.player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                magic.perform(this.player);

                ItemStack wand = this.player.getInventory().getItemInMainHand();
                wand.setAmount(wand.getAmount() - 1);
            }
        };
    }

    private int score(@NotNull Magic magic, @NotNull String prompt)
    {
        return (int) magic.getKeywords().stream()
                .filter(prompt.toLowerCase()::contains)
                .count();
    }
}