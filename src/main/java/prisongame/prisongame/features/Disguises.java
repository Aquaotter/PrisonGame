package prisongame.prisongame.features;

import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;

public class Disguises implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            DisguiseAPI.setActionBarShown(player, false);
    }
}
