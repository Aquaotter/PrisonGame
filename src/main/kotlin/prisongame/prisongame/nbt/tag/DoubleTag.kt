package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.DoubleTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class DoubleTag(
    override val name: String,
    override val data: Double
) : Tag<Double>(TagType.DOUBLE, name, data) {
    constructor(name: String, tag: DoubleTag) : this(name, tag.asDouble)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.toByteArray())
    }

    override fun convert(): DoubleTag {
        return DoubleTag.valueOf(data)
    }
}
