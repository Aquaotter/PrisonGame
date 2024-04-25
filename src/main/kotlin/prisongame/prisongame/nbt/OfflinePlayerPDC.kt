package prisongame.prisongame.nbt

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import prisongame.prisongame.nbt.tag.*

class OfflinePlayerPDC(val data: MutableMap<String, Tag<*>>) : PersistentDataContainer {
    private val adapterContext = OfflinePlayerPersistentDataAdapterContext()

    override fun <T : Any?, Z : Any?> set(p0: NamespacedKey, p1: PersistentDataType<T, Z>, p2: Z & Any) {
        val key = p0.toString()
        data[key] = wrap(p0, p1.primitiveType, p1.toPrimitive(p2, adapterContext))
    }

    override fun <T : Any?, Z : Any?> has(p0: NamespacedKey, p1: PersistentDataType<T, Z>): Boolean {
        val key = p0.toString()
        return has(p0) && data[key]!!.data!!.javaClass == p1.complexType
    }

    override fun has(p0: NamespacedKey): Boolean {
        val key = p0.toString()
        return key in data
    }

    override fun <T : Any?, Z : Any?> get(p0: NamespacedKey, p1: PersistentDataType<T, Z>): Z? {
        val key = p0.toString()

        @Suppress("UNCHECKED_CAST")
        val tag = (data[key] ?: return null) as Tag<T & Any>

        return p1.fromPrimitive(tag.data, adapterContext)
    }

    override fun <T : Any?, Z : Any?> getOrDefault(
        p0: NamespacedKey,
        p1: PersistentDataType<T, Z>,
        p2: Z & Any
    ): Z & Any {
        return get(p0, p1) ?: p2
    }

    override fun getKeys(): MutableSet<NamespacedKey> {
        val keys = mutableSetOf<NamespacedKey>()

        for (key in data.keys) {
            val chunks = key.split(":")
            keys.add(NamespacedKey(chunks[0], chunks[1]))
        }

        return keys
    }

    override fun remove(p0: NamespacedKey) {
        data.remove(p0.toString())
    }

    override fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    override fun getAdapterContext(): PersistentDataAdapterContext {
        return adapterContext
    }

    override fun serializeToBytes(): ByteArray {
        throw UnsupportedOperationException("Not needed")
    }

    override fun readFromBytes(p0: ByteArray, p1: Boolean) {
        throw UnsupportedOperationException("Not needed")
    }

    private fun <T> wrap(p0: NamespacedKey, type: Class<T>, value: T): Tag<T> {
        val key = p0.toString()
        return when (type) {
            ByteArray::class.java -> ByteArrayTag(key, (value as ByteArray).mapIndexed { i, v -> ByteTag(i.toString(), v) })
            Byte::class.java -> ByteTag(key, value as Byte)
            Double::class.java -> DoubleTag(key, value as Double)
            Float::class.java -> FloatTag(key, value as Float)
            IntArray::class.java -> IntArrayTag(key, (value as IntArray).mapIndexed { i, v -> IntTag(i.toString(), v) })
            Int::class.java -> IntTag(key, value as Int)
            LongArray::class.java -> LongArrayTag(key, (value as LongArray).mapIndexed { i, v -> LongTag(i.toString(), v) })
            Long::class.java -> LongTag(key, value as Long)
            Short::class.java -> ShortTag(key, value as Short)
            String::class.java -> StringTag(key, value as String)
            else -> throw IllegalArgumentException("Invalid type")
        } as Tag<T>
    }
}