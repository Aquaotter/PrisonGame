package prisongame.prisongame.keys;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

public record Key<Z>(String name, NamespacedKey key, KeyType<Z> type) {
    public @Nullable Z get(PersistentDataHolder player) {
        var pdc = player.getPersistentDataContainer();
        return pdc.get(key, type.dataType());
    }

    public Z get(PersistentDataHolder player, Z defaultValue) {
        var value = get(player);

        if (value == null)
            return defaultValue;
        return value;
    }

    public boolean has(PersistentDataHolder player) {
        var pdc = player.getPersistentDataContainer();
        return pdc.has(key);
    }

    public void set(PersistentDataHolder player, Z value) {
        var pdc = player.getPersistentDataContainer();
        pdc.set(key, type.dataType(), value);
    }

    public void remove(PersistentDataHolder holder) {
        var pdc = holder.getPersistentDataContainer();
        pdc.remove(key);
    }
}
