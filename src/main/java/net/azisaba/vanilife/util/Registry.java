package net.azisaba.vanilife.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry<T>
{
    private final Map<String, T> registry = new HashMap<>();

    public T get(@NotNull String key)
    {
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
}
