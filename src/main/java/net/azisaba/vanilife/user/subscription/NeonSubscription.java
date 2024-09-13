package net.azisaba.vanilife.user.subscription;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Product
@SingletonSubscription
public class NeonSubscription implements ISubscription
{
    @Override
    public @NotNull String getName()
    {
        return "neon";
    }

    @Override
    public @NotNull String getDisplayName()
    {
        return "Neon";
    }

    @Override
    public @NotNull Material getFavicon()
    {
        return Material.OAK_SIGN;
    }

    @Override
    public @NotNull List<String> getDescription()
    {
        return List.of("ばにらいふ！のチャット内で装飾コードを", "利用できるようになります");
    }

    @Override
    public @NotNull List<Component> getDetails()
    {
        return List.of(Component.text("「&」記号を使用したチャットの装飾:"), Component.text(this.getCost() + " Mola").color(NamedTextColor.GREEN));
    }

    @Override
    public int getCost()
    {
        return 1000;
    }
}
