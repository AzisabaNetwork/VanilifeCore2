package net.azisaba.vanilife.gimmick;

import net.azisaba.vanilife.util.Registry;

public class Gimmicks
{
    public static final Registry<Class<? extends Gimmick>> registry = new Registry<>();

    public static final Class<? extends Gimmick> INIT = Gimmicks.registry.register("init", InitGimmick.class);

    public static final Class<? extends Gimmick> MESSAGE = Gimmicks.registry.register("message", MessageGimmick.class);

    public static final Class<? extends Gimmick> TITLE = Gimmicks.registry.register("title", TitleGimmick.class);

    public static final Class<? extends Gimmick> SOUND = Gimmicks.registry.register("sound", SoundGimmick.class);
}
