package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.FloatTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class FloatTag(
    override val name: String,
    override val data: Float
) : Tag<Float>(TagType.FLOAT, name, data) {
    constructor(name: String, tag: FloatTag) : this(name, tag.asFloat)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.toByteArray())
    }

    override fun convert(): FloatTag {
        return FloatTag.valueOf(data)
    }
}
