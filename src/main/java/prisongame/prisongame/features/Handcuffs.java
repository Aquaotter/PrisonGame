package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class Handcuffs implements Feature {
    @Override
    public void schedule() {

    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            for (var entity : player.getPassengers())
                if (entity instanceof Player passenger && !passenger.hasPotionEffect(PotionEffectType.WEAKNESS) && !passenger.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                    player.removePassenger(passenger);
                    player.sendMessage(PrisonGame.mm.deserialize("<red>The handcuffs rotted away..."));
                    passenger.sendMessage(PrisonGame.mm.deserialize("<green>The handcuffs rotted away! <gray>(you're free)"));
                }

            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]")) {
                    if (PrisonGame.roles.get(player) == Role.PRISONER) {
                        player.getInventory().remove(player.getInventory().getItemInMainHand());
                    }
                }
            }

            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                var mainHand = player.getInventory().getItemInMainHand();
                var meta = mainHand.getItemMeta();
                if (meta != null && !meta.getDisplayName().equals(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]")) {
                    player.setWalkSpeed(0.2f);
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.4);
                } else {
                    player.getInventory().getItemInMainHand().setDurability((short) 3);
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(9999);
                }
            }

            if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                if (player.isInsideVehicle() && player.isOnline()) {
                    if (player.getVehicle() instanceof Player) {
                        ((Player) player.getVehicle()).setCooldown(Material.IRON_SHOVEL, 20 * 5);
                        ((Player) player.getVehicle()).hidePlayer(PrisonGame.getPlugin(PrisonGame.class), player);
                    }
                }
                player.setNoDamageTicks(20);
                player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
            }

            if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                player.removePotionEffect(PotionEffectType.GLOWING);
            }
        }
    }
}
