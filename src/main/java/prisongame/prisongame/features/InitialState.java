package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class InitialState implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (!PrisonGame.roles.containsKey(player)) {
                PrisonGame.roles.put(player, Role.PRISONER);
                MyListener.playerJoin(player, false);
            }

            if (!PrisonGame.hardmode.containsKey(player))
                PrisonGame.hardmode.put(player, false);

            if (!PrisonGame.word.containsKey(player))
                PrisonGame.word.put(player, "Amoger Susser");

            if (!PrisonGame.escaped.containsKey(player))
                PrisonGame.escaped.put(player, false);

            if (!PrisonGame.prisonnumber.containsKey(player))
                PrisonGame.prisonnumber.put(player, "690");
        }
    }
}
