package net.azisaba.vanilife.magic;

import net.azisaba.vanilife.util.Registry;

public class Magics
{
    public static final Registry<Magic> registry = new Registry<>();

    public static final AdminMagic ADMIN = (AdminMagic) Magics.registry.register("admin", new AdminMagic());

    public static final CakeMagic CAKE = (CakeMagic) Magics.registry.register("cake", new CakeMagic());

    public static final ClearMagic CLEAR = (ClearMagic) Magics.registry.register("clear", new ClearMagic());

    public static final ElytraMagic ELYTRA = (ElytraMagic) Magics.registry.register("elytra", new ElytraMagic());

    public static final DiamondMagic DIAMOND = (DiamondMagic) Magics.registry.register("diamond", new DiamondMagic());

    public static final DownfallMagic DOWNFALL = (DownfallMagic) Magics.registry.register("down_fall", new DownfallMagic());

    public static final EmeraldMagic EMERALD = (EmeraldMagic) Magics.registry.register("emerald", new EmeraldMagic());

    public static final GoldMagic GOLD = (GoldMagic) Magics.registry.register("gold", new GoldMagic());

    public static final IronMagic IRON = (IronMagic) Magics.registry.register("iron", new IronMagic());

    public static final MolaMagic MOLA = (MolaMagic) Magics.registry.register("mola", new MolaMagic());

    public static final NetheriteMagic NETHERITE = (NetheriteMagic) Magics.registry.register("nethrite", new NetheriteMagic());

    public static final PromptInjectionMagic PROMPT_INJECTION = (PromptInjectionMagic) Magics.registry.register("prompt_injection", new PromptInjectionMagic());
}
