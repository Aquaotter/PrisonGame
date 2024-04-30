package prisongame.prisongame.commands.staff.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Key;
import prisongame.prisongame.keys.Keys;

import java.util.Arrays;

public class PDCCommand implements CommandExecutor {
    public enum PDCAction {
        GET,
        SET,
        REMOVE
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide an action."));
                return true;
            }

            var action = PDCAction.valueOf(args[0].toUpperCase());

            if (args.length == 1) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
                return true;
            }

            var player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>That player is not online."));
                return true;
            }

            var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(new String[0]);

            return switch (action) {
                case GET -> get(sender, player, rest);
                case SET -> set(sender, player, rest);
                case REMOVE -> remove(sender, player, rest);
            };
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid action! (get/set)"));
            return true;
        }
    }

    private boolean remove(CommandSender sender, Player player, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a key."));
            return true;
        }

        var key = Keys.valueOf(args[1].toUpperCase());

        if (key == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid key."));
            return true;
        }

        key.remove(player);
        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Removed the key."));
        return true;
    }

    private boolean set(CommandSender sender, Player player, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a key."));
            return true;
        }

        var key = Keys.valueOf(args[1].toUpperCase());

        if (key == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid key."));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a value."));
            return true;
        }

        var input = String.join(" ", Arrays.stream(args).toList().subList(2, args.length));
        var value = key.type().parser().apply(input);

        if (value == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid input."));
            return true;
        }

        for (var method : Key.class.getMethods()) {
            if (!method.getName().equals("set") || method.getParameterTypes().length != 2)
                continue;

            try {
                method.invoke(key, player, value);
                sender.sendMessage(PrisonGame.mm.deserialize("<gray>Set the value of the key."));
            } catch (ReflectiveOperationException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred setting the value."));
                PrisonGame.instance.getLogger().severe(exception.getMessage());
                return true;
            }
        }

        return true;
    }

    private boolean get(CommandSender sender, Player player, String[] args) {
        if (args.length > 1) {
            var key = Keys.valueOf(args[1].toUpperCase());

            if (key == null) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid key."));
                return true;
            }

            var value = key.get(player);

            if (value == null) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>There is no value attached to that key."));
                return true;
            }

            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<gray>The value attached is <white><value></white>.",
                    Placeholder.component("value", Component.text(value.toString()))
            ));

            return true;
        }

        try {
            for (var key : Keys.values()) {
                var value = key.get(player);

                if (value == null)
                    continue;

                sender.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><key> <dark_gray>=> </gray><value>",
                        Placeholder.component("key", Component.text(key.name())),
                        Placeholder.component("value", Component.text(key.get(player).toString()))
                ));
            }
        } catch (IllegalArgumentException ignored) {}

        return true;
    }
}
