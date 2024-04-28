package prisongame.prisongame.features;

import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import prisongame.prisongame.keys.Keys;

public class DevMode implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for(Player p : Bukkit.getOnlinePlayers()){
            Integer isDevMode = Keys.DEVMODE.get(p, 0);
            if(isDevMode == 1){
                p.setDisplayName(p.getName());
            }
        }
    }
}
