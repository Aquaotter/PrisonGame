package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.StringTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class StringTag(
    override val name: String,
    override val data: String
) : Tag<String>(TagType.STRING, name, data) {
    constructor(name: String, tag: StringTag) : this(name, tag.asString)

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(data.length.toShort().toByteArray())
        stream.write(data.map { it.code.toByte() }.toByteArray())
    }

    override fun convert(): StringTag {
        return StringTag.valueOf(data)
    }
}
