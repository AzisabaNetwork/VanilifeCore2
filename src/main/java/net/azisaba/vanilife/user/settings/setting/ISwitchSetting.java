package net.azisaba.vanilife.user.settings.setting;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;

public interface ISwitchSetting<T extends Serializable> extends ISetting<T>
{
    @NotNull LinkedHashMap<T, String> getOptions();
}
