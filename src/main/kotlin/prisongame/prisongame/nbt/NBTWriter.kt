package prisongame.prisongame.nbt

import prisongame.prisongame.nbt.tag.*
import java.nio.ByteBuffer
import java.util.zip.GZIPOutputStream

class NBTWriter(val stream: GZIPOutputStream) {
    fun writeTag(tag: Tag<*>) {
        tag.writeHeader(stream)
        tag.writePayload(stream)
    }
}