package prisongame.prisongame.listeners;

import me.coralise.spigot.API.CBPAPI;
import me.coralise.spigot.CustomBansPlus;
import me.coralise.spigot.players.CBPlayer;
import me.coralise.spigot.players.PlayerManager;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.danger.staff.SeasonCommand;
import prisongame.prisongame.commands.staff.VanishCommand;
import prisongame.prisongame.discord.listeners.Messages;
import prisongame.prisongame.keys.Key;
import prisongame.prisongame.keys.Keys;

import java.io.IOException;

import static prisongame.prisongame.MyListener.playerJoin;
import static prisongame.prisongame.config.ConfigKt.getConfig;
import static prisongame.prisongame.discord.DiscordKt.guild;
import static prisongame.prisongame.discord.DiscordKt.removeMuted;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoinCBP(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (!Keys.LINK.has(player))
            return;

        var member = guild.getMemberById(Keys.LINK.get(player));

        if (member == null)
            return;

        CBPAPI api = CBPAPI.getApi();

        if (api == null)
            return;

        CustomBansPlus cbp = CustomBansPlus.getInstance();
        PlayerManager playerManager = cbp.plm;

        CBPlayer cbpPlayer = playerManager.getCBPlayer(player.getUniqueId());

        if (api.isPlayerMuted(cbpPlayer))
            return;

        removeMuted(member);
    }

    @EventHandler
    public void onJoinSeason(PlayerJoinEvent event) {
        var player = event.getPlayer();

        try {
            var currentSeason = SeasonCommand.getCurrentSeason();
            var playerSeason = Keys.SEASON.get(player, 0);
            var money = Keys.MONEY.get(player, 0.0);

            if (currentSeason != playerSeason && money > 0) {
                Keys.SEASON.set(player, currentSeason);
                Keys.PREVIOUS_MONEY.set(player, money);
                Keys.MONEY.set(player, 0.0);
                player.sendMessage(PrisonGame.mm.deserialize("\n<red>Your money has been reset due to the start of a new season!\n"));
            }
        } catch (IOException e) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred checking the current season. Please let _Aquaotter_ know on Discord. (@aquaotter)"));
            Bukkit.getConsoleSender().sendMessage(PrisonGame.mm.deserialize("<red>" + e.getMessage()));
        }
        if (event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED)) {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onJoinVanish(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var container = player.getPersistentDataContainer();

        for (var loopedPlayer : Bukkit.getOnlinePlayers()) {
            var loopedContainer = loopedPlayer.getPersistentDataContainer();

            if (container.has(VanishCommand.VANISHED))
                loopedPlayer.hidePlayer(PrisonGame.instance, player);

            if (loopedContainer.has(VanishCommand.VANISHED))
                player.hidePlayer(PrisonGame.instance, loopedPlayer);
        }
        if (event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED)) {
            event.setJoinMessage("");
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //
        if(Keys.PICKAXE_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.PICKAXE_UPGRADE.set(event.getPlayer(), 3);
        if(Keys.SWORD_UPGRADE.get(event.getPlayer(), 0) >= 2) Keys.SWORD_UPGRADE.set(event.getPlayer(), 2);
        if(Keys.PLUMBER_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.PLUMBER_UPGRADE.set(event.getPlayer(), 3);
        if(Keys.SHOVELING_UPGRADE.get(event.getPlayer(), 0) >= 2) Keys.SHOVELING_UPGRADE.set(event.getPlayer(), 2);
        //
        if(event.getPlayer().isInvulnerable()) event.getPlayer().setInvulnerable(false);
        if (!getConfig().getDev() && !event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED))
            Messages.INSTANCE.onJoin(event.getPlayer());
        if (PrisonGame.wardenenabled) {
            Player p = event.getPlayer();
            PrisonGame.trustlevel.put(event.getPlayer(), 0);
            DisguiseAPI.undisguiseToAll(event.getPlayer());
            event.getPlayer().removePotionEffect(PotionEffectType.DARKNESS);
            event.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
            event.setJoinMessage(ChatColor.GOLD + event.getPlayer().getName() + " was caught and sent to prison! (JOIN)");
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            PrisonGame.st.put(event.getPlayer(), 0.0);
            PrisonGame.sp.put(event.getPlayer(), 0.0);
            playerJoin(event.getPlayer(), false);
            if (event.getPlayer().hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
                Player g = event.getPlayer();
                Bukkit.broadcastMessage(ChatColor.GRAY + g.getName() + " was sent to solitary!");
                g.setGameMode(GameMode.ADVENTURE);
                g.removePotionEffect(PotionEffectType.LUCK);
                PrisonGame.solittime.put(g, 20 * 120);
                PrisonGame.escaped.put(g, true);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + g.getName() + " only prison:solit");
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    g.teleport(PrisonGame.active.getSolitary().getLocation());
                }, 10);
                g.sendTitle("", "You're in solitary.", 10, 0, 10);
                g.addPotionEffect(PotionEffectType.WATER_BREATHING.createEffect(Integer.MAX_VALUE, 1));
                Player pe = g;
                pe.setCustomName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + pe.getName());
                pe.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + pe.getName());
                pe.setDisplayName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + pe.getName());

                event.setJoinMessage(ChatColor.RED + event.getPlayer().getName() + " was caught and sent back to solitary! (JOIN)");
            }
        }else{
            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', "&4&lThe server is currently &a&lReloading &c&lOr &4Completely messed up. &c&lIf this error is occurring constantly please alert me @aquaotter &bhttps://discord.gg/GrcHKkFQsv"));
        }
        Player pe = (Player) event.getPlayer();
        if (PrisonGame.warden != null && PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).containsKey(pe.getUniqueId()) && !pe.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
            switch (PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).get(pe.getUniqueId())) {
                case 2 -> PrisonGame.setNurse((Player) pe, false);
                case 1 -> PrisonGame.setGuard((Player) pe, false);
                case 3 -> PrisonGame.setSwat((Player) pe, false);
                default -> ((Player) pe).sendMessage("An error has occurred.");
            }
        }
        if (event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED)) {
            event.setJoinMessage("");
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("pbb.staff.vanish.seejoin")) p.sendMessage(ChatColor.RED+"[STAFF] "+event.getPlayer().getName()+" Joined in Vanish!");
            }
        }
    }
}
