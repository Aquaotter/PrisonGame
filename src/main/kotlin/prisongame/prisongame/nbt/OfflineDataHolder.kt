package prisongame.prisongame.nbt

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import prisongame.prisongame.nbt.tag.CompoundTag
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

open class OfflineDataHolder(val uuid: UUID) {
    constructor(player: OfflinePlayer) : this(player.uniqueId)

    private val world = Bukkit.getWorlds()[0]
    private val file = world.worldFolder.resolve("playerdata/$uuid.dat")
    private var reader: NBTReader? = null
    val compound: CompoundTag

    init {
        if (!file.exists()) {
            compound = CompoundTag("", mutableMapOf())
        } else {
            reader = NBTReader(GZIPInputStream(FileInputStream(file)))
            compound = reader!!.readTag() as CompoundTag
            reader!!.stream.close()
        }
    }

    fun isOnline(): Boolean {
        return Bukkit.getOnlinePlayers().any { it.uniqueId == uuid }
    }

    fun close() {
        if (isOnline())
            return

        val writer = NBTWriter(GZIPOutputStream(FileOutputStream(file)))
        writer.writeTag(compound)
        writer.stream.close()
    }
}