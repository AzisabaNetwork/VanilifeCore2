package net.azisaba.vanilife.ui;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.housing.HousingScope;
import net.azisaba.vanilife.housing.HousingTime;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.HeadUtility;
import net.azisaba.vanilife.vwm.VanilifeWorld;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HousingUI extends InventoryUI
{
    private final Housing housing;
    private final User owner;

    public HousingUI(@NotNull Player player, @NotNull Housing housing)
    {
        super(player, Bukkit.createInventory(null, 45, Language.translate("ui.housing.title", player)));

        this.housing = housing;
        this.owner = this.housing.getUser();

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            ItemStack headStack = HeadUtility.getPlayerHead(this.owner.getPlaneName());
            ItemMeta headMeta = headStack.getItemMeta();
            headMeta.displayName(this.owner.getName().decoration(TextDecoration.ITALIC, false));

            ArrayList<Component> headLore = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            if (this.owner.getBio() != null)
            {
                for (int i = 0; i < this.owner.getBio().length(); i ++)
                {
                    sb.append(this.owner.getBio().charAt(i));

                    if (16 <= sb.length())
                    {
                        headLore.add(Component.text(sb.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                        sb = new StringBuilder();
                    }
                }

                if (sb.length() < 16)
                {
                    headLore.add(Component.text(sb.toString()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                }

                headLore.add(Component.text(""));
            }

            headLore.add(Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                    .append((this.owner.isOnline() ? Language.translate("ui.profile.online", this.player) : Language.translate("ui.profile.offline", this.player)).color(this.owner.isOnline() ? NamedTextColor.GREEN : NamedTextColor.RED)));

            if (! this.owner.isOnline())
            {
                headLore.add(Language.translate("ui.profile.last-login", this.player).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).append(Component.text(Vanilife.sdf3.format(this.owner.getLastLogin())).color(NamedTextColor.GREEN)));
            }

            if (this.owner.getSettings().BIRTHDAY.isWithinScope(User.getInstance(player)) && this.owner.getBirthday() != null)
            {
                headLore.add(Language.translate("ui.profile.birthday", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(Vanilife.sdf4.format(this.owner.getBirthday())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
            }

            headMeta.lore(headLore);

            headStack.setItemMeta(headMeta);
            inventory.setItem(4, headStack);
        });

        ItemStack spawnStack = new ItemStack(Material.ENDER_EYE);
        ItemMeta spawnMeta = spawnStack.getItemMeta();
        spawnMeta.displayName(Language.translate("ui.housing.spawn", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        spawnMeta.lore(List.of(Language.translate("ui.housing.spawn.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        spawnStack.setItemMeta(spawnMeta);
        this.inventory.setItem(20, spawnStack);

        final boolean afk = this.owner.read("settings.housing.afk").getAsBoolean();

        ItemStack afkStack = new ItemStack(Material.STRING);
        ItemMeta afkMeta = afkStack.getItemMeta();
        afkMeta.displayName(Language.translate("ui.housing.afk", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        afkMeta.lore(List.of(Language.translate("ui.housing.afk.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(afk ? "ui.true" : "ui.false", this.player).color(afk ? NamedTextColor.GREEN : NamedTextColor.RED))));
        afkStack.setItemMeta(afkMeta);
        this.inventory.setItem(21, afkStack);

        ItemStack blockStack = new ItemStack(Material.BRICKS);
        ItemMeta blockMeta = blockStack.getItemMeta();
        blockMeta.displayName(Language.translate("ui.housing.block", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        blockMeta.lore(List.of(Language.translate("ui.housing.block.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        blockStack.setItemMeta(blockMeta);
        this.inventory.setItem(22, blockStack);

        ItemStack timeStack = new ItemStack(Material.CLOCK);
        ItemMeta timeMeta = timeStack.getItemMeta();
        timeMeta.displayName(Language.translate("ui.housing.time", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        timeMeta.lore(List.of(Language.translate("ui.housing.time.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.housing.time.day", this.player).color(this.housing.getTime() == HousingTime.DAY ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.time.noon", this.player).color(this.housing.getTime() == HousingTime.NOON ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.time.night", this.player).color(this.housing.getTime() == HousingTime.NIGHT ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.time.midnight", this.player).color(this.housing.getTime() == HousingTime.MIDNIGHT ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        timeStack.setItemMeta(timeMeta);
        this.inventory.setItem(23, timeStack);

        final boolean activity = this.owner.read("settings.housing.activity").getAsBoolean();

        ItemStack activityStack = new ItemStack(Material.LEVER);
        ItemMeta activityMeta = activityStack.getItemMeta();
        activityMeta.displayName(Language.translate("ui.housing.activity", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        activityMeta.lore(List.of(Language.translate("ui.housing.activity.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.state", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Language.translate(activity ? "ui.true" : "ui.false", this.player).color(activity ? NamedTextColor.GREEN : NamedTextColor.RED))));
        activityStack.setItemMeta(activityMeta);
        this.inventory.setItem(24, activityStack);

        ItemStack visitScopeStack = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta visitScopeMeta = visitScopeStack.getItemMeta();
        visitScopeMeta.displayName(Language.translate("ui.housing.scope.visit", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        visitScopeMeta.lore(List.of(Language.translate("ui.housing.scope.visit.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.housing.scope.public", this.player).color(this.housing.getVisitScope() == HousingScope.PUBLIC ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.friend", this.player).color(this.housing.getVisitScope() == HousingScope.FRIEND ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.osatou", this.player).color(this.housing.getVisitScope() == HousingScope.OSATOU ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.private", this.player).color(this.housing.getVisitScope() == HousingScope.PRIVATE ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        visitScopeStack.setItemMeta(visitScopeMeta);
        this.inventory.setItem(30, visitScopeStack);

        ItemStack buildScopeStack = new ItemStack(Material.BREWING_STAND);
        ItemMeta buildScopeMeta = buildScopeStack.getItemMeta();
        buildScopeMeta.displayName(Language.translate("ui.housing.scope.build", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        buildScopeMeta.lore(List.of(Language.translate("ui.housing.scope.build.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.housing.scope.public", this.player).color(this.housing.getBuildScope() == HousingScope.PUBLIC ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.friend", this.player).color(this.housing.getBuildScope() == HousingScope.FRIEND ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.osatou", this.player).color(this.housing.getBuildScope() == HousingScope.OSATOU ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                Language.translate("ui.housing.scope.private", this.player).color(this.housing.getBuildScope() == HousingScope.PRIVATE ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        buildScopeStack.setItemMeta(buildScopeMeta);
        this.inventory.setItem(31, buildScopeStack);

        ItemStack worldsStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta worldsMeta = (SkullMeta) worldsStack.getItemMeta();
        worldsMeta.displayName(Language.translate("ui.housing.worlds", this.player).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        worldsMeta.lore(List.of(Language.translate("ui.housing.worlds.details", this.player).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text().build(),
                Language.translate("ui.housing.worlds.help", this.player).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        worldsMeta.setPlayerProfile(HeadUtility.getPlayerProfile("http://textures.minecraft.net/texture/f9e986ccac3dc804f1bebe054dfb3e800480b7e08b2e7c6a86c84621c756c142"));
        worldsStack.setItemMeta(worldsMeta);
        this.registerListener(32, worldsStack, "vanilife:worlds", ExecutionType.CLIENT);
    }

    @Override
    public void onUiClick(@NotNull InventoryClickEvent event)
    {
        super.onUiClick(event);

        if (event.getCurrentItem() == null)
        {
            return;
        }

        this.player.playSound(this.player, Sound.UI_BUTTON_CLICK, 0.8F, 1.2F);

        if (event.getSlot() == 20)
        {
            this.player.closeInventory();

            this.housing.setSpawn(this.player.getLocation());
            this.player.sendMessage(Language.translate("ui.housing.spawn.changed", this.player).color(NamedTextColor.GREEN));
            this.player.playSound(this.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        }

        if (event.getSlot() == 21)
        {
            boolean afk = this.owner.read("settings.housing.afk").getAsBoolean();
            this.owner.write("settings.housing.afk", ! afk);
            new HousingUI(this.player, this.housing);
        }

        if (event.getSlot() == 22)
        {
            new HousingBlockUI(this.player);
        }

        if (event.getSlot() == 23)
        {
            this.housing.setTime(switch (this.housing.getTime())
            {
                case DAY -> HousingTime.NOON;
                case NOON -> HousingTime.NIGHT;
                case NIGHT -> HousingTime.MIDNIGHT;
                case MIDNIGHT -> HousingTime.DAY;
            });

            new HousingUI(this.player, this.housing);
        }

        if (event.getSlot() == 24)
        {
            boolean activity = this.owner.read("settings.housing.activity").getAsBoolean();
            this.owner.write("settings.housing.activity", ! activity);
            new HousingUI(this.player, this.housing);
        }

        if (event.getSlot() == 30)
        {
            this.housing.setVisitScope(switch (this.housing.getVisitScope())
            {
                case HousingScope.PUBLIC -> HousingScope.FRIEND;
                case HousingScope.FRIEND -> HousingScope.OSATOU;
                case HousingScope.OSATOU -> HousingScope.PRIVATE;
                case HousingScope.PRIVATE -> HousingScope.PUBLIC;
            });

            for (Player player : Bukkit.getOnlinePlayers().stream().filter(p -> Housing.getInstance(p.getLocation()) == this.housing).toList())
            {
                if (! this.housing.canVisit(User.getInstance(player)))
                {
                    VanilifeWorld world = VanilifeWorld.getInstance(VanilifeWorldManager.getLatestVersion());

                    if (world == null)
                    {
                        continue;
                    }

                    Bukkit.dispatchCommand(player, "world " + world.getName());
                }
            }

            new HousingUI(this.player, this.housing);
        }

        if (event.getSlot() == 31)
        {
            this.housing.setBuildScope(switch (this.housing.getBuildScope())
            {
                case HousingScope.PUBLIC -> HousingScope.FRIEND;
                case HousingScope.FRIEND -> HousingScope.OSATOU;
                case HousingScope.OSATOU -> HousingScope.PRIVATE;
                case HousingScope.PRIVATE -> HousingScope.PUBLIC;
            });

            new HousingUI(this.player, this.housing);
        }
    }
}
