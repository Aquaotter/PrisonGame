package prisongame.prisongame.nbt.tag

import net.minecraft.nbt.EndTag
import java.util.zip.GZIPOutputStream

class EndTag : Tag<Unit>(TagType.END, "", Unit) {
    override fun writePayload(stream: GZIPOutputStream) {}
    override fun convert(): EndTag {
        return EndTag.INSTANCE
    }
}