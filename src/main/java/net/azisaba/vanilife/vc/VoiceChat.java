package net.azisaba.vanilife.vc;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.user.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class VoiceChat
{
    private static final List<VoiceChat> instances = new ArrayList<>();

    public static VoiceChat getInstance(VoiceChannel channel)
    {
        List<VoiceChat> filteredInstances = VoiceChat.instances.stream().filter(i -> i.getChannel() == channel).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VoiceChat getInstance(User user)
    {
        List<VoiceChat> filteredInstances = VoiceChat.instances.stream().filter(i -> i.isMember(user)).toList();
        return filteredInstances.isEmpty() ? null : filteredInstances.getFirst();
    }

    public static VoiceChat getInstance(Player player)
    {
        return VoiceChat.getInstance(User.getInstance(player));
    }

    public static List<VoiceChat> getInstances()
    {
        return VoiceChat.instances;
    }

    @NotNull
    public static VoiceChat search(@NotNull Player player)
    {
        VoiceChat currentVoiceChant = VoiceChat.getInstance(player);
        VoiceChat voiceChat = null;
        double distance = -1;

        for (VoiceChat vc : VoiceChat.getInstances().stream().filter(i -> i.getLocation().getWorld() == player.getWorld()).toList())
        {
            if (vc == currentVoiceChant && currentVoiceChant.getMembers().size() == 1)
            {
                continue;
            }

            double distance2 = player.getLocation().distance(vc.getLocation());

            if (distance == -1)
            {
                distance = distance2;
            }

            if (distance2 <= distance)
            {
                voiceChat = vc;
                distance = distance2;
            }
        }

        if (40 < distance || voiceChat == null)
        {
            return currentVoiceChant != null && currentVoiceChant.getMembers().size() == 1 ? currentVoiceChant : new VoiceChat();
        }

        return voiceChat;
    }

    @NotNull
    private static String name()
    {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345678";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i ++)
        {
            sb.append(characters.charAt(Vanilife.random.nextInt(characters.length())));
        }

        return "VC-" + sb;
    }

    private final String name = VoiceChat.name();
    private final AudioChannel channel = Vanilife.SERVER_PUBLIC.createVoiceChannel(this.name, Vanilife.category).complete();

    private final List<User> members = new ArrayList<>();

    public VoiceChat()
    {
        this.channel.getManager().putPermissionOverride(Vanilife.SERVER_PUBLIC.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)).queue();

        VoiceChat.instances.add(this);
    }

    public String getName()
    {
        return this.name;
    }

    public AudioChannel getChannel()
    {
        return this.channel;
    }

    public List<User> getMembers()
    {
        return this.members;
    }

    public Location getLocation()
    {
        if (this.members.isEmpty())
        {
            return null;
        }

        double xSum = 0;
        double ySum = 0;
        double zSum = 0;

        for (User user : this.members)
        {
            Location location = user.getAsPlayer().getLocation();

            xSum += location.getX();
            ySum += location.getY();
            zSum += location.getZ();
        }

        double xAverage = xSum / this.members.size();
        double yAverage = ySum / this.members.size();
        double zAverage = zSum / this.members.size();

        return new Location(this.members.getFirst().getAsPlayer().getWorld(), xAverage, yAverage, zAverage);
    }

    public boolean isMember(User user)
    {
        return this.members.contains(user);
    }

    public boolean isMember(Player player)
    {
        return this.isMember(User.getInstance(player));
    }

    public void connect(@NotNull User user)
    {
        if (user.getDiscord() == null)
        {
            return;
        }

        if (! user.isOnline())
        {
            return;
        }

        if (! this.members.isEmpty() && this.members.getFirst().getAsPlayer().getWorld() != user.getAsPlayer().getWorld())
        {
            return;
        }

        this.members.add(user);

        Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
            Member member = Vanilife.SERVER_PUBLIC.retrieveMemberById(user.getDiscord().getId()).complete();
            Vanilife.SERVER_PUBLIC.moveVoiceMember(member, this.channel).queue();
        });
    }

    public void disconnect(@NotNull User user)
    {
        if (! this.isMember(user))
        {
            return;
        }

        this.members.remove(user);

        if (this.members.isEmpty())
        {
            VoiceChat.instances.remove(this);

            Bukkit.getScheduler().runTaskAsynchronously(Vanilife.getPlugin(), () -> {
                this.channel.delete().queue();
            });
        }
    }

    public void delete()
    {
        List<User> members = new ArrayList<>(this.members);
        members.forEach(this::disconnect);
    }
}
