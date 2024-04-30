package prisongame.prisongame.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

import java.util.Random;

public class LinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        if (Keys.LINK.has(player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You are already linked."));
            return true;
        }

        var code = generateCode();
        PrisonGame.linkCodes.put(code, player);
        player.sendMessage(PrisonGame.mm.deserialize(
                "<gray>Run <white>/link <code></white> on our Discord server to link your accounts.",
                Placeholder.component("code", Component.text(code))
        ));

        return true;
    }

    private int generateCode() {
        int code = new Random().nextInt(100000, 999999);

        if (PrisonGame.linkCodes.containsKey(code))
            return generateCode();

        return code;
    }
}
