package prisongame.prisongame.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            event.getPlayer().sendMessage("Wow! You managed to place a block in survival mode! This means the server is completely broken, or it's reloading. Please tell Aquaotter. Please.");
            event.setCancelled(true);
        }
    }
}
