package prisongame.prisongame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;

import java.util.HashMap;
import java.util.UUID;

public class AcceptCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (PrisonGame.askType.getOrDefault((Player) sender, 0)) {
            case 2 -> PrisonGame.setNurse((Player) sender, false);
            case 1 -> PrisonGame.setGuard((Player) sender, false);
            case 3 -> PrisonGame.setSwat((Player) sender, false);
            case -1 -> {
                MyListener.playerJoin(PrisonGame.warden, false);
                PrisonGame.warden = null;
                ((Player) sender).performCommand("warden");

                PrisonGame.askType.entrySet().forEach((entry) -> {
                   if (entry.getValue() == -1)
                       PrisonGame.askType.put(entry.getKey(), 0);
                });
            }
            default -> sender.sendMessage("You haven't been invited!");
        }
        if (PrisonGame.askType.get((Player) sender) > 0) {
            if (!PrisonGame.savedPlayerGuards.containsKey(PrisonGame.warden.getUniqueId())) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Creating warden save file...");
                HashMap<UUID, Integer> roleHashMap = new HashMap<>();
                roleHashMap.put(((Player) sender).getUniqueId(), PrisonGame.askType.get((Player) sender));
                PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
            } else {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Saving warden save file...");
                HashMap<UUID, Integer> roleHashMap = PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId());
                if (PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).containsKey(((Player) sender).getUniqueId())) {
                    roleHashMap.remove(((Player) sender).getUniqueId());
                }
                roleHashMap.put(((Player) sender).getUniqueId(), PrisonGame.askType.get((Player) sender));
                PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
            }
        }


        PrisonGame.askType.put((Player) sender, 0);

        return true;
    }
}