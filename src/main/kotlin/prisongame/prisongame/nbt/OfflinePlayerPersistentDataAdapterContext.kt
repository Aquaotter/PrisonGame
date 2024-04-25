package prisongame.prisongame.nbt

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import prisongame.prisongame.nbt.tag.CompoundTag

class OfflinePlayerPersistentDataAdapterContext : PersistentDataAdapterContext {
    override fun newPersistentDataContainer(): PersistentDataContainer {
        return OfflinePlayerPDC(CompoundTag("BukkitValues", mutableMapOf()).data)
    }
}