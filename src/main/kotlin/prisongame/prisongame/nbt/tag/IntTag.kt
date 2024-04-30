package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.IntTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class IntTag(
    override val name: String,
    override val data: Int
) : Tag<Int>(TagType.INT, name, data) {
    constructor(name: String, tag: IntTag) : this(name, tag.asInt)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.toByteArray())
    }

    override fun convert(): IntTag {
        return IntTag.valueOf(data)
    }
}