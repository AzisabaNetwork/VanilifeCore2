package net.azisaba.vanilife.ui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.user.Skin;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.util.MojangAPI;
import net.azisaba.vanilife.util.Typing;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinUI extends InventoryUI
{
    private final User user;

    private final Map<Integer, Skin> skinMap = new HashMap<>();

    public SkinUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 36, Language.translate("ui.skin.title", player)));

        this.user = User.getInstance(player);

        final int[] slots = {10, 11, 12, 13, 14, 15, 16};

        for (int i = 0; i < slots.length; i ++)
        {
            if (i < this.user.getSkins().size())
            {
                Skin skin = this.user.getSkins().get(i);

                ItemStack skinStack = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skinMeta = (SkullMeta) skinStack.getItemMeta();
                skinMeta.displayName(Component.text(skin.getName()).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                skinMeta.lore(List.of(Language.translate("ui.skin.left", this.player).decoration(TextDecoration.ITALIC, false),
                        Language.translate("ui.skin.right", this.player).decoration(TextDecoration.ITALIC, false)));
                skinMeta.setPlayerProfile(HeadUtility.getPlayerProfile(MojangAPI.getSkin(skin.getTexture())));

                if (this.user.getSkin() == skin)
                {
                    skinMeta.addEnchant(Enchantment.INFINITY, 1, false);
                    skinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                skinStack.setItemMeta(skinMeta);

                this.skinMap.put(slots[i], skin);
                this.inventory.setItem(slots[i], skinStack);
                continue;
            }

            ItemStack emptyStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta emptyMeta = (SkullMeta) emptyStack.getItemMeta();
            emptyMeta.displayName(Language.translate("ui.skin.empty", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            emptyMeta.setPlayerProfile(HeadUtility.getPlayerProfile("http://textures.minecraft.net/texture/ffccfe5096a335b9ab78ab4f778ae499f4ccab4e2c95fa349227fd060759baaf"));
            emptyStack.setItemMeta(emptyMeta);
            this.inventory.setItem(slots[i], emptyStack);
        }
        
        ItemStack uploadStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta uploadMeta = (SkullMeta) uploadStack.getItemMeta();
        uploadMeta.displayName(Language.translate("ui.skin.upload", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        uploadMeta.lore(List.of(Language.translate("ui.skin.upload.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        uploadMeta.setOwningPlayer(this.player);
        uploadStack.setItemMeta(uploadMeta);

        if (Skin.getInstance(this.player) == null && this.user.getSkins().size() < 7)
        {
            this.inventory.setItem(30, uploadStack);
        }

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", this.player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(31, closeStack);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        Skin skin = this.skinMap.get(event.getSlot());

        if (skin != null)
        {
            if (event.isLeftClick())
            {
                Bukkit.dispatchCommand(this.player, "vanilife:skin " + skin.getName());
            }
            else if (event.isRightClick() && event.isShiftClick())
            {
                skin.delete();
                this.player.sendMessage(Language.translate("ui.skin.deleted", this.player).color(NamedTextColor.GREEN));
                this.player.closeInventory();
            }
        }

        if (event.getSlot() == 30)
        {
            new Typing(this.player)
            {
                @Override
                public void init()
                {
                    this.player.sendMessage(Language.translate("ui.skin.pls-send-name", this.player).color(NamedTextColor.GREEN));
                    this.player.sendMessage(Language.translate("ui.skin.pls-send-name.details", this.player).color(NamedTextColor.YELLOW));
                }

                @Override
                public void onTyped(String string)
                {
                    super.onTyped(string);

                    if (string.equals(":"))
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.cancelled", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    if (string.length() < 3)
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.limit-under", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    if (8 < string.length())
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.limit-over", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    if (user.getSkins().stream().anyMatch(skin -> skin.getName().equals(string)))
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.already", this.player).color(NamedTextColor.RED));
                        this.player.playSound(this.player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0.1f);
                        return;
                    }

                    PlayerProfile profile = this.player.getPlayerProfile();

                    if (profile.getProperties().stream().noneMatch(property -> property.getName().equals("textures")))
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.error", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    ProfileProperty textures = profile.getProperties().stream().filter(property -> property.getName().equals("textures")).toList().getFirst();

                    if (! textures.isSigned())
                    {
                        this.player.sendMessage(Language.translate("ui.skin.pls-send-name.error", this.player).color(NamedTextColor.RED));
                        return;
                    }

                    user.addSkin(new Skin(string, user, textures.getValue(), textures.getSignature()));
                    this.player.sendMessage(Language.translate("ui.skin.pls-send-name.uploaded", this.player).color(NamedTextColor.GREEN));
                    this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                }
            };
        }

        if (event.getSlot() == 31)
        {
            this.player.closeInventory();
        }
    }
}
