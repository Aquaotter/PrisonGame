package prisongame.prisongame.nbt

import prisongame.prisongame.nbt.tag.*
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.util.zip.GZIPInputStream

class NBTReader(val stream: GZIPInputStream) {
    fun readTag(): Tag<*> {
        val type = readType()
        val name = readName(type)
        return readPayload(type, name)
    }

    private fun readPayload(type: TagType, name: String): Tag<*> {
        return when (type) {
            TagType.END -> EndTag()
            TagType.BYTE -> readByte(name)
            TagType.SHORT -> readShort(name)
            TagType.INT -> readInt(name)
            TagType.LONG -> readLong(name)
            TagType.FLOAT -> readFloat(name)
            TagType.DOUBLE -> readDouble(name)
            TagType.BYTE_ARRAY -> readByteArray(name)
            TagType.STRING -> readString(name)
            TagType.LIST -> readList(name)
            TagType.COMPOUND -> readCompound(name)
            TagType.INT_ARRAY -> readIntArray(name)
            TagType.LONG_ARRAY -> readLongArray(name)
//            else -> throw UnsupportedOperationException("Tried to read unsupported tag type $type")
        }
    }

    private fun readType(): TagType {
        return TagType.entries[stream.read()]
    }

    private fun readName(type: TagType): String {
        if (type == TagType.END)
            return ""

        val length = readTagLength()
        val name = StringBuilder()

        for (i in 0..<length)
            name.append(stream.read().toChar())

        return name.toString()
    }

    private fun readByte(name: String): ByteTag {
        return ByteTag(name, stream.read().toByte())
    }

    private fun readShort(name: String): ShortTag {
        return ShortTag(name, readAndWrap(2).getShort())
    }

    private fun readInt(name: String): IntTag {
        return IntTag(name, readAndWrap(4).getInt())
    }

    private fun readLong(name: String): LongTag {
        return LongTag(name, readAndWrap(8).getLong())
    }

    private fun readFloat(name: String): FloatTag {
        return FloatTag(name, readAndWrap(4).getFloat())
    }

    private fun readDouble(name: String): DoubleTag {
        return DoubleTag(name, readAndWrap(8).getDouble())
    }

    private fun readByteArray(name: String): ByteArrayTag {
        val size = readAndWrap(4).getInt()
        val tags = mutableListOf<ByteTag>()

        for (i in 0..<size)
            tags.add(readByte(""))

        return ByteArrayTag(name, tags)
    }

    private fun readString(name: String): StringTag {
        val shortLength = readAndWrap(2).getShort()
        val length = if (shortLength >= 0) shortLength.toInt() else shortLength + 0x10000

        val value = StringBuilder()

        for (i in 0..<length)
            value.append(stream.read().toChar())

        return StringTag(name, value.toString())
    }

    private fun readList(name: String): ListTag {
        val type = readType()
        val size = readAndWrap(4).getInt()
        val tags = mutableListOf<Tag<*>>()

        for (i in 0..<size)
            tags.add(readPayload(type, ""))

        return ListTag(name, type, tags)
    }

    private fun readCompound(name: String): CompoundTag {
        val tags = mutableMapOf<String, Tag<*>>()

        while (true) {
            val tag = readTag()

            if (tag is EndTag)
                break // end of compound

            tags[tag.name] = tag
        }

        return CompoundTag(name, tags)
    }

    private fun readIntArray(name: String): IntArrayTag {
        val size = readAndWrap(4).getInt()
        val tags = mutableListOf<IntTag>()

        for (i in 0..<size)
            tags.add(readInt(""))

        return IntArrayTag(name, tags)
    }

    private fun readLongArray(name: String): LongArrayTag {
        val size = readAndWrap(4).getInt()
        val tags = mutableListOf<LongTag>()

        for (i in 0..<size)
            tags.add(readLong(""))

        return LongArrayTag(name, tags)
    }

    private fun readAndWrap(length: Int): ByteBuffer {
        return ByteBuffer.wrap(stream.readNBytes(length))
    }

    private fun readTagLength(): Short {
        val bytes = stream.readNBytes(2);
        return ByteBuffer.wrap(bytes).getShort(0);
    }
}