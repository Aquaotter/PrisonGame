package prisongame.prisongame.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

import java.util.HashMap;
import java.util.UUID;

public class ResignCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!((Player) sender).hasCooldown(Material.IRON_DOOR)) {
            Player p = (Player) sender;
            if(p.hasPotionEffect(PotionEffectType.WEAKNESS) || p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)){
                boolean onRollCall = p.hasPotionEffect(PotionEffectType.JUMP);
                String onWhat = "Handcuffed!";
                if(onRollCall) onWhat = "on RollCall!";
                p.sendMessage(PrisonGame.mm.deserialize("<red>You can't resign while being "+onWhat));
                return true;
            }
            if (!((Player) sender).getDisplayName().contains("SOLITARY") && !new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()).getBlock().getType().equals(Material.RED_SAND) && !p.hasPotionEffect(PotionEffectType.WEAKNESS) && !p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                if (PrisonGame.warden != null) {
                    if (PrisonGame.warden.equals(sender)) {
                        PrisonGame.wardenCooldown = 20 * 3;
                        PrisonGame.warden = null;
                    }
                }
                if (PrisonGame.warden != null) {
                    if (PrisonGame.savedPlayerGuards.containsKey(PrisonGame.warden.getUniqueId())) {
                        HashMap<UUID, Integer> roleHashMap = PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId());
                        if (PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).containsKey(((Player) sender).getUniqueId())) {
                            roleHashMap.remove(((Player) sender).getUniqueId());
                        }
                        PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
                    }
                }
                PrisonGame.roles.put((Player) sender, Role.PRISONER);
                ((Player) sender).clearActivePotionEffects();
                MyListener.playerJoin((Player) sender, false);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You're in combat!");
        }
        return true;
    }
}