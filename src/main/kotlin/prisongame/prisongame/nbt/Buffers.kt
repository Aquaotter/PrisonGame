package prisongame.prisongame.nbt

import java.nio.ByteBuffer

fun Short.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(2)
        .putShort(this)
        .array()
}

fun Int.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(4)
        .putInt(this)
        .array()
}

fun Float.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(4)
        .putFloat(this)
        .array()
}

fun Double.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(8)
        .putDouble(this)
        .array()
}

fun Long.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(8)
        .putLong(this)
        .array()
}