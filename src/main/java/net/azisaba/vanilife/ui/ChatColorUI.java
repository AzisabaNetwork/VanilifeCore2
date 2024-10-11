package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.chat.Chat;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.util.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.azisaba.vanilife.user.Sara.$100YEN;
import static net.azisaba.vanilife.user.Sara.$500YEN;

public class ChatColorUI extends ChestUI
{
    private final Chat chat;

    public ChatColorUI(@NotNull Player player, @NotNull Chat chat)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.chat-color.title", player)));

        this.chat = chat;

        ItemStack blueStack = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta blueMeta = blueStack.getItemMeta();
        blueMeta.displayName(Language.translate("ui.chat-color.blue", this.player).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));

        if (this.chat.getColor().value() == NamedTextColor.BLUE.value())
        {
            blueMeta.addEnchant(Enchantment.INFINITY, 1, false);
            blueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        blueStack.setItemMeta(blueMeta);
        this.inventory.setItem(10, blueStack);

        ItemStack redStack = new ItemStack(Material.RED_DYE);
        ItemMeta redMeta = redStack.getItemMeta();
        redMeta.displayName(Language.translate("ui.chat-color.red", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        redMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString($100YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.RED.value())
        {
            redMeta.addEnchant(Enchantment.INFINITY, 1, false);
            redMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        redStack.setItemMeta(redMeta);
        this.inventory.setItem(11, redStack);

        ItemStack limeStack = new ItemStack(Material.LIME_DYE);
        ItemMeta limeMeta = limeStack.getItemMeta();
        limeMeta.displayName(Language.translate("ui.chat-color.lime", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        limeMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString($100YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.GREEN.value())
        {
            limeMeta.addEnchant(Enchantment.INFINITY, 1, false);
            limeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        limeStack.setItemMeta(limeMeta);
        this.inventory.setItem(12, limeStack);

        ItemStack yellowStack = new ItemStack(Material.YELLOW_DYE);
        ItemMeta yellowMeta = yellowStack.getItemMeta();
        yellowMeta.displayName(Language.translate("ui.chat-color.yellow", this.player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        yellowMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString($500YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.YELLOW.value())
        {
            yellowMeta.addEnchant(Enchantment.INFINITY, 1, false);
            yellowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        yellowStack.setItemMeta(yellowMeta);
        this.inventory.setItem(13, yellowStack);

        ItemStack pinkStack = new ItemStack(Material.PINK_DYE);
        ItemMeta pinkMeta = pinkStack.getItemMeta();
        pinkMeta.displayName(Language.translate("ui.chat-color.pink", this.player).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
        pinkMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString($500YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.LIGHT_PURPLE.value())
        {
            pinkMeta.addEnchant(Enchantment.INFINITY, 1, false);
            pinkMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        pinkStack.setItemMeta(pinkMeta);
        this.inventory.setItem(14, pinkStack);

        ItemStack aquaStack = new ItemStack(Material.LIGHT_BLUE_DYE);
        ItemMeta aquaMeta = aquaStack.getItemMeta();
        aquaMeta.displayName(Language.translate("ui.chat-color.aqua", this.player).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        aquaMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$1000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.AQUA.value())
        {
            aquaMeta.addEnchant(Enchantment.INFINITY, 1, false);
            aquaMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        aquaStack.setItemMeta(aquaMeta);
        this.inventory.setItem(15, aquaStack);

        ItemStack cyanStack = new ItemStack(Material.CYAN_DYE);
        ItemMeta cyanMeta = cyanStack.getItemMeta();
        cyanMeta.displayName(Language.translate("ui.chat-color.cyan", this.player).color(NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false));
        cyanMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$1000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.DARK_AQUA.value())
        {
            cyanMeta.addEnchant(Enchantment.INFINITY, 1, false);
            cyanMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        cyanStack.setItemMeta(cyanMeta);
        this.inventory.setItem(16, cyanStack);

        ItemStack greenStack = new ItemStack(Material.GREEN_DYE);
        ItemMeta greenMeta = greenStack.getItemMeta();
        greenMeta.displayName(Language.translate("ui.chat-color.green", this.player).color(NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false));
        greenMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$2000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.DARK_GREEN.value())
        {
            greenMeta.addEnchant(Enchantment.INFINITY, 1, false);
            greenMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        greenStack.setItemMeta(greenMeta);
        this.inventory.setItem(19, greenStack);

        ItemStack darkRedStack = new ItemStack(Material.REDSTONE);
        ItemMeta darkRedMeta = darkRedStack.getItemMeta();
        darkRedMeta.displayName(Language.translate("ui.chat-color.dark_red", this.player).color(NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false));
        darkRedMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$2000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.DARK_RED.value())
        {
            darkRedMeta.addEnchant(Enchantment.INFINITY, 1, false);
            darkRedMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        darkRedStack.setItemMeta(darkRedMeta);
        this.inventory.setItem(20, darkRedStack);

        ItemStack purpleStack = new ItemStack(Material.PURPLE_DYE);
        ItemMeta purpleMeta = purpleStack.getItemMeta();
        purpleMeta.displayName(Language.translate("ui.chat-color.purple", this.player).color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
        purpleMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$5000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.DARK_PURPLE.value())
        {
            purpleMeta.addEnchant(Enchantment.INFINITY, 1, false);
            purpleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        purpleStack.setItemMeta(purpleMeta);
        this.inventory.setItem(21, purpleStack);

        ItemStack goldStack = new ItemStack(Material.ORANGE_DYE);
        ItemMeta goldMeta = goldStack.getItemMeta();
        goldMeta.displayName(Language.translate("ui.chat-color.gold", this.player).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        goldMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$5000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.GOLD.value())
        {
            goldMeta.addEnchant(Enchantment.INFINITY, 1, false);
            goldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        goldStack.setItemMeta(goldMeta);
        this.inventory.setItem(22, goldStack);

        ItemStack grayStack = new ItemStack(Material.GRAY_DYE);
        ItemMeta grayMeta = grayStack.getItemMeta();
        grayMeta.displayName(Language.translate("ui.chat-color.gray", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        grayMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$10000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.GRAY.value())
        {
            grayMeta.addEnchant(Enchantment.INFINITY, 1, false);
            grayMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        grayStack.setItemMeta(grayMeta);
        this.inventory.setItem(23, grayStack);

        ItemStack whiteStack = new ItemStack(Material.BONE_MEAL);
        ItemMeta whiteMeta = whiteStack.getItemMeta();
        whiteMeta.displayName(Language.translate("ui.chat-color.white", this.player).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        whiteMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$10000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.WHITE.value())
        {
            whiteMeta.addEnchant(Enchantment.INFINITY, 1, false);
            whiteMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        whiteStack.setItemMeta(whiteMeta);
        this.inventory.setItem(24, whiteStack);

        ItemStack blackStack = new ItemStack(Material.INK_SAC);
        ItemMeta blackMeta = blackStack.getItemMeta();
        blackMeta.displayName(Language.translate("ui.chat-color.black", this.player).color(NamedTextColor.BLACK).decoration(TextDecoration.ITALIC, false));
        blackMeta.lore(List.of(Component.text().build(),
                Language.translate("ui.chat-color.required", this.getPlayer(), "sara=" + ComponentUtility.asString(Sara.$50000YEN.role)).decoration(TextDecoration.ITALIC, false)));

        if (this.chat.getColor().value() == NamedTextColor.BLACK.value())
        {
            blackMeta.addEnchant(Enchantment.INFINITY, 1, false);
            blackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        blackStack.setItemMeta(blackMeta);
        this.inventory.setItem(25, blackStack);

        ItemStack returnStack = new ItemStack(Material.ARROW);
        ItemMeta returnMeta = returnStack.getItemMeta();
        returnMeta.displayName(Language.translate("ui.return", player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        returnMeta.lore(List.of(Language.translate("ui.return.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        returnStack.setItemMeta(returnMeta);
        this.inventory.setItem(39, returnStack);

        ItemStack closeStack = new ItemStack(Material.ANVIL);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(40, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 1.0f, 1.2f);

        if (event.getSlot() == 10)
        {
            this.chat.setColor(NamedTextColor.BLUE);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 11)
        {
            if (this.chat.getOwner().getSara().level < Sara.$100YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.RED);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 12)
        {
            if (this.chat.getOwner().getSara().level < Sara.$100YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.GREEN);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 13)
        {
            if (this.chat.getOwner().getSara().level < Sara.$500YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.YELLOW);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 14)
        {
            if (this.chat.getOwner().getSara().level < Sara.$500YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.LIGHT_PURPLE);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 15)
        {
            if (this.chat.getOwner().getSara().level < Sara.$1000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.AQUA);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 16)
        {
            if (this.chat.getOwner().getSara().level < Sara.$1000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.DARK_AQUA);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 19)
        {
            if (this.chat.getOwner().getSara().level < Sara.$2000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.DARK_GREEN);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 20)
        {
            if (this.chat.getOwner().getSara().level < Sara.$2000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.DARK_RED);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 21)
        {
            if (this.chat.getOwner().getSara().level < Sara.$5000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.DARK_PURPLE);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 22)
        {
            if (this.chat.getOwner().getSara().level < Sara.$5000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.GOLD);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 23)
        {
            if (this.chat.getOwner().getSara().level < Sara.$10000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.GRAY);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 24)
        {
            if (this.chat.getOwner().getSara().level < Sara.$10000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.WHITE);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 25)
        {
            if (this.chat.getOwner().getSara().level < Sara.$10000YEN.level)
            {
                this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                return;
            }

            this.chat.setColor(NamedTextColor.BLACK);
            this.player.playSound(this.player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
            new ChatColorUI(this.player, this.chat);
        }

        if (event.getSlot() == 39)
        {
            new ChatSettingsUI(this.player, this.chat);
        }

        if (event.getSlot() == 40)
        {
            this.player.closeInventory();
        }
    }
}
