package net.azisaba.vanilife.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry<T>
{
    private final Map<String, T> registry = new HashMap<>();

    public T get(String key)
    {
        if (key == null)
        {
            return null;
        }

        List<Map.Entry<String, T>> filteredRegistry = this.registry.entrySet().stream().filter(e -> e.getKey().equals(key)).toList();
        return filteredRegistry.isEmpty() ? null : filteredRegistry.getFirst().getValue();
    }

    public boolean has(String key)
    {
        return this.registry.containsKey(key);
    }

    public T register(@NotNull String key, @NotNull T value)
    {
        this.registry.put(key, value);
        return value;
    }

    public @NotNull List<String> keys()
    {
        return new ArrayList<>(this.registry.keySet().stream().toList());
    }

    public @NotNull List<T> values()
    {
        return new ArrayList<>(this.registry.values());
    }

    public @NotNull Map<String, T> map()
    {
        return new HashMap<>(this.registry);
    }
}
