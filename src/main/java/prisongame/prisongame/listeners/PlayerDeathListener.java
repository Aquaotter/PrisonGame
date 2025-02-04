package prisongame.prisongame.listeners;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.discord.listeners.Messages;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.util.Random;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onWardenDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();
        var killer = player.getKiller();

        if (PrisonGame.roles.get(player) != Role.WARDEN)
            return;

        if (killer != null && killer.getInventory().getItemInMainHand().isEmpty()) {
            var advancement = Bukkit.getAdvancement(new NamespacedKey("prison", "disrespect_at_its_limit"));
            player.getAdvancementProgress(advancement).awardCriteria("no");
        }

        var lastDamageCause = player.getLastDamageCause();
        if (lastDamageCause != null && lastDamageCause.getCause() == EntityDamageEvent.DamageCause.FALL) {
            var advancement = Bukkit.getAdvancement(new NamespacedKey("prison", "light_as_a_feather"));
            player.getAdvancementProgress(advancement).awardCriteria("no");
        }
    }

    @EventHandler
    public void onPlayerDeath2(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if(p.isDead()) {
            var killer = p.getKiller();

            if (killer == null)
                return;

            var inventory = killer.getInventory();
            var mainHand = inventory.getItemInMainHand();
            var meta = mainHand.getItemMeta();

            if (
                    !meta.getDisplayName().equals(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]") ||
                    killer.hasCooldown(Material.IRON_SHOVEL) ||
                    killer.hasPotionEffect(PotionEffectType.UNLUCK) ||
                    !p.getPassengers().isEmpty()
            ) {
                p.getKiller().playSound(p.getKiller(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);
                if (p.hasPotionEffect(PotionEffectType.GLOWING)) {
                    double GainCash = 0;
                    double bountylvl = Keys.SWORD_UPGRADE.get(p, 0);
                    if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                        if(bountylvl==1) GainCash = 212.0;
                        if(bountylvl==2) GainCash = 343.75;
                        if(bountylvl==0) GainCash = 125.0;
                        if(killer.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) GainCash = 375.0;
                    }else {
                        if(bountylvl==1) GainCash = 170.0;
                        if(bountylvl==2) GainCash = 275.0;
                        if(bountylvl==0) GainCash = 100.0;
                        if(killer.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) GainCash = 300.0;
                    }
                    Keys.MONEY.set(p.getKiller(), Keys.MONEY.get(p.getKiller(), 0.0) + GainCash);
                    p.getKiller().sendMessage(ChatColor.GREEN + "You gained a little bit of money for killing a criminal. "+ChatColor.GRAY+"("+GainCash+")");
                } else {
                    if (PrisonGame.roles.get(p.getKiller()) == Role.PRISONER) {
                        p.getKiller().addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 5, 0));
                    }
                }
                // =-=-= Criminal Kills Guard/Warden/Swat gets money! (This is prob better way to do this... NOTE)
                boolean IsEscaped = PrisonGame.roles.get(killer) == Role.PRISONER && PrisonGame.escaped.get(killer);
                String roleKilled = "None";
                double paidedamount = 0;
                double bountylvl = Keys.SWORD_UPGRADE.get(p, 0);
                double increasearg = 1;
                double wardenDeathAmount = 750;
                double guardDeathAmount = 150;
                double swatDeathAmount = 300;   
                double nurseDeathAmount = 100;
                if(bountylvl==1) increasearg=1.5;
                if(bountylvl==2) increasearg=2;
                if(killer.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) increasearg = 2.5;
                if(IsEscaped){
                    if(PrisonGame.roles.get(p) == Role.WARDEN){
                        paidedamount=wardenDeathAmount;
                        Keys.MONEY.set(p.getKiller(), Keys.MONEY.get(p.getKiller(), 0.0) + wardenDeathAmount);
                        roleKilled = ChatColor.RED+"Warden";
                    }
                    if(PrisonGame.roles.get(p) == Role.SWAT){
                        paidedamount=(swatDeathAmount*increasearg)+swatDeathAmount;
                        Keys.MONEY.set(p.getKiller(), Keys.MONEY.get(p.getKiller(), 0.0) + paidedamount);
                        roleKilled = ChatColor.GRAY+"Swat";
                    }
                    if(PrisonGame.roles.get(p) == Role.GUARD){
                        paidedamount=(guardDeathAmount*increasearg)+guardDeathAmount;
                        Keys.MONEY.set(p.getKiller(), Keys.MONEY.get(p.getKiller(), 0.0) + paidedamount);
                        roleKilled = ChatColor.BLUE+"Guard";
                    }
                    if(PrisonGame.roles.get(p) == Role.NURSE){
                        paidedamount=(nurseDeathAmount*increasearg)+nurseDeathAmount;
                        Keys.MONEY.set(p.getKiller(), Keys.MONEY.get(p.getKiller(), 0.0) + paidedamount);
                        roleKilled = ChatColor.LIGHT_PURPLE+"Nurse";
                    }
                    if(!(PrisonGame.roles.get(p) == Role.PRISONER)){
                        killer.sendMessage(ChatColor.GREEN + "You gained a little bit of money for killing a guard role. "+ChatColor.GRAY+"("+paidedamount+")");
                    }
                }
                TryAxe(event.getPlayer());
                // =-=-=
            }
        }
    }
    public void TryAxe(Player p){
        Boolean IsEscaped = PrisonGame.roles.get(p) == Role.PRISONER && PrisonGame.escaped.get(p);
        Random rand = new Random();
        float crimchance = 0.0133315f;
        float chance = 	0.00010f;
        if(IsEscaped)chance = crimchance;
        float comparison = rand.nextFloat() * 100;
        if(chance >= comparison){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" netherite_axe{Unbreakable:1,display:{Name:'[{\"text\":\"Brachydios\",\"bold\":true,\"color\":\"gold\"}]',Lore:['[{\"text\":\"\",\"italic\":false}]','[{\"text\":\"\",\"italic\":false}]','[{\"text\":\"(1 in 25,000)\",\"italic\":false}]']},Enchantments:[{id:vanishing_curse,lvl:1},{id:knockback,lvl:1},{id:sharpness,lvl:2}]} 1");
            Bukkit.broadcastMessage(ChatColor.GOLD+p.getName() + " got Branchy axe (RUN FOR YOUR LIFE)");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var killer = player.getKiller();

        for (var passenger : player.getPassengers()) {
            if (!(passenger instanceof Player playerPassenger))
                continue;

            playerPassenger.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        }
        if (killer != null) {
            if (!killer.equals(player) ) {
                var inventory = killer.getInventory();
                var mainHand = inventory.getItemInMainHand();
                var meta = mainHand.getItemMeta();
                boolean isHandCuffOrBm = killer.hasPotionEffect(PotionEffectType.WEAKNESS) && killer.hasPotionEffect(PotionEffectType.UNLUCK) && killer.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE);
                boolean hasAnotherPlayerHandcuff = killer.getPassengers().isEmpty();
                if (mainHand.getType().equals(Material.IRON_SHOVEL) && !killer.hasCooldown(Material.IRON_SHOVEL) && !isHandCuffOrBm && hasAnotherPlayerHandcuff) {
                    event.setCancelled(true);
                    event.getEntity().addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 30, 255));
                    event.getEntity().addPotionEffect(PotionEffectType.DOLPHINS_GRACE.createEffect(20 * 30, 255));
                    event.getEntity().sendTitle(ChatColor.RED + "HANDCUFFED!", ChatColor.GOLD+"by "+killer.getName(), 20, 160, 20);
                    PlayerDisguise playerDisguise = new PlayerDisguise(event.getPlayer().getName());
                    playerDisguise.setKeepDisguiseOnPlayerDeath(false);
                    DisguiseAPI.disguiseToAll(event.getEntity(), playerDisguise);
                    Player p = event.getEntity();
                    p.getKiller().addPassenger(p);
                    p.hidePlayer(PrisonGame.instance, killer);
                    event.getEntity().getKiller().sendMessage(ChatColor.GREEN + "Shift to drop players.");
                    return;
                }
            }
            PrisonGame.killior.put(event.getEntity(), event.getEntity().getKiller());
        }
        event.getDrops().removeIf(i ->
                i.getType() == Material.TRIPWIRE_HOOK ||
                i.getType() == Material.WOODEN_AXE ||
                i.getType() == Material.WOODEN_SWORD ||
                i.getType() == Material.CARROT_ON_A_STICK ||
                i.getType() == Material.IRON_DOOR ||
                i.getType() == Material.STONE_BUTTON ||
                i.getType() == Material.BOWL ||
                i.getType() == Material.GLASS_BOTTLE ||
                i.getType() == Material.IRON_SHOVEL ||
                i.getType() == Material.BUCKET ||
                i.getEnchantments().containsKey(Enchantment.VANISHING_CURSE) ||
                (i.getItemMeta() != null && i.getItemMeta().getDisplayName().contains("Prisoner Uniform")));
        if (Bukkit.getWorld("world").getTime() > 16000 && Bukkit.getWorld("world").getTime() < 24000) {
            if (event.getPlayer().getKiller() != null) {
                if (event.getPlayer().getKiller().getInventory().getItemInMainHand().getType().equals(Material.TORCH)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:lightsin");
                }
            }
        }
        if (PrisonGame.roles.get(event.getEntity()) == Role.SWAT) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:invincible");
        }
        if (PrisonGame.roles.get(event.getEntity()) != Role.PRISONER) {
            if (event.getEntity().getKiller() != null) {

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:killstaff");
//                PrisonGame.axekills.put(event.getEntity().getKiller(), PrisonGame.axekills.get(event.getEntity().getKiller()) + 1);
                PrisonGame.worryachieve.put(event.getEntity().getKiller(), 0);
//                if (PrisonGame.axekills.get(event.getEntity().getKiller()) == 5) {
//                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:oneman");
//                }
            }
        }
        if (PrisonGame.roles.get(event.getEntity()) == Role.PRISONER) {
            if (event.getEntity().getKiller() != null) {
                if (PrisonGame.roles.get(event.getEntity().getKiller()) == Role.WARDEN) {
                    if (event.getEntity().getKiller().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:yoink");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:skillissue");
                    }
                }
            }
        }
        if (PrisonGame.warden != null) {
            if (PrisonGame.warden.equals(event.getEntity())) {
                if (event.getEntity().getKiller() != null) {
                    if (PrisonGame.roles.get(event.getEntity().getKiller()) != Role.PRISONER) {
                        if (PrisonGame.roles.get(event.getEntity().getKiller()) == Role.NURSE) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:incorrectmogus");
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:dieguard");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:killwardenguard");
                        if (PrisonGame.active.getName().equals("Skeld")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:impostor");
                        }
                    }
                    if(PrisonGame.roles.get(event.getEntity().getKiller()) == Role.GUARD){
                        PrisonGame.axekills.putIfAbsent(event.getPlayer(), 0);
                        PrisonGame.axekills.put(player, PrisonGame.axekills.get(player) + 1);
                        if(PrisonGame.axekills.get(player) >= 5){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:oneman");
                        }else if(PrisonGame.axekills.get(player) >= 1){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:killstaff");
                        }
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:killwarden");
                }
                if(PrisonGame.wardentime.get(event.getEntity()) / 20 / 60 == 155){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:failed_attempt");
                }
                if (PrisonGame.wardentime.get(event.getEntity()) / 20 / 60 >= 120) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:afinishedstory");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:pbb");
                    }
                }
                PrisonGame.wardenCooldown = 40;
                event.getDrops().clear();
                PrisonGame.warden = null;
                PrisonGame.roles.put(event.getEntity(), Role.PRISONER);
                MyListener.playerJoin(event.getEntity(), false);
            }
        }
        if (!getConfig().getDev())
            Messages.INSTANCE.onDeath(player, killer, ChatColor.stripColor(event.getDeathMessage()));
        if (PrisonGame.roles.get(event.getEntity()) == Role.PRISONER) {
            if(killer.hasPotionEffect(PotionEffectType.FAST_DIGGING)){
                if(killer.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD) ) {
                    event.setDeathMessage(null);
                }else{
                    event.setDeathMessage(ChatColor.GRAY + event.getDeathMessage());
                }
            }else{
                event.setDeathMessage(ChatColor.GRAY + event.getDeathMessage());
            }
            if (event.getEntity().getKiller() != null) {
                if (PrisonGame.roles.get(event.getEntity().getKiller()) == Role.WARDEN && event.getEntity().hasPotionEffect(PotionEffectType.UNLUCK)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getName() + " only prison:badluck");
                }
                if (PrisonGame.escaped.get(event.getEntity().getKiller())) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getEntity().getKiller().getName() + " only prison:nmng");
                }
            }
        }
        if (PrisonGame.roles.get(event.getEntity()) != Role.PRISONER) {
            if(killer.hasPotionEffect(PotionEffectType.FAST_DIGGING)){
                if(killer.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD) ) {
                    event.setDeathMessage(null);
                }else{
                    event.setDeathMessage(ChatColor.GOLD + event.getDeathMessage());
                }
            }else{
                event.setDeathMessage(ChatColor.GOLD + event.getDeathMessage());
            }
        }
    }
}
