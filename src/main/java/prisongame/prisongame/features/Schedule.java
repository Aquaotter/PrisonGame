package prisongame.prisongame.features;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

import java.time.Duration;

public class Schedule implements Feature {
    public static int endTimer;
    public static int startTimer;
    public static int jobMultiplier = 1;
    public static final BossBar bossBar = Bukkit.createBossBar(
            ChatColor.WHITE + "Morning",
            BarColor.WHITE,
            BarStyle.SOLID);

    private final World world = Bukkit.getWorld("world");
    private boolean isNotRollCall = true;

    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var time = world.getTime();

        if (time > 0 && time < 2000)
            setRollCall(true);

        if (time == 2000 && !isNotRollCall)
            endRollCall();

        if (time > 2000 && time < 4000)
            setBreakfast();

        if (time > 4000 && time < 7000)
            setFreeTime();

        if (time > 7000 && time < 10000)
            setJobTime();

        if (time > 10000 && time < 13000)
            setLunch();

        if (time > 13000 && time < 15000)
            setRollCall(false);

        if (time == 15000)
            endRollCall();

        if (time > 15000 && time < 16000)
            setCellTime();
        if(time == 24000) endLightsOut();

        if (time > 16000 && time < 24000)
            setLightsOut();
        else {
            bossBar.setColor(BarColor.WHITE);
            bossBar.removeFlag(BarFlag.DARKEN_SKY);

            if (!PrisonGame.active.getName().equals("Island"))
                bossBar.removeFlag(BarFlag.CREATE_FOG);
        }

        if (time == 16000)
            Bukkit.broadcastMessage(ChatColor.RED + "All cells have been automatically closed! " + ChatColor.DARK_RED + "GET TO SLEEP!");

        if (time > 23300)
            world.setTime(0);

        bossBar.setTitle(ChatColor.BOLD + bossBar.getTitle());
        bossBar.setProgress(Math.max(0, Math.min(1, ((float) time - (float) startTimer) / ((float) endTimer - (float) startTimer))));

        if (time == endTimer)
            for (var p : Bukkit.getOnlinePlayers()) {
                p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);

                scheduler.runTaskLater(plugin, () -> {
                    p.sendTitle("", ChatColor.BOLD + bossBar.getTitle(), 20, 40, 20);
                    p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                }, 4);

                scheduler.runTaskLater(plugin, () -> {
                    p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                }, 8);
            }

        for (var player : Bukkit.getOnlinePlayers())
            bossBar.addPlayer(player);
    }

    private void setRollCall(boolean isMorning) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.removePotionEffect(PotionEffectType.BLINDNESS);

        startTimer = isMorning ? 0 : 13000;
        endTimer = isMorning ? 2500 : 15000;
        bossBar.setTitle((isMorning ? "" : "EVENING ") + "ROLL CALL");
        isNotRollCall = false;

        if (!isMorning)
            jobMultiplier = 1;
    }

    private void endRollCall() {
        isNotRollCall = true;

        for (var player : Bukkit.getOnlinePlayers()) {
            var role = PrisonGame.roles.get(player);

            if (PrisonGame.escaped.get(player) || !player.hasPotionEffect(PotionEffectType.GLOWING) || role != Role.PRISONER || PrisonGame.builder.get(player)) {
                PrisonGame.saidcycle.put(player, PrisonGame.saidcycle.get(player) + 1);
                continue;
            }

            if (player.hasPotionEffect(PotionEffectType.GLOWING) && role == Role.PRISONER) {
                PrisonGame.saidcycle.put(player, PrisonGame.saidcycle.get(player) - 1);
                Bukkit.broadcast(PrisonGame.mm.deserialize(
                        "<gold><player> didn't come to roll call! <red>Kill them for 100 dollars!",
                        Placeholder.component("player", player.name().color(NamedTextColor.RED))
                ));

                player.sendTitlePart(TitlePart.SUBTITLE, PrisonGame.mm.deserialize("<red>COME TO ROLL CALL NEXT TIME!"));
                player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(20 * 4),
                        Duration.ZERO
                ));

                player.playSound(player, Sound.ENTITY_SILVERFISH_AMBIENT, 1, 0.25f);

                if (PrisonGame.hardmode.get(player))
                    player.damage(99999);
            }
        }
    }

    private void setBreakfast() {
        startTimer = 2000;
        endTimer = 4000;

        bossBar.setTitle("Breakfast");

        for (var player : Bukkit.getOnlinePlayers())
            if (!PrisonGame.hardmode.get(player) && !PrisonGame.escaped.get(player) && PrisonGame.roles.get(player) == Role.PRISONER)
                player.addPotionEffect(PotionEffectType.SATURATION.createEffect(120, 0));
    }

    private void setFreeTime() {
        jobMultiplier = 1;
        startTimer = 4000;
        endTimer = 7000;

        bossBar.setTitle("Free Time");
    }

    private void setJobTime() {
        jobMultiplier = 2;
        startTimer = 7000;
        endTimer = 10000;

        bossBar.setTitle("Job Time");
    }

    private void setLunch() {
        jobMultiplier = 1;
        startTimer = 10000;
        endTimer = 13000;

        bossBar.setTitle("Lunch");

        for (var player : Bukkit.getOnlinePlayers())
            if (!PrisonGame.hardmode.get(player) && !PrisonGame.escaped.get(player) && PrisonGame.roles.get(player) == Role.PRISONER)
                player.addPotionEffect(PotionEffectType.SATURATION.createEffect(120, 0));
    }

    private void setCellTime() {
        jobMultiplier = 1;
        startTimer = 15000;
        endTimer = 16000;

        bossBar.setTitle("Cell Time");
    }

    private void setLightsOut() {
        var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        startTimer = 16000;
        endTimer = 24000;

        bossBar.setTitle("LIGHTS OUT");

        for (var player : Bukkit.getOnlinePlayers()) {
            var hardMode = PrisonGame.hardmode.get(player);
            var role = PrisonGame.roles.get(player);

            if (hardMode && player.hasPotionEffect(PotionEffectType.LUCK)) {
                player.addPotionEffect(PotionEffectType.LUCK.createEffect(20 * 15, 0));
                player.addPotionEffect(PotionEffectType.BAD_OMEN.createEffect(999999, 0));
                player.sendTitlePart(TitlePart.SUBTITLE, PrisonGame.mm.deserialize("<red>You will be respawned at roll call!"));
                player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                        Duration.ZERO,
                        Duration.ofSeconds(3),
                        Duration.ZERO
                ));
            }

            if (role == Role.PRISONER && !PrisonGame.escaped.get(player)) {
                player.addPotionEffect(PotionEffectType.SATURATION.createEffect(20, 3));
                player.getWorld().getWorldBorder().setWarningDistance(Integer.MAX_VALUE);

                if (player.isSleeping())
                    continue;

                if (player.hasPotionEffect(PotionEffectType.LUCK)) {
                    player.sendTitlePart(TitlePart.SUBTITLE, PrisonGame.mm.deserialize("<red>GET TO SLEEP IN A BED OR YOU'LL BE KILLED!"));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                            Duration.ZERO,
                            Duration.ofSeconds(3),
                            Duration.ZERO
                    ));

                    player.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                    player.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                    scoreboard.getTeam("Criminals").addPlayer(player);

                    if (hardMode && player.getHealth() > 1)
                        player.setHealth(1);
                } else {
                    world.setTime(world.getTime() + 2);
                    player.removePotionEffect(PotionEffectType.HUNGER);
                    player.removePotionEffect(PotionEffectType.GLOWING);

                    if (scoreboard.getPlayerTeam(player) == scoreboard.getTeam("Criminals"))
                        scoreboard.getTeam("Prisoners").addPlayer(player);
                }
            }
        }

        world.setTime(world.getTime() + 2);
        bossBar.setColor(BarColor.RED);
        bossBar.addFlag(BarFlag.DARKEN_SKY);
        bossBar.addFlag(BarFlag.CREATE_FOG);
    }
    private void endLightsOut(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.isSleeping()) {
                PrisonGame.saidcycle.put(p, PrisonGame.saidcycle.get(p) + 1);
            }else{
                PrisonGame.saidcycle.put(p, PrisonGame.saidcycle.get(p) - 1);
            }
        }
    }
}
