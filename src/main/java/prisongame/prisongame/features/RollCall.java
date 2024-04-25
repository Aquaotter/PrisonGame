package prisongame.prisongame.features;

import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.time.Duration;

public class RollCall implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if (!Schedule.bossBar.getTitle().contains("ROLL CALL")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setCollidable(true);
                player.removePotionEffect(PotionEffectType.JUMP);
            }

            return;
        }

        var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        PrisonGame.gotcafefood.clear();
        var allPlayers = true;

        for (var player : Bukkit.getOnlinePlayers()) {
            var below = player.getLocation().clone().subtract(0, 1, 0).getBlock();
            var role = PrisonGame.roles.get(player);
            var world = player.getWorld();

            player.removePotionEffect(PotionEffectType.BAD_OMEN);
            world.getWorldBorder().setWarningDistance(5);

            if (role != Role.PRISONER && PrisonGame.hardmode.get(player)) {
                if (below.getType() != Material.LIGHT_BLUE_CONCRETE_POWDER) {
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(250),
                            Duration.ZERO
                    ));
                    player.sendTitlePart(
                            TitlePart.SUBTITLE,
                            PrisonGame.mm.deserialize("<blue>GET ONTO LIGHT BLUE POWDER OR GET FIRED!"));

                    player.setCollidable(true);
                    player.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                    player.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                    player.removePotionEffect(PotionEffectType.JUMP);
                } else {
                    if (player.hasPotionEffect(PotionEffectType.GLOWING) && player.getGameMode() != GameMode.SPECTATOR) {
                        player.sendMessage(PrisonGame.mm.deserialize("<green>You came to roll call!"));
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                    }

                    player.setFoodLevel(6);
                    player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
                    player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20, 255));
                    player.removePotionEffect(PotionEffectType.HUNGER);
                    player.removePotionEffect(PotionEffectType.GLOWING);
                }
            }

            if (role == Role.PRISONER && !PrisonGame.escaped.get(player)) {
                if (below.getType() != Material.RED_SAND) {
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(250),
                            Duration.ZERO
                    ));
                    player.sendTitlePart(
                            TitlePart.SUBTITLE,
                            PrisonGame.mm.deserialize("<red>GET ONTO THE RED SAND OR YOU'LL BE KILLED!"));

                    player.setCollidable(true);
                    player.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                    player.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                    player.removePotionEffect(PotionEffectType.JUMP);
                    allPlayers = false;

                    scoreboard.getTeam("Criminals").addPlayer(player);
                } else {
                    // If they're a prisoner but on the criminals team, put them on the prisoners team.
                    if (scoreboard.getPlayerTeam(player) == scoreboard.getTeam("Criminals"))
                        scoreboard.getTeam("Prisoners").addPlayer(player);

                    if (player.hasPotionEffect(PotionEffectType.GLOWING) && player.getGameMode() != GameMode.SPECTATOR) {
                        player.sendMessage(PrisonGame.mm.deserialize("<green>You came to roll call!"));
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                    }

                    player.addPotionEffect(PotionEffectType.SLOW.createEffect(10, 255));
                    player.addPotionEffect(PotionEffectType.JUMP.createEffect(20, -25));
                    player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
                    player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20, 255));
                    player.removePotionEffect(PotionEffectType.HUNGER);
                    player.removePotionEffect(PotionEffectType.GLOWING);
                    player.setHealth(player.getMaxHealth());
                    player.setCollidable(false);
                    player.setFoodLevel(6);
                }
            }
        }

        if (allPlayers && PrisonGame.warden != null) {
            PrisonGame.warden.sendMessage(PrisonGame.mm.deserialize("<green>All players at roll call! +1k$!"));
            Keys.MONEY.set(PrisonGame.warden, Keys.MONEY.get(PrisonGame.warden, 0.0) + 1000.0);

            for (var player : Bukkit.getOnlinePlayers()) {
                player.playSound(player, Sound.BLOCK_BELL_USE, 1, 1);

                scheduler.runTaskLater(plugin, () -> {
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ofMillis(50 * 20),
                            Duration.ofMillis(50 * 40),
                            Duration.ofMillis(50 * 20)
                    ));
                    player.sendTitlePart(TitlePart.SUBTITLE,
                            PrisonGame.mm.deserialize("<b>" + Schedule.bossBar.getTitle()));

                    player.playSound(player, Sound.BLOCK_BELL_USE, 1, 1);
                }, 4);

                scheduler.runTaskLater(plugin, () -> {
                    player.playSound(player, Sound.BLOCK_BELL_USE, 1, 1);
                }, 8);
            }

            Bukkit.getWorld("world").setTime(Schedule.endTimer + 1);
        }
    }
}
