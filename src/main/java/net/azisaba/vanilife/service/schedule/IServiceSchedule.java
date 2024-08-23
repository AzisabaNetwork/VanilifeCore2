package net.azisaba.vanilife.service.schedule;

import net.azisaba.vanilife.service.Service;
import org.bukkit.configuration.ConfigurationSection;

public interface IServiceSchedule
{
    String getType();

    void start(Service service, ConfigurationSection config);
}
