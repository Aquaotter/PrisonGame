package prisongame.prisongame.commands.economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.danger.staff.SeasonCommand;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

public class BalanceCommand implements CommandExecutor {
    public static final int MINUTES_PER_REFRESH = 5;

    public enum Action {
        LEADERBOARD,
        VIEW

    }
    public Instant lastLeaderboardUpdate = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Component leaderboard = Component.empty();
    public boolean currentlyUpdating = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var action = getAction(args);

        if (action == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide an action. (leaderboard/view)"));
            return true;
        }

        var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(String[]::new);

        return switch (action) {
            case LEADERBOARD -> leaderboard(sender, rest);
            case VIEW -> view(sender, rest);
        };
    }

    private boolean leaderboard(CommandSender sender, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdate.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboard);
            return true;
        }

        if (currentlyUpdating) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating. Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard. This may take a few seconds..."));
        currentlyUpdating = true;

        new Thread(() -> {
            try {
                var moneyCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getMoney(p1, season, moneyCache);
                            var money2 = getMoney(p2, season, moneyCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = 0; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = moneyCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + "$")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdating = false;
                this.leaderboard = message;
                sender.sendMessage(message);
                lastLeaderboardUpdate = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }

    private double getMoney(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            var money = Keys.MONEY.get(player.getPlayer(), 0.0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.MONEY.get(holder, 0.0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }

    private boolean view(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
            return true;
        }

        new Thread(() -> {
            var player = Bukkit.getOfflinePlayer(args[0]);
            var holder = new OfflinePlayerHolder(player);
            var balance = Keys.MONEY.get(holder, 0.0);

            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<gray><player> has <amount>.",
                    Placeholder.component("player", Component
                            .text(player.getName())
                            .color(NamedTextColor.WHITE)),
                    Placeholder.component("amount", Component
                            .text(PrisonGame.formatBalance(balance) + "$")
                            .color(NamedTextColor.GREEN))
            ));
        }).start();

        return true;
    }

    private @Nullable Action getAction(String[] args) {
        if (args.length < 1)
            return null;

        try {
            return Action.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
