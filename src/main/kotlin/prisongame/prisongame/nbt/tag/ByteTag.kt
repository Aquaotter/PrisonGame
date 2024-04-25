package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.ByteTag
import java.util.zip.GZIPOutputStream

data class ByteTag(
    override val name: String,
    override val data: Byte
) : Tag<Byte>(TagType.BYTE, name, data) {
    constructor(name: String, tag: ByteTag) : this(name, tag.asByte)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(byteArrayOf(data))
    }

    override fun convert(): ByteTag {
        return ByteTag.valueOf(data)
    }
}