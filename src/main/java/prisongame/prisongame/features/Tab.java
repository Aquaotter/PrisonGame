package prisongame.prisongame.features;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.staff.VanishCommand;
import prisongame.prisongame.keys.Keys;

import java.text.DecimalFormat;

public class Tab implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var format = new DecimalFormat("#0.0");
        var timer = PrisonGame.mm.deserialize(PrisonGame.warden == null
                ? "(No Warden!)"
                : PrisonGame.wardentime.get(PrisonGame.warden) / (20 * 60) + "m");

        var wardenDisplay = PrisonGame.warden == null
                ? PrisonGame.mm.deserialize("No warden!")
                : PrisonGame.mm.deserialize(
                "<name> <ping>\n<red>(<health> HP <aqua><damage></aqua>) for <time>",
                Placeholder.component("damage", PrisonGame.mm.deserialize(String.format(
                        "[%ss of No-Damage]",
                        PrisonGame.warden.getNoDamageTicks() / 20
                ))),
                Placeholder.component("name", PrisonGame.warden.displayName()),
                Placeholder.component("ping", PrisonGame.getPingDisplay(PrisonGame.warden)),
                Placeholder.component("health", PrisonGame.mm.deserialize("" + Math.round(PrisonGame.warden.getHealth()))),
                Placeholder.component("time", timer)
        );

        var guardCount = 0;
        var prisonerCount = 0;
        var playerCount = 0;
        for(var player : Bukkit.getOnlinePlayers()){
            if (!player.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                playerCount++;
            }
        }

        var guards = Component.empty();
        var prisoners = Component.empty();

        for (var player : Bukkit.getOnlinePlayers()) {
            var role = PrisonGame.roles.get(player);
            switch (role) {
                case NURSE, GUARD, SWAT -> {
                    if (!player.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                        guards = guards.append(player
                                .displayName()
                                .append(Component.space())
                                .append(PrisonGame.getPingDisplay(player))
                                .append(Component.newline()));
                        guardCount++;
                    }
                }
                case PRISONER -> {
                    if (player.isDead() || player.hasPotionEffect(PotionEffectType.LUCK)) {
                        prisoners = prisoners.append(PrisonGame.mm.deserialize(
                                "<dark_red>☠ <gray><player>\n",
                                Placeholder.component("player", player.name())
                        ));
                        prisonerCount++;
                        break;
                    }
                    if (!player.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                        prisoners = prisoners.append(player
                                .displayName()
                                .append(Component.space())
                                .append(PrisonGame.getPingDisplay(player))
                                .append(Component.newline()));
                        prisonerCount++;
                    }
                }
            }
        }

        var tab = PrisonGame.mm.deserialize("""
                <gray>---
                <yellow>PrisonButBad</yellow> - <white>made by agmass, 4950, Goose, and _Aquaotter_!</white>
                <green>Players: <player-count></green>
                <red><warden></red>
                ---
                
                <aqua>Guards (<guard-count>):</aqua>
                
                <guards>
                
                ---
                
                <gold>Prisoners (<prisoner-count>):</gold>
                
                <prisoners>
                <padding>
                """,
                Placeholder.component("player-count", PrisonGame.mm.deserialize("" + playerCount)),
                Placeholder.component("warden", wardenDisplay),
                Placeholder.component("guard-count", PrisonGame.mm.deserialize("" + guardCount)),
                Placeholder.component("guards", guards),
                Placeholder.component("prisoner-count", PrisonGame.mm.deserialize("" + prisonerCount)),
                Placeholder.component("prisoners", prisoners),
                Placeholder.component("padding", PrisonGame.mm.deserialize("\n".repeat(50)))
        );

        for (var player : Bukkit.getOnlinePlayers())
            if (!Keys.OLD_TAB.has(player)) {
                player.sendPlayerListHeader(tab);
            } else {
                player.sendPlayerListHeaderAndFooter(Component.empty(), PrisonGame.mm.deserialize("Imagine using old tab. Actual pussy move ngl"));
            }
    }
}
