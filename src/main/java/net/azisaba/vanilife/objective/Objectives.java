package net.azisaba.vanilife.objective;

import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Registry;
import org.jetbrains.annotations.NotNull;

public class Objectives
{
    public static final Registry<Objective> registry = new Registry<>();

    public static final SendMessageObjective SEND_MESSAGE = (SendMessageObjective) Objectives.registry.register("send_message", new SendMessageObjective());

    public static final MakeFriendObjective MAKE_FRIEND = (MakeFriendObjective) Objectives.registry.register("make_friend", new MakeFriendObjective());

    public static final LinkDiscordObjective LINK_DISCORD = (LinkDiscordObjective) Objectives.registry.register("link_discord", new LinkDiscordObjective());

    public static final StartHousingObjective START_HOUSING = (StartHousingObjective) Objectives.registry.register("start_housing", new StartHousingObjective());

    public static int getLevel(@NotNull User user)
    {
        int level = -1;

        for (Objective objective : user.getAchievements())
        {
            level = Math.max(objective.getLevel(), level);
        }

        return level;
    }

    public static Objective next(@NotNull User user)
    {
        final int level = Objectives.getLevel(user);

        Objective next = null;

        for (Objective objective : Objectives.registry.values())
        {
            if (level < objective.getLevel() && (next == null || objective.getLevel() < next.getLevel()))
            {
                next = objective;
            }
        }

        return next;
    }
}
