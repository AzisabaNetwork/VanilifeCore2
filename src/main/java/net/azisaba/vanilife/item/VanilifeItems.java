package net.azisaba.vanilife.item;

import net.azisaba.vanilife.util.Registry;

public class VanilifeItems
{
    public static final Registry<VanilifeItem> registry = new Registry<>();

    public static final MagicWandItem MAGIC_WAND = (MagicWandItem) VanilifeItems.registry.register("hw24.magic_wand", new MagicWandItem());

    public static final MilkItem MILK = (MilkItem) VanilifeItems.registry.register("hw24.milk", new MilkItem());

    public static final PumpkinItem PUMPKIN = (PumpkinItem) VanilifeItems.registry.register("hw24.pumpkin", new PumpkinItem());

    public static final SugarItem SUGAR = (SugarItem) VanilifeItems.registry.register("hw24.sugar", new SugarItem());

    public static final TreatItem TREAT = (TreatItem) VanilifeItems.registry.register("hw24.treat", new TreatItem());
}
