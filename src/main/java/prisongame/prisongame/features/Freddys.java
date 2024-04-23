package prisongame.prisongame.features;

import kotlin.Pair;
import kotlin.Triple;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;

import java.util.ArrayList;

public class Freddys implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var world = Bukkit.getWorld("world");
        var time = world.getTime();

        if (time > 0 && time < 20)
            for (Player player : Bukkit.getOnlinePlayers())
                if (PrisonGame.isInside(player, new Location(Bukkit.getWorld("world"), -35, -55, -991), new Location(Bukkit.getWorld("world"), -59, -61, -968)))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only prison:freder");

        if (time < 16000)
            for (Entity entity : world.getEntities()) {
                var name = entity.getCustomName();

                if (name == null)
                    continue;

                if (!name.equals("fredy") && !name.equals("boner") && !name.equals("foxer") && !name.equals("chicker"))
                    continue;

                entity.remove();
            }

        if (time != 16000)
            return;

        var location = new Location(world, -40, -58, -973);
        var animatronics = new ArrayList<Pair<String, String>>();
        animatronics.add(new Pair<>("FreddyFazbearXXX", "fredy"));
        animatronics.add(new Pair<>("Freakedoutt", "boner"));
        animatronics.add(new Pair<>("minekaufcraft", "chicker"));
        animatronics.add(new Pair<>("Paignton", "foxer"));

        for (var animatronic : animatronics) {
            var skin = animatronic.getFirst();
            var name = animatronic.getSecond();

            var entity = (LivingEntity) world.spawnEntity(location, EntityType.ZOMBIE);
            entity.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(999999, 255));
            entity.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
            entity.setInvulnerable(true);
            entity.setPersistent(true);
            entity.setCustomName(name);
            entity.setSilent(true);

            var disguise = new PlayerDisguise(skin);
            disguise.setName(name);

            DisguiseAPI.disguiseEntity(entity, disguise);
        }
    }
}
