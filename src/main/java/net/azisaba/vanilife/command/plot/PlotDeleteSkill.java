package net.azisaba.vanilife.command.plot;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.command.skill.ICommandSkill;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.Sara;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Typing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotDeleteSkill implements ICommandSkill
{
    @Override
    public String getName()
    {
        return "delete";
    }

    @Override
    public Sara getRequirement()
    {
        return Sara.DEFAULT;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! (sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Please run this from within the game.").color(NamedTextColor.RED));
            return;
        }

        if (args.length != 0)
        {
            sender.sendMessage(Component.text("Correct syntax: /plot delete").color(NamedTextColor.RED));
            return;
        }

        Plot plot = Plot.getInstance(player.getChunk());

        if (plot == null)
        {
            sender.sendMessage(Component.text("Plot が見つかりませんでした").color(NamedTextColor.RED));
            return;
        }

        User user = User.getInstance(player);

        if (user != plot.getOwner())
        {
            sender.sendMessage(Component.text("あなたはこの Plot のオーナーではありません").color(NamedTextColor.RED));
            return;
        }

        new Typing(player)
        {
            private String confirmCode;

            @Override
            public void init()
            {
                this.confirmCode = this.getConfirmCode(6);
                this.player.sendMessage(Component.text(String.format("確認: %s をチャットに送信してください", this.confirmCode)).color(NamedTextColor.YELLOW));
            }

            @Override
            public void onTyped(String string)
            {
                super.onTyped(string);

                if (string.equals(this.confirmCode))
                {
                    plot.delete();
                    sender.sendMessage(Component.text(String.format("Plot %s を削除しました", plot.getName())).color(NamedTextColor.GREEN));
                }
                else
                {
                    sender.sendMessage(Component.text("Plot の削除をキャンセルしました").color(NamedTextColor.GREEN));
                }
            }

            public String getConfirmCode(int length)
            {
                if (length < 1)
                {
                    length = 1;
                }

                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < length; i ++)
                {
                    sb.append(characters.charAt(Vanilife.random.nextInt(characters.length())));
                }

                return sb.toString();
            }
        };
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
