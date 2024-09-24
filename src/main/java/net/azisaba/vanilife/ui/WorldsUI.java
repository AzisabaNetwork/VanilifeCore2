package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.util.SeasonUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldsUI extends InventoryUI
{
    private int page = 0;

    public WorldsUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 36, Language.translate("ui.worlds.title", player)));

        final VanilifeWorld spring = VanilifeWorld.getInstance(switch (SeasonUtility.getSeason())
        {
            case SeasonUtility.Season.SPRING -> VanilifeWorldManager.getLatestVersion();
            case SeasonUtility.Season.SUMMER -> VanilifeWorldManager.getLatestVersion() - 1;
            case SeasonUtility.Season.FALL -> VanilifeWorldManager.getLatestVersion() - 2;
            case SeasonUtility.Season.WINTER -> VanilifeWorldManager.getLatestVersion() - 3;
        });

        final VanilifeWorld summer = VanilifeWorld.getInstance(switch (SeasonUtility.getSeason())
        {
            case SeasonUtility.Season.SPRING -> VanilifeWorldManager.getLatestVersion() - 3;
            case SeasonUtility.Season.SUMMER -> VanilifeWorldManager.getLatestVersion();
            case SeasonUtility.Season.FALL -> VanilifeWorldManager.getLatestVersion() - 1;
            case SeasonUtility.Season.WINTER -> VanilifeWorldManager.getLatestVersion() - 2;
        });

        final VanilifeWorld fall = VanilifeWorld.getInstance(switch (SeasonUtility.getSeason())
        {
            case SeasonUtility.Season.SPRING -> VanilifeWorldManager.getLatestVersion() - 2;
            case SeasonUtility.Season.SUMMER -> VanilifeWorldManager.getLatestVersion() - 3;
            case SeasonUtility.Season.FALL -> VanilifeWorldManager.getLatestVersion();
            case SeasonUtility.Season.WINTER -> VanilifeWorldManager.getLatestVersion() - 1;
        });

        final VanilifeWorld winter = VanilifeWorld.getInstance(switch (SeasonUtility.getSeason())
        {
            case SeasonUtility.Season.SPRING -> VanilifeWorldManager.getLatestVersion() - 1;
            case SeasonUtility.Season.SUMMER -> VanilifeWorldManager.getLatestVersion() - 2;
            case SeasonUtility.Season.FALL -> VanilifeWorldManager.getLatestVersion() - 3;
            case SeasonUtility.Season.WINTER -> VanilifeWorldManager.getLatestVersion();
        });

        this.registerListener(10, this.getItemStack(spring, SeasonUtility.Season.SPRING), spring);
        this.registerListener(12, this.getItemStack(summer, SeasonUtility.Season.SUMMER), summer);
        this.registerListener(14, this.getItemStack(fall, SeasonUtility.Season.FALL), fall);
        this.registerListener(16, this.getItemStack(winter, SeasonUtility.Season.WINTER), winter);

        ItemStack closeStack = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(31, closeStack);
    }

    protected @NotNull ItemStack getItemStack(VanilifeWorld world, @NotNull SeasonUtility.Season season)
    {
        ItemStack worldStack = new ItemStack(SeasonUtility.getSeasonMaterial(season));
        ItemMeta worldMeta = worldStack.getItemMeta();
        worldMeta.displayName(Language.translate("ui.worlds." + season.name().toLowerCase(), this.player).color(SeasonUtility.getSeasonColor(season)).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        List<Component> springLore = new ArrayList<>();

        springLore.add(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(world == null ? "ui.worlds.state.close" : "ui.worlds.state.open", this.player).color(world == null ? NamedTextColor.RED : NamedTextColor.GREEN)));
        springLore.add(Language.translate("ui.worlds.online", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text((world == null ? 0 : world.getOnline()) + " player(s)").color(world == null || world.getOnline() == 0 ? NamedTextColor.RED : NamedTextColor.GREEN)));

        if (world != null)
        {
            springLore.add(Component.text().build());

            if (world.contains(this.player))
            {
                springLore.add(Language.translate("ui.worlds.playing", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }
            else
            {
                springLore.add(Language.translate("ui.click-to-play", this.player).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            }
        }

        worldMeta.lore(springLore);
        worldStack.setItemMeta(worldMeta);
        return worldStack;
    }

    protected void registerListener(int index, @NotNull ItemStack stack, VanilifeWorld world)
    {
        if (world == null)
        {
            this.inventory.setItem(index, stack);
            return;
        }

        super.registerListener(index, stack, "vanilife:world " + world.getName(), ExecutionType.CLIENT);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getSlot() == 31)
        {
            this.player.closeInventory();
        }
    }
}
