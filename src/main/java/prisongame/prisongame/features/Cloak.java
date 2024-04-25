package prisongame.prisongame.features;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class Cloak implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (player.getLocation().getBlock().getType().equals(Material.VOID_AIR)) {
                if (PrisonGame.roles.get(player).equals(0)) {
                    if (!player.hasPotionEffect(PotionEffectType.GLOWING)) {
                        Boolean yesdothat = true;
                        if (player.getInventory().getChestplate() != null) {
                            if (player.getInventory().getChestplate().getItemMeta() != null) {
                                if (player.getInventory().getChestplate().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Cloak")) {
                                    yesdothat = false;
                                }
                            }
                        }
                        if (yesdothat) {
                            for (ItemStack i : player.getInventory()) {
                                if (i != null) {
                                    if (i.getItemMeta().getDisplayName().contains("[CONTRABAND]") || i.getType().equals(Material.STONE_SWORD) || i.getType().equals(Material.IRON_SWORD) || i.getType().equals(Material.IRON_HELMET) || i.getType().equals(Material.IRON_CHESTPLATE) || i.getType().equals(Material.IRON_LEGGINGS) || i.getType().equals(Material.IRON_BOOTS)) {
                                        if (!player.isInsideVehicle() && player.isOnline()) {
                                            player.addPotionEffect(PotionEffectType.GLOWING.createEffect(1200, 0));
                                            player.sendMessage(PrisonGame.mm.deserialize("<red>You were caught with contraband!"));
                                            if (PrisonGame.prisonerlevel.getOrDefault(player, 0) == 1) {
                                                player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(20 * 6, 0));
                                                player.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 6, 0));
                                                player.addPotionEffect(PotionEffectType.DARKNESS.createEffect(20 * 6, 0));
                                            }
                                            for (Player g : Bukkit.getOnlinePlayers()) {
                                                if (PrisonGame.roles.get(g) != Role.PRISONER) {
                                                    g.playSound(g, Sound.ENTITY_SILVERFISH_DEATH, 1, 0.5f);
                                                    g.sendMessage(PrisonGame.mm.deserialize(
                                                        "<dark_red><player> was caught with contraband!",
                                                        Placeholder.component("player", player.name().color(NamedTextColor.RED))
                                                    ));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}
