package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.staff.VanishCommand;

public class Vanish implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            for (Player player2 : Bukkit.getOnlinePlayers())
                if (!player.canSee(player2) && player.isOnline())
                    if (!player2.isInsideVehicle() && player2.isOnline() && !player2.getPersistentDataContainer().has(VanishCommand.VANISHED))
                        player.showPlayer(PrisonGame.getPlugin(PrisonGame.class), player2);
        }
    }
}
