package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.IntArrayTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class IntArrayTag(
    override val name: String,
    override val data: List<IntTag>
) : Tag<List<IntTag>>(TagType.INT_ARRAY, name, data) {
    constructor(name: String, tag: IntArrayTag) : this(
        name,
        tag.asIntArray.mapIndexed { index, int -> IntTag(index.toString(), int) }
    )

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.size.toByteArray())
        data.forEach { it.writePayload(stream) }
    }

    override fun convert(): IntArrayTag {
        return IntArrayTag(data.map(IntTag::data))
    }
}
