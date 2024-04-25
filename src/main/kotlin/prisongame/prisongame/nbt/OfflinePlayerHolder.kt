package prisongame.prisongame.nbt

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder
import prisongame.prisongame.nbt.tag.CompoundTag
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class OfflinePlayerHolder(uuid: UUID) : PersistentDataHolder, OfflineDataHolder(uuid) {
    constructor(player: OfflinePlayer) : this(player.uniqueId)
    private var values: CompoundTag

    init {
        val empty = CompoundTag("BukkitValues", mutableMapOf())
        values = (compound.data["BukkitValues"] ?: empty) as CompoundTag
    }

    override fun getPersistentDataContainer(): PersistentDataContainer {
        return if (isOnline()) Bukkit.getPlayer(uuid)!!.persistentDataContainer
            else OfflinePlayerPDC(values.data)
    }
}