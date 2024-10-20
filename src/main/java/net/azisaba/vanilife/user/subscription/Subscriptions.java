package net.azisaba.vanilife.user.subscription;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions
{
    private static final List<ISubscription> registry = new ArrayList<>();

    private static final List<ISubscription> products = new ArrayList<>();

    public static final FireworkEmote FIREWORKS_EMOTE = (FireworkEmote) Subscriptions.register(new FireworkEmote());

    public static final CreeperEmote CREEPER_EMOTE = (CreeperEmote) Subscriptions.register(new CreeperEmote());

    public static final ThinkingEmote THINKING_EMOTE = (ThinkingEmote) Subscriptions.register(new ThinkingEmote());

    public static final AngryEmote ANGRY_EMOTE = (AngryEmote) Subscriptions.register(new AngryEmote());

    public static final HeartEmote HEART_EMOTE = (HeartEmote) Subscriptions.register(new HeartEmote());

    public static final NeonSubscription NEON = (NeonSubscription) Subscriptions.register(new NeonSubscription());

    public static final RainEmote RAIN_EMOTE = (RainEmote) Subscriptions.register(new RainEmote());

    public static final RichEmote RICH_EMOTE = (RichEmote) Subscriptions.register(new RichEmote());

    public static final SadnessEmote SADNESS_EMOTE = (SadnessEmote) Subscriptions.register(new SadnessEmote());

    private static ISubscription register(@NotNull ISubscription subscription)
    {
        if (Subscriptions.valueOf(subscription.getName()) != null)
        {
            throw new IllegalArgumentException("Subscription name " + subscription.getName() + " is already in use.");
        }

        if (! subscription.getClass().isAnnotationPresent(SingletonSubscription.class))
        {
            throw new IllegalArgumentException(subscription.getClass().getName() + " is not an implementation of Singleton Subscription.");
        }

        if (subscription.getClass().isAnnotationPresent(Product.class))
        {
            int priority = subscription.getClass().getAnnotation(Product.class).priority();

            for (int i = 0; i < Subscriptions.products.size(); i ++)
            {
                ISubscription s = Subscriptions.products.get(i);

                if (priority < s.getClass().getAnnotation(Product.class).priority())
                {
                    Subscriptions.products.add(i, s);
                    break;
                }
            }

            if (Subscriptions.products.isEmpty() || ! Subscriptions.products.contains(subscription))
            {
                Subscriptions.products.addFirst(subscription);
            }
        }

        Subscriptions.registry.add(subscription);
        return subscription;
    }

    public static List<ISubscription> products()
    {
        return Subscriptions.products;
    }

    public static ISubscription valueOf(String name)
    {
        List<ISubscription> filteredRegistry = Subscriptions.registry.stream().filter(i -> i.getName().equals(name)).toList();
        return filteredRegistry.isEmpty() ? null : filteredRegistry.getFirst();
    }
}
