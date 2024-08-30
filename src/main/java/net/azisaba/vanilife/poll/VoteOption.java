package net.azisaba.vanilife.poll;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteOption
{
    private final String name;

    private final List<Player> voters = new ArrayList<>();

    public VoteOption(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Player> getVoters()
    {
        return this.voters;
    }

    public boolean isVoter(Player player)
    {
        return this.voters.contains(player);
    }

    public void vote(Player voter)
    {
        if (! this.voters.contains(voter))
        {
            this.voters.add(voter);
        }
    }

    public void unVote(Player player)
    {
        this.voters.remove(player);
    }
}
