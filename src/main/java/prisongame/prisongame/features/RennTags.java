package prisongame.prisongame.features;

import org.bukkit.Bukkit;

import java.util.UUID;

public class RennTags implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var player = Bukkit.getPlayer(UUID.fromString("31d3ab71-8b3d-4f47-93d4-66433f4c315b"));

        if (player == null)
            return;

        player.setDisplayName(player.getDisplayName().replace("FUNDER", "fund deez nuts"));
        player.setDisplayName(player.getDisplayName().replace("WARDEN", "nah fuck this dude"));
        player.setDisplayName(player.getDisplayName().replace("GUARD", "worst guard"));
        player.setDisplayName(player.getDisplayName().replace("NURSE", "L nurse"));
        player.setDisplayName(player.getDisplayName().replace("SWAT", "who gave him swat"));
        player.setDisplayName(player.getDisplayName().replace("PRISONER", "idiot -->"));
        player.setDisplayName(player.getDisplayName().replace("SOLITARY", "deserved"));
        player.setDisplayName(player.getDisplayName().replace("VISITOR", "i wish he was a prisoner"));
        player.setDisplayName(player.getDisplayName().replace("CRIMINAL", "*snore* mimimimimi"));
    }
}
