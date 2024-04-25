package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.CompoundTag as VanillaCompoundTag
import java.util.zip.GZIPOutputStream

data class CompoundTag(
    override val name: String,
    override val data: MutableMap<String, Tag<*>>
) : Tag<Map<String, Tag<*>>>(TagType.COMPOUND, name, data) {
    constructor(name: String, tag: CompoundTag) : this(
        name,
        tag.tags
            .mapValues { convertFromVanillaTag(it.key, it.value) }
            .toMutableMap()
    )

    override fun writePayload(stream: GZIPOutputStream) {
        data.values.forEach { it.write(stream) }
        EndTag().write(stream)
    }

    override fun convert(): VanillaCompoundTag {
        val tag = VanillaCompoundTag()

        for (entry in data.entries)
            tag.put(entry.key, entry.value.convert())

        return tag
    }
}