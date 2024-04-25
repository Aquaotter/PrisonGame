package prisongame.prisongame.lib;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import prisongame.prisongame.nbt.OfflineDataHolder;
import prisongame.prisongame.nbt.tag.CompoundTag;
import prisongame.prisongame.nbt.tag.IntTag;
import prisongame.prisongame.nbt.tag.TagConverterKt;
import prisongame.prisongame.nbt.tag.TagType;

import java.util.ArrayList;

public class OfflineEnderChest extends PlayerEnderChestContainer {
    private OfflineDataHolder holder;
    public OfflinePlayer owner;

    public OfflineEnderChest(OfflinePlayer owner) {
        super(null);
        this.owner = owner;

        holder = new OfflineDataHolder(owner);
        var data = holder.getCompound().getData();

        if (data.containsKey("EnderItems"))
            fromTag((ListTag) data.get("EnderItems").convert());
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        super.onClose(who);

        var tags = new ArrayList<CompoundTag>();

        for (int i = 0; i < getContainerSize(); i++) {
            var item = items.get(i);
            var tag = item.save(new net.minecraft.nbt.CompoundTag());
            var compound = (CompoundTag) TagConverterKt.convertFromVanillaTag(String.valueOf(i), tag);

            compound.getData().put("Slot", new IntTag("Slot", i));
            tags.add(compound);
        }

        var list = new prisongame.prisongame.nbt.tag.ListTag("EnderItems", TagType.COMPOUND, tags);
        holder.getCompound().getData().put("EnderItems", list);

        holder.close();
    }
}
