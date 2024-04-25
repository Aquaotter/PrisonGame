package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.FilteredWords;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.config.filter.FilterAction;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class CreateCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a gang name."));
            return true;
        }

        var name = args[0];

        if (!name.matches("^[0-9a-zA-Z-_ ]{1,16}$")) {
            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<red>Please provide a valid gang name. (<charset>)",
                    Placeholder.component("charset", Component.text("[0-9a-zA-Z-_ ]"))
            ));
            return true;
        }

        var filter = FilteredWords.isClean(name);
        if (filter != null) {
            if (filter.getSecond().getAction() == FilterAction.BLOCK_MESSAGE) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Gangs are currently disabled."));
                return true;
            }

            FilteredWords.alert(player, name, filter.getFirst(), "creating gang");
            sender.sendMessage(PrisonGame.mm.deserialize("<red>That name isn't appropriate."));
            return true;
        }

        try {
            if (Keys.GANG.has(player)) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>You're already in a gang."));
                return true;
            }

            if (Gangs.exists(name)) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>That name is taken."));
                return true;
            }

            Gangs.create(player, name);
            Keys.GANG.set(player, name);
            Keys.GANG_CONTRIBUTION.set(player, 0.0);
            Keys.GANG_ROLE.set(player, GangRole.OWNER.ordinal());
            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<gray>Created gang with name <name>.",
                    Placeholder.component("name", Component
                            .text(name)
                            .color(NamedTextColor.WHITE))
            ));
        } catch (SQLException exception) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }
}
