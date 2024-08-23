package net.azisaba.vanilife.user.settings.setting;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.ui.SettingsUI;
import net.azisaba.vanilife.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScopeSetting implements ISetting
{
    protected User user;
    protected Scope scope = Scope.PUBLIC;

    @Override
    public void init(User user)
    {
        this.user = user;

        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement(String.format("SELECT %s FROM settings WHERE user = ?", this.getName()));
            stmt.setString(1, this.user.getId().toString());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            this.scope = Scope.valueOf(rs.getString(this.getName()));

            rs.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to get settings record: %s", e.getMessage())).color(NamedTextColor.RED));
        }
    }

    @Override
    public void save()
    {
        try
        {
            Connection con = DriverManager.getConnection(Vanilife.DB_URL, Vanilife.DB_USER, Vanilife.DB_PASS);
            PreparedStatement stmt = con.prepareStatement(String.format("UPDATE settings SET %s = ? WHERE user = ?", this.getName()));
            stmt.setString(1, this.scope.toString());
            stmt.setString(2, this.user.getId().toString());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            Vanilife.getPluginLogger().error(Component.text(String.format("Failed to update settings record: %s", e.getMessage())).color(NamedTextColor.RED));
        }

        Player player = this.user.getAsOfflinePlayer().getPlayer();

        if (player != null)
        {
            new SettingsUI(player, this.user.getSettings());
        }
    }

    public ArrayList<Component> getLore()
    {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(Scope.PUBLIC.name.color((this.scope == Scope.PUBLIC) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Scope.FRIEND.name.color((this.scope == Scope.FRIEND) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Scope.PRIVATE.name.color((this.scope == Scope.PRIVATE) ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));

        return lore;
    }

    public ArrayList<Component> getLore(Component... components)
    {

        ArrayList<Component> lore = new ArrayList<>(List.of(components));

        lore.add(Component.text(""));
        lore.addAll(this.getLore());

        return lore;
    }

    public boolean isWithinScope(User user)
    {
        return (this.scope == Scope.PUBLIC)
                || (this.scope == Scope.FRIEND && this.user.isFriend(user))
                || this.user == user;
    }

    @Override
    public void onClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
        this.save();
    }

    @Override
    public void onLeftClick(InventoryClickEvent event)
    {
        this.scope = switch (this.scope)
        {
            case PUBLIC -> Scope.FRIEND;
            case FRIEND -> Scope.PRIVATE;
            case PRIVATE -> Scope.PUBLIC;
        };

        this.save();
    }

    @Override
    public void onRightClick(InventoryClickEvent event)
    {
        this.scope = switch (this.scope)
        {
            case PUBLIC -> Scope.PRIVATE;
            case FRIEND -> Scope.PUBLIC;
            case PRIVATE -> Scope.FRIEND;
        };

        this.save();
    }

    public enum Scope
    {
        PUBLIC(Component.text("公開")),
        FRIEND(Component.text("Friend に公開")),
        PRIVATE(Component.text("非公開"));

        private final Component name;

        Scope(Component name)
        {
            this.name = name;
        }
    }
}
