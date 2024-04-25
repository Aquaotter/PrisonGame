package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.LongArrayTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class LongArrayTag(
    override val name: String,
    override val data: List<LongTag>
) : Tag<List<LongTag>>(TagType.LONG_ARRAY, name, data) {
    constructor(name: String, tag: LongArrayTag) : this(
        name,
        tag.asLongArray.mapIndexed { index, long -> LongTag(index.toString(), long) }
    )

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.size.toByteArray())
        data.forEach { it.writePayload(stream) }
    }

    override fun convert(): LongArrayTag {
        return LongArrayTag(data.map(LongTag::data))
    }
}
