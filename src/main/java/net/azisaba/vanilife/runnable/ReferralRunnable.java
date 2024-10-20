package net.azisaba.vanilife.runnable;

import net.azisaba.vanilife.user.referral.Referral;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class ReferralRunnable extends BukkitRunnable
{
    @Override
    public void run()
    {
        Date now = new Date();
        Referral.getInstances().stream()
                .filter(ref -> ref.getExpiration().before(now))
                .forEach(Referral::delete);
    }
}
