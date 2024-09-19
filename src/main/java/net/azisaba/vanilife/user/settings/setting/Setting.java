package net.azisaba.vanilife.user.settings.setting;

import com.google.gson.JsonElement;
import net.azisaba.vanilife.user.User;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Setting<T extends Serializable> implements ISetting<T>
{
    protected final User user;

    public Setting(@NotNull User user)
    {
        this.user = user;
    }

    @NotNull
    public String getKey()
    {
        return "settings." + this.getName();
    }

    public void write(@NotNull T value)
    {
        if (value instanceof String)
        {
            this.user.write(this.getKey(), (String) value);
        }

        if (value instanceof Number)
        {
            this.user.write(this.getKey(), (Number) value);
        }

        if (value instanceof Boolean)
        {
            this.user.write(this.getKey(), (Boolean) value);
        }

        if (value instanceof Character)
        {
            this.user.write(this.getKey(), (Character) value);
        }
    }

    public JsonElement read()
    {
        return this.user.read(this.getKey());
    }
}
