package prisongame.prisongame.commands.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.GangChatCommand;
import prisongame.prisongame.commands.danger.staff.SeasonCommand;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

public class LeaderboardCommand implements CommandExecutor { // TODO Find a better way to do this... -Aquaotter
    public static final int MINUTES_PER_REFRESH = 5;

    public enum Action {
        PLAYTIME,
        MONEY,
        BELLS,
        KILLS,
        DEATHS,
        KDR,
        ALLWARDENTIME,
        SHOVELING,
        MINING,
        BOUNTY,
        LUMBERJACK,
        PLUMBER,
        CODCOOKER,
        ESCAPECOUNT


    }
    public Instant lastLeaderboardUpdatePlaytime = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateEscapeCount = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateBells = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateMoney = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateKills = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateDeaths = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateKDR = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateAllWardenTIme = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateMining = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdatePlumber = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateCodCooker = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES); // i love that name -aqua
    public Instant lastLeaderboardUpdateLumberJack = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateBounty = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Instant lastLeaderboardUpdateShoveling = Instant.now().minus(MINUTES_PER_REFRESH * 2, ChronoUnit.MINUTES);
    public Component leaderboardplaytime = Component.empty();
    public Component leaderboardEscapeCount = Component.empty();
    public Component leaderboardBellsRung = Component.empty();
    public Component leaderboardmoney = Component.empty();
    public Component leaderboardKills = Component.empty();
    public Component leaderboardDeaths = Component.empty();
    public Component leaderboardKDR = Component.empty();
    public Component leaderboardAllWardenTime = Component.empty();
    public Component leaderboardMining = Component.empty();
    public Component leaderboardPlumber = Component.empty();
    public Component leaderboardCodCooker = Component.empty();
    public Component leaderboardLumber = Component.empty();
    public Component leaderboardBounty = Component.empty();
    public Component leaderboardShoveling = Component.empty();
    public boolean currentlyUpdatingPlaytime = false;
    public boolean currentlyUpdatingEscapeCount = false;
    public boolean currentlyUpdatingMoney = false;
    public boolean currentlyUpdatingBellsRung = false;
    public boolean currentlyUpdatingDeaths = false;
    public boolean currentlyUpdatingKills = false;
    public boolean currentlyUpdatingKDR = false;
    public boolean currentlyUpdatingAllWardenTime = false;

    public boolean currentlyUpdatingMining = false;
    public boolean currentlyUpdatingCodCooker = false;
    public boolean currentlyUpdatingLumberjAck = false;
    public boolean currentlyUpdatingPlumber = false;
    public boolean currentlyUpdatingBounty = false;
    public boolean currentlyUpdatingShoveling = false;




    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var action = getAction(args);
        int page;

        if (action == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide an action."));
            return true;
        }
        if(!(args.length == 1) && !args[1].isEmpty()) {
            page = Integer.parseInt(args[1]) * 10;
        }else{
            page = 0;
        }
        var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(String[]::new);

        return switch (action) {
            case MONEY -> veiwleaderboardmoney(sender, page, rest);
            case PLAYTIME -> veiwleaderboardplaytime(sender, page, rest);
            case ESCAPECOUNT -> veiwleaderboardEscapeCount(sender, page, rest);
            case BELLS -> veiwLeaderboardBellsRung(sender, page, rest);
            case KILLS -> veiwLeaderboardKills(sender, page, rest);
            case DEATHS -> veiwLeaderboardDeaths(sender, page, rest);
            case KDR -> veiwLeaderboardKDR(sender, page, rest);
            case ALLWARDENTIME -> veiwLeaderboardAllWardenTIme(sender, page, rest);
            case LUMBERJACK -> veiwLeaderboardLumberJack(sender, page, rest);
            case PLUMBER -> veiwLeaderboardPlumber(sender, page, rest);
            case BOUNTY -> veiwLeaderboardBounty(sender, page, rest);
            case MINING  -> veiwLeaderboardMining(sender, page, rest);
            case CODCOOKER -> veiwLeaderboardCodCooker(sender, page, rest);
            case SHOVELING -> veiwLeaderboardShoveling(sender, page, rest);
        };
    }

    private boolean veiwleaderboardmoney(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateMoney.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardmoney);
            return true;
        }

        if (currentlyUpdatingMoney) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Money). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Money). This may take a few seconds..."));
        currentlyUpdatingMoney = true;

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
                for (int i = page; i < 10; i++) {
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


                currentlyUpdatingMoney = false;
                this.leaderboardmoney = message;
                sender.sendMessage(message);
                lastLeaderboardUpdateMoney = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwleaderboardplaytime(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdatePlaytime.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardplaytime);
            return true;
        }

        if (currentlyUpdatingPlaytime) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Playtime). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Playtime). This may take a few seconds..."));
        currentlyUpdatingPlaytime = true;

        new Thread(() -> {
            try {
                var playtimeHoursCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getPlaytimeHours(p1, season, playtimeHoursCache);
                            var money2 = getPlaytimeHours(p2, season, playtimeHoursCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = playtimeHoursCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Hours")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingPlaytime = false;
                this.leaderboardplaytime = message;
                sender.sendMessage(message);
                lastLeaderboardUpdatePlaytime = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwleaderboardEscapeCount(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateEscapeCount.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardEscapeCount);
            return true;
        }

        if (currentlyUpdatingEscapeCount) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Escape Count). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Escape Count). This may take a few seconds..."));
        currentlyUpdatingEscapeCount = true;

        new Thread(() -> {
            try {
                var EscapeCountCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getEscapeCount(p1, season, EscapeCountCache);
                            var money2 = getEscapeCount(p2, season, EscapeCountCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = EscapeCountCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Escapes")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingEscapeCount = false;
                this.leaderboardEscapeCount = message;
                sender.sendMessage(message);
                lastLeaderboardUpdateEscapeCount = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardBellsRung(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateBells.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardBellsRung);
            return true;
        }

        if (currentlyUpdatingBellsRung) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Bells). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Bells). This may take a few seconds..."));
        currentlyUpdatingBellsRung = true;

        new Thread(() -> {
            try {
                var BellsRungCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = GetBellsRang(p1, season, BellsRungCache);
                            var money2 = GetBellsRang(p2, season, BellsRungCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = BellsRungCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Bells Rung")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingBellsRung = false;
                this.leaderboardBellsRung = message;
                sender.sendMessage(message);
                lastLeaderboardUpdateBells = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardKills(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateKills.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardKills);
            return true;
        }

        if (currentlyUpdatingKills) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Kills). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Kills). This may take a few seconds..."));
        currentlyUpdatingKills = true;

        new Thread(() -> {
            try {
                var KillsCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getKills(p1, season, KillsCache);
                            var money2 = getKills(p2, season, KillsCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = KillsCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Kills")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingKills = false;
                this.leaderboardKills = message;
                sender.sendMessage(message);
                lastLeaderboardUpdateKills = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardDeaths(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateDeaths.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardDeaths);
            return true;
        }

        if (currentlyUpdatingDeaths) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Deaths). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Deaths). This may take a few seconds..."));
        currentlyUpdatingDeaths = true;

        new Thread(() -> {
            try {
                var DeathsCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getDeaths(p1, season, DeathsCache);
                            var money2 = getDeaths(p2, season, DeathsCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = DeathsCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Deaths")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingDeaths = false;
                this.leaderboardDeaths = message;
                sender.sendMessage(message);
                lastLeaderboardUpdateDeaths = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardKDR(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateKDR.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardKDR);
            return true;
        }

        if (currentlyUpdatingKDR) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (KDR). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (KDR). This may take a few seconds..."));
        currentlyUpdatingKDR = true;

        new Thread(() -> {
            try {
                var KDRCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getKDR(p1, season, KDRCache);
                            var money2 = getKDR(p2, season, KDRCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = KDRCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " KDR")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingKDR = false;
                this.leaderboardKDR= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateKDR = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardAllWardenTIme(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateAllWardenTIme.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardAllWardenTime);
            return true;
        }

        if (currentlyUpdatingAllWardenTime) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (All Warden Time). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (All Warden Time). This may take a few seconds..."));
        currentlyUpdatingAllWardenTime = true;

        new Thread(() -> {
            try {
                var AllWardenTimeCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getAllWardenTime(p1, season, AllWardenTimeCache);
                            var money2 = getAllWardenTime(p2, season, AllWardenTimeCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = AllWardenTimeCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " LifeTime Warden Minutes")
                                    .color(NamedTextColor.GREEN))
                    ));
                }

                currentlyUpdatingAllWardenTime = false;
                this.leaderboardAllWardenTime= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateAllWardenTIme = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardMining(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateMining.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardMining);
            return true;
        }

        if (currentlyUpdatingMining) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Mining). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Mining). This may take a few seconds..."));
        currentlyUpdatingMining = true;

        new Thread(() -> {
            try {
                var MiningCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getMiningCount(p1, season, MiningCache);
                            var money2 = getMiningCount(p2, season, MiningCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = MiningCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Blocks Mined (Miner Job)")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingMining = false;
                this.leaderboardMining= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateMining = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardShoveling(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateShoveling.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardShoveling);
            return true;
        }

        if (currentlyUpdatingShoveling) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Shoveling). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Shoveling). This may take a few seconds..."));
        currentlyUpdatingShoveling = true;

        new Thread(() -> {
            try {
                var ShovelingCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getShovelingCount(p1, season, ShovelingCache);
                            var money2 = getShovelingCount(p2, season, ShovelingCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = ShovelingCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Shoveled Blocks")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingShoveling = false;
                this.leaderboardShoveling= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateShoveling = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardCodCooker(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateCodCooker.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardCodCooker);
            return true;
        }

        if (currentlyUpdatingCodCooker) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Cod Cooker). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Cod Cooker). This may take a few seconds..."));
        currentlyUpdatingCodCooker = true;

        new Thread(() -> {
            try {
                var CodCookerCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getCodCookedCount(p1, season, CodCookerCache);
                            var money2 = getCodCookedCount(p2, season, CodCookerCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = CodCookerCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Cooked Cod")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingCodCooker = false;
                this.leaderboardCodCooker= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateCodCooker = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardPlumber(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdatePlumber.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardPlumber);
            return true;
        }

        if (currentlyUpdatingPlumber) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Plumber). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Plumber). This may take a few seconds..."));
        currentlyUpdatingPlumber= true;

        new Thread(() -> {
            try {
                var PlumberCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getPlumberCount(p1, season, PlumberCache);
                            var money2 = getPlumberCount(p2, season, PlumberCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = PlumberCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Plumbed")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingPlumber = false;
                this.leaderboardPlumber= message;
                sender.sendMessage(message);
                lastLeaderboardUpdatePlumber = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardLumberJack(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateLumberJack.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardLumber);
            return true;
        }

        if (currentlyUpdatingLumberjAck) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (LumberJack). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (LumberJack). This may take a few seconds..."));
        currentlyUpdatingLumberjAck = true;

        new Thread(() -> {
            try {
                var LumberJackCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getAxeCount(p1, season, LumberJackCache);
                            var money2 = getAxeCount(p2, season, LumberJackCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = LumberJackCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Chopped Wood")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingLumberjAck = false;
                this.leaderboardLumber= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateLumberJack = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }
    private boolean veiwLeaderboardBounty(CommandSender sender, int page, String[] args) {
        if (Instant.now().getEpochSecond() - lastLeaderboardUpdateBounty.getEpochSecond() < MINUTES_PER_REFRESH * 60) {
            sender.sendMessage(leaderboardBounty);
            return true;
        }

        if (currentlyUpdatingBounty) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The leaderboard is currently updating (Bounty). Please try again in a few seconds."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize("<gray>Updating the leaderboard (Bounty). This may take a few seconds..."));
        currentlyUpdatingBounty = true;

        new Thread(() -> {
            try {
                var BountyCache = new HashMap<OfflinePlayer, Double>();
                var season = SeasonCommand.getCurrentSeason();
                var leaderboard = Arrays.stream(Bukkit.getOfflinePlayers())
                        .toList()
                        .stream()
                        .sorted((p1, p2) -> {
                            var money1 = getSwordmanCount(p1, season, BountyCache);
                            var money2 = getSwordmanCount(p2, season, BountyCache);

                            if (money2 < money1)
                                return -1;
                            else if (money2 == money1)
                                return 0;
                            else return 1;
                        })
                        .toList();

                var message = Component.empty();

                for (int i = page; i < 10; i++) {
                    if (i >= leaderboard.size())
                        break;

                    if (!message.equals(Component.empty()))
                        message = message.append(Component.newline());

                    var player = leaderboard.get(i);
                    var money = BountyCache.get(player);

                    message = message.append(PrisonGame.mm.deserialize(
                            "<position> <player> <dark_gray>-</dark_gray> <money>",
                            Placeholder.component("position", Component
                                    .text("#" + (i + 1))
                                    .color(NamedTextColor.GRAY)),
                            Placeholder.component("player", Component.text(player.getName())),
                            Placeholder.component("money", Component
                                    .text(PrisonGame.formatBalance(money) + " Bounty Kills")
                                    .color(NamedTextColor.GREEN))
                    ));
                }


                currentlyUpdatingBounty = false;
                this.leaderboardBounty= message;
                sender.sendMessage(message);
                lastLeaderboardUpdateBounty = Instant.now();
            } catch (IOException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Failed to fetch the current season."));
            }
        }).start();

        return true;
    }


    private @Nullable LeaderboardCommand.Action getAction(String[] args) {
        if (args.length < 1)
            return null;

        try {
            return LeaderboardCommand.Action.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException exception) {
            return null;
        }
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
    private double getPlaytimeHours(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60/60;

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60/60;

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double GetBellsRang(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) player.getStatistic(Statistic.BELL_RING);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : player.getStatistic(Statistic.BELL_RING);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getKills(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) player.getStatistic(Statistic.PLAYER_KILLS);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : player.getStatistic(Statistic.PLAYER_KILLS);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getDeaths(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) player.getStatistic(Statistic.DEATHS);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : player.getStatistic(Statistic.DEATHS);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getKDR(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) player.getStatistic(Statistic.PLAYER_KILLS)/player.getStatistic(Statistic.DEATHS);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : player.getStatistic(Statistic.PLAYER_KILLS)/player.getStatistic(Statistic.DEATHS);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getAllWardenTime(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.ALLWARDENTIME.get(player.getPlayer(), 0)/20/60/60;

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.ALLWARDENTIME.get(player.getPlayer(), 0)/20/60/60;

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getShovelingCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.SHOVELING_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.SHOVELING_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getMiningCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.PICKAXE_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.PICKAXE_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getAxeCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.FOARGING_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.FOARGING_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getPlumberCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.PLUMBER_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.PLUMBER_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getSwordmanCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.SWORDMAN_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.SWORDMAN_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getCodCookedCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.COOKEDFISH_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.COOKEDFISH_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
    private double getEscapeCount(OfflinePlayer player, int season, HashMap<OfflinePlayer, Double> cache) {
        if (player.isOnline()) {
            double money = (double) Keys.ESCAPE_COUNT.get(player.getPlayer(), 0);

            if (money == Double.POSITIVE_INFINITY)
                money = 0.0;

            cache.put(player, money);
            return money;
        }

        if (cache.containsKey(player))
            return cache.get(player);

        var holder = new OfflinePlayerHolder(player);
        var playerSeason = Keys.SEASON.get(holder, 0);
        var money = playerSeason != season ? 0.0 : Keys.ESCAPE_COUNT.get(player.getPlayer(), 0);

        if (money == Double.POSITIVE_INFINITY)
            money = 0.0;

        cache.put(player, money);
        return money;
    }
}
