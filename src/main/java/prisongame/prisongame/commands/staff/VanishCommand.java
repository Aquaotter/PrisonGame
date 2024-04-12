package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class VanishCommand implements CommandExecutor {
    public static NamespacedKey VANISHED = new NamespacedKey(PrisonGame.instance, "vanished");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Only players can run this command/"));
            return true;
        }

        var container = player.getPersistentDataContainer();

        if (container.has(VANISHED)) {
            container.remove(VANISHED);

            for (var loopedPlayer : Bukkit.getOnlinePlayers())
                loopedPlayer.showPlayer(PrisonGame.instance, player);

            sender.sendMessage(PrisonGame.mm.deserialize("<gray>You are revealed."));
            return true;
        }

        container.set(VANISHED, PersistentDataType.INTEGER, 1);
        for (var loopedPlayer : Bukkit.getOnlinePlayers())
            loopedPlayer.hidePlayer(PrisonGame.instance, player);

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>You are hidden."));
        return true;
    }
}
