package prisongame.prisongame.keys;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record KeyType<Z>(PersistentDataType<?, Z> dataType, Function<String, Z> parser) {}
