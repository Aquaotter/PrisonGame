package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.Tag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

abstract class Tag<T>(
    val type: TagType,
    open val name: String,
    open val data: T
) {
    fun write(stream: GZIPOutputStream) {
        writeHeader(stream)
        writePayload(stream)
    }

    fun writeHeader(stream: GZIPOutputStream) {
        stream.write(type.ordinal)

        if (type == TagType.END)
            return

        stream.write(name.length.toShort().toByteArray())
        stream.write(name.toCharArray().map { it.code.toByte() }.toByteArray())
    }

    abstract fun writePayload(stream: GZIPOutputStream)
    abstract fun convert(): Tag
}
