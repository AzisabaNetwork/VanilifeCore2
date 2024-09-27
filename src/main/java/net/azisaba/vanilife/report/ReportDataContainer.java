package net.azisaba.vanilife.report;

import net.azisaba.vanilife.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportDataContainer
{
    private static final List<ReportDataContainer> instances = new ArrayList<>();

    public static ReportDataContainer getInstance(User user)
    {
        List<ReportDataContainer> filteredInstances = ReportDataContainer.instances.stream().filter(i -> i.getSender() == user).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static ReportDataContainer getInstance(Player player)
    {
        return ReportDataContainer.getInstance(User.getInstance(player));
    }

    private final User sender;
    private final User target;
    private final String details;

    private final List<Location> locations = new ArrayList<>();

    public ReportDataContainer(@NotNull User sender, User target, @NotNull String details)
    {
        this.sender = sender;
        this.target = target;
        this.details = details;

        ReportDataContainer.instances.add(this);
    }

    public @NotNull User getSender()
    {
        return this.sender;
    }

    public User getTarget()
    {
        return this.target;
    }

    public @NotNull String getDetails()
    {
        return this.details;
    }

    public @NotNull List<Location> getLocations()
    {
        return this.locations;
    }

    public void addLocation(@NotNull Location location)
    {
        if (this.contains(location))
        {
            return;
        }

        this.locations.add(location);
    }

    public void removeLocation(@NotNull Location location)
    {
        this.locations.removeIf(b -> b.equals(location));
    }

    public boolean hasTarget()
    {
        return this.target != null;
    }

    public boolean contains(Location location)
    {
        return this.locations.stream().anyMatch(b -> b.equals(location));
    }

    public void cancel()
    {
        ReportDataContainer.instances.remove(this);
    }
}
