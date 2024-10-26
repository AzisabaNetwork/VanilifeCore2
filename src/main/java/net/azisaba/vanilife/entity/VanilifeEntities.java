package net.azisaba.vanilife.entity;

import net.azisaba.vanilife.util.Registry;

public class VanilifeEntities
{
    public static final Registry<Class<? extends VanilifeEntity<?>>> registry = new Registry<>();

    public static final Class<? extends VanilifeEntity<?>> DRAGON = VanilifeEntities.registry.register("dragon", DragonEntity.class);

    public static final Class<? extends VanilifeEntity<?>> GATEWAY = VanilifeEntities.registry.register("gateway", GatewayEntity.class);
}
