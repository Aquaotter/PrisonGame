package prisongame.prisongame.listeners;

import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.BlockStateMeta;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.OfflineEnderChest;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onShulkerClose(InventoryCloseEvent event) {
        var inventory = event.getInventory();
        var player = (Player) event.getPlayer();
        var type = inventory.getType();

        if (type != InventoryType.SHULKER_BOX || !PrisonGame.shulkers.containsKey(player))
            return;

        var pair = PrisonGame.shulkers.get(player);
        var clickEvent = pair.getFirst();
        var item = pair.getSecond();

        var meta = (BlockStateMeta) item.getItemMeta();
        var shulkerBox = (ShulkerBox) meta.getBlockState();

        shulkerBox.getInventory().setContents(inventory.getContents());
        meta.setBlockState(shulkerBox);
        item.setItemMeta(meta);
        clickEvent.setCurrentItem(item);
        PrisonGame.shulkers.remove(player);

        var vanillaInventory = ((CraftInventory) clickEvent.getInventory()).getInventory();

        if (vanillaInventory instanceof OfflineEnderChest)
            vanillaInventory.onClose((CraftHumanEntity) player);
    }
}
