package prisongame.prisongame.features;

import com.sun.jna.platform.win32.WinBase;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

/**
 * Keeps track of various goals for advancements.
 */
public class Advancements implements Feature {
    private final Advancement DICTATORSHIP = getAdvancement("dictatorship");
    private final Advancement WHAT_U_WORRIED = getAdvancement("whatuworried");
    private final Advancement DEWATER = getAdvancement("dewater");
    private final Advancement PIGGO = getAdvancement("piggo");
    private final Advancement GOLDDIGGER = getAdvancement("golddigger");

    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            checkWhatUWorried(player);
            checkDewater(player);
            checkGoldDigger(player);
        }

        checkDictatorship();
        checkPiggo();
    }

    private void checkDewater(Player player) {
        var name = PrisonGame.active.getName();
        var y = player.getY();

        if (((!name.equals("Island") || y != 61) && (!name.equals("Boat") || y > -53)) || !player.isInWater())
            return;

        var progress = player.getAdvancementProgress(DEWATER);
        progress.awardCriteria("impossible");
    }

    private void checkDictatorship() {
        if (PrisonGame.warden == null)
            return;

        var player = PrisonGame.warden;
        var time = PrisonGame.wardentime.get(player);
        var progress = player.getAdvancementProgress(DICTATORSHIP);

        if (time / 20 / 60 >= 120 && !progress.isDone())
            progress.awardCriteria("impossible");
    }

    private void checkWhatUWorried(Player player) {
        if (!PrisonGame.worryachieve.containsKey(player))
            PrisonGame.worryachieve.put(player, -1);

        var time = PrisonGame.worryachieve.get(player) + 1;

        if (time <= 0)
            return;

        var progress = player.getAdvancementProgress(WHAT_U_WORRIED);
        PrisonGame.worryachieve.put(player, time);

        if (time / 20 / 60 >= 15 && !progress.isDone()) {
            PrisonGame.worryachieve.put(player, -1);
            progress.awardCriteria("impossible");
        }
    }
    private void checkGoldDigger(Player p){
        int shoveled = Keys.SHOVELING_COUNT.get(p, 0);
        if(shoveled >= 500 && p.getAdvancementProgress(GOLDDIGGER).equals(false)){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:golddigger");
        }
    }

    private void checkPiggo() {
        if (!PrisonGame.givepig || PrisonGame.warden == null)
            return;

        var progress = PrisonGame.warden.getAdvancementProgress(PIGGO);
        progress.awardCriteria("impossible");
        PrisonGame.givepig = false;
    }

    private static Advancement getAdvancement(String key) {
        var namespacedKey = new NamespacedKey("prison", key);
        return Bukkit.getAdvancement(namespacedKey);
    }
}
