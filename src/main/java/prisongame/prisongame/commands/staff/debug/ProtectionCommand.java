package prisongame.prisongame.commands.staff.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class ProtectionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (PrisonGame.warden == null || PrisonGame.warden.isDead()) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>There is no warden."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a timer."));
            return true;
        }

        if (!args[0].matches("^\\d+$")) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>That timer isn't valid."));
            return true;
        }

        var timer = Integer.parseInt(args[0]) * 20;

        if (timer < 0) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The timer cannot be below 0 seconds."));
            return true;
        }

        PrisonGame.warden.setNoDamageTicks(timer);

        sender.sendMessage(PrisonGame.mm.deserialize(
                "<gray>Set the warden protection timer to <timer>.",
                Placeholder.component("timer", Component
                        .text(timer / 20 + " seconds")
                        .color(NamedTextColor.WHITE))
        ));

        return true;
    }
}
