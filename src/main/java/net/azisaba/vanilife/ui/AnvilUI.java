package net.azisaba.vanilife.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class AnvilUI extends InventoryUI
{
    private final float exp;

    public AnvilUI(@NotNull Player player, @NotNull Component title)
    {
        super(player);

        this.exp = this.player.getExp();
        this.player.setLevel(1);

        this.player.setLevel(1);

        EntityPlayer nmsPlayer = ((CraftPlayer) this.player).getHandle();
        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer);
        nmsPlayer.cd = nmsPlayer.cc;

        int containerId = nmsPlayer.nextContainerCounter();

        ContainerAnvil anvil = new ContainerAnvil(containerId, nmsPlayer.fY(), ContainerAccess.a(nmsPlayer.dO(), BlockPosition.d(BlockPosition.a(0, 0, 0))));
        anvil.checkReachable = false;
        anvil.setTitle(IChatBaseComponent.c(LegacyComponentSerializer.legacySection().serialize(title)));

        Inventory inventory = anvil.getBukkitView().getTopInventory();
        inventory.setItem(0, this.getDummy());

        nmsPlayer.c.sendPacket(new PacketPlayOutOpenWindow(anvil.j, anvil.a(), anvil.getTitle()));

        nmsPlayer.cd = anvil;
        nmsPlayer.a(anvil);
    }

    @Override
    public Inventory getInventory()
    {
        Inventory i = this.player.getOpenInventory().getTopInventory();
        System.out.println(i.getType());
        return i.getType() == InventoryType.ANVIL ? i : null;
    }

    @NotNull
    public ItemStack getDummy()
    {
        ItemStack dummyStack = new ItemStack(Material.PAPER);
        ItemMeta dummyMeta = dummyStack.getItemMeta();
        dummyMeta.displayName(Component.text("_"));
        dummyMeta.setHideTooltip(true);
        dummyStack.setItemMeta(dummyMeta);
        return dummyStack;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event)
    {
        event.setCancelled(true);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        if (event.getSlot() == 2)
        {
            this.player.closeInventory();
        }
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event)
    {
        event.setCancelled(true);
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event)
    {
        super.onClose(event);

        this.player.setExp(this.exp);

        ItemStack stack = this.getInventory().getItem(2);
        this.getInventory().clear();

        if (stack == null)
        {
            return;
        }

        TextComponent component = (TextComponent) stack.getItemMeta().displayName();

        if (component == null)
        {
            return;
        }

        this.onTyped(component.content());
    }

    protected void onTyped(@NotNull String string) {}
}
