package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.LongTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class LongTag(
    override val name: String,
    override val data: Long
) : Tag<Long>(TagType.LONG, name, data) {
    constructor(name: String, tag: LongTag) : this(name, tag.asLong)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.toByteArray())
    }

    override fun convert(): LongTag {
        return LongTag.valueOf(data)
    }
}
