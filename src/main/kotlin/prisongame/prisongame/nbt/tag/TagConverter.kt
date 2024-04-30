package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.ByteArrayTag as VanillaByteArrayTag
import net.minecraft.nbt.CompoundTag as VanillaCompoundTag
import net.minecraft.nbt.DoubleTag as VanillaDoubleTag
import net.minecraft.nbt.FloatTag as VanillaFloatTag
import net.minecraft.nbt.IntArrayTag as VanillaIntArrayTag
import net.minecraft.nbt.ListTag as VanillaListTag
import net.minecraft.nbt.LongArrayTag as VanillaLongArrayTag
import net.minecraft.nbt.LongTag as VanillaLongTag
import net.minecraft.nbt.StringTag as VanillaStringTag
import net.minecraft.nbt.ByteTag as VanillaByteTag
import net.minecraft.nbt.EndTag as VanillaEndTag
import net.minecraft.nbt.IntTag as VanillaIntTag
import net.minecraft.nbt.ShortTag as VanillaShortTag
import net.minecraft.nbt.Tag as VanillaTag

fun convertFromVanillaTag(name: String, tag: VanillaTag): Tag<out Any> {
    return when (tag.type) {
        VanillaEndTag.TYPE -> EndTag()
        VanillaByteTag.TYPE -> ByteTag(name, tag as VanillaByteTag)
        VanillaShortTag.TYPE -> ShortTag(name, tag as VanillaShortTag)
        VanillaIntTag.TYPE -> IntTag(name, tag as VanillaIntTag)
        VanillaLongTag.TYPE -> LongTag(name, tag as VanillaLongTag)
        VanillaFloatTag.TYPE -> FloatTag(name, tag as VanillaFloatTag)
        VanillaDoubleTag.TYPE -> DoubleTag(name, tag as VanillaDoubleTag)
        VanillaByteArrayTag.TYPE -> ByteArrayTag(name, tag as VanillaByteArrayTag)
        VanillaStringTag.TYPE -> StringTag(name, tag as VanillaStringTag)
        VanillaListTag.TYPE -> ListTag(name, tag as VanillaListTag)
        VanillaCompoundTag.TYPE -> CompoundTag(name, tag as VanillaCompoundTag)
        VanillaIntArrayTag.TYPE -> IntArrayTag(name, tag as VanillaIntArrayTag)
        VanillaLongArrayTag.TYPE -> LongArrayTag(name, tag as VanillaLongArrayTag)
        else -> throw IllegalArgumentException("Invalid tag")
    }
}