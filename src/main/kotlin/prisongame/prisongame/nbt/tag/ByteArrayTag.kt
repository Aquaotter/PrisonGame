package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.ByteArrayTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class ByteArrayTag(
    override val name: String,
    override val data: List<ByteTag>
) : Tag<List<ByteTag>>(TagType.BYTE_ARRAY, name, data) {
    constructor(name: String, tag: ByteArrayTag) : this(
        name,
        tag.asByteArray.mapIndexed { index, byte -> ByteTag(index.toString(), byte) }
    )

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.size.toByteArray())
        data.forEach { it.writePayload(stream) }
    }

    override fun convert(): ByteArrayTag {
        return ByteArrayTag(data.map(ByteTag::data))
    }
}