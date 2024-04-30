package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.ListTag
import prisongame.prisongame.nbt.toByteArray
import java.util.zip.GZIPOutputStream

data class ListTag(
    override val name: String,
    val listType: TagType,
    override val data: List<Tag<*>>
) : Tag<List<Tag<*>>>(TagType.LIST, name, data) {
    constructor(name: String, tag: ListTag) : this(
        name,
        TagType.entries[tag.elementType.toInt()],
        (0..<tag.size).map { convertFromVanillaTag(it.toString(), tag[it]) }
    )

    override fun writePayload(stream: GZIPOutputStream) {
        stream.write(listType.ordinal)
        stream.write(data.size.toByteArray())

        for (tag in data)
            tag.writePayload(stream)
    }

    override fun convert(): ListTag {
        val tag = ListTag()

        for (item in data)
            tag.add(item.convert())

        return tag
    }
}
