package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.util.SeasonUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorldsUI extends InventoryUI
{
    private final BukkitRunnable runnable;

    private boolean b;

    public WorldsUI(@NotNull Player player)
    {
        super(player, Bukkit.createInventory(null, 36, Language.translate("ui.worlds.title", player)));

        int year = Calendar.getInstance().get(Calendar.YEAR);


        VanilifeWorld spring = VanilifeWorld.getInstance(year + "-spring");
        ItemStack springStack = this.getItemStack(spring, SeasonUtility.Season.SPRING);
        this.registerListener(10, springStack, spring);

        VanilifeWorld summer = VanilifeWorld.getInstance(year + "-summer");
        ItemStack summerStack = this.getItemStack(summer, SeasonUtility.Season.SUMMER);
        this.registerListener(12, summerStack, summer);

        VanilifeWorld fall = VanilifeWorld.getInstance(year + "-fall");
        ItemStack fallStack = this.getItemStack(fall, SeasonUtility.Season.FALL);
        this.registerListener(14, fallStack, fall);

        VanilifeWorld winter = VanilifeWorld.getInstance(year + "-winter");
        ItemStack winterStack = this.getItemStack(winter, SeasonUtility.Season.WINTER);
        this.registerListener(16, winterStack, winter);

        ItemStack closeStack = new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.displayName(Language.translate("ui.close", player).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        closeMeta.lore(List.of(Language.translate("ui.close.details", player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        closeStack.setItemMeta(closeMeta);
        this.inventory.setItem(31, closeStack);

        this.runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (spring != null && ! spring.contains(player))
                {
                    inventory.setItem(10, getItemStack(spring, SeasonUtility.Season.SPRING));
                }

                if (summer != null && ! summer.contains(player))
                {
                    inventory.setItem(12, getItemStack(summer, SeasonUtility.Season.SUMMER));
                }

                if (fall != null && ! fall.contains(player))
                {
                    inventory.setItem(14, getItemStack(fall, SeasonUtility.Season.FALL));
                }

                if (winter != null && ! winter.contains(player))
                {
                    inventory.setItem(16, getItemStack(winter, SeasonUtility.Season.WINTER));
                }

                b = ! b;
            }
        };

        this.runnable.runTaskTimer(Vanilife.getPlugin(), 0L, 10L);
    }

    private @NotNull ItemStack getItemStack(VanilifeWorld world, @NotNull SeasonUtility.Season season)
    {
        ItemStack worldStack = new ItemStack(SeasonUtility.getSeasonMaterial(season));
        ItemMeta worldMeta = worldStack.getItemMeta();
        worldMeta.displayName(Language.translate("ui.worlds." + season.name().toLowerCase(), this.player).color(SeasonUtility.getSeasonColor(season)).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        List<Component> worldLore = new ArrayList<>();

        worldLore.add(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(world == null ? "ui.worlds.state.close" : "ui.worlds.state.open", this.player).color(world == null ? NamedTextColor.RED : NamedTextColor.GREEN)));
        worldLore.add(Language.translate("ui.worlds.online", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text((world == null ? 0 : world.getOnline()) + " player(s)").color(world == null || world.getOnline() == 0 ? NamedTextColor.RED : NamedTextColor.GREEN)));

        if (world != null)
        {
            worldLore.add(Component.text().build());

            if (world.contains(this.player))
            {
                worldMeta.addEnchant(Enchantment.INFINITY, 1, false);
                worldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                worldLore.add(Language.translate("ui.worlds.playing", this.player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            }
            else
            {
                worldLore.add(Component.text(this.b ? "▶" : " ").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).append(Language.translate("ui.click-to-play", this.player)));
            }
        }

        worldMeta.lore(worldLore);
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

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);
        this.runnable.cancel();
    }
}
