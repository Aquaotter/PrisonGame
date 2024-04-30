package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class TargetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player g = Bukkit.getPlayer(args[0]);
        g.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));

        return true;
    }
}