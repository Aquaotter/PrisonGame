package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.ShortTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class ShortTag(
    override val name: String,
    override val data: Short
) : Tag<Short>(TagType.SHORT, name, data) {
    constructor(name: String, tag: ShortTag) : this(name, tag.asShort)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.toByteArray())
    }

    override fun convert(): ShortTag {
        return ShortTag.valueOf(data)
    }
}