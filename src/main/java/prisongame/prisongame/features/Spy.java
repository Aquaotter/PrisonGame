package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.staff.VanishCommand;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.util.Collections;

public class Spy implements Feature {
    @Override
    public void schedule() {scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);}

    @Override
    public void execute() {
        for(Player p : Bukkit.getOnlinePlayers()){
            boolean isHandcuffed = p.hasPotionEffect(PotionEffectType.WEAKNESS) || p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE);
            if(p.hasPotionEffect(PotionEffectType.FAST_DIGGING) && !isHandcuffed){
                p.setInvisible(true);
                p.removePotionEffect(PotionEffectType.GLOWING);
                p.setCustomNameVisible(false);
                for(var player2 : Bukkit.getOnlinePlayers()){
                    var container = player2.getPersistentDataContainer();
                    if (!(container.has(VanishCommand.VANISHED)))
                        if(!(player2.canSee(p)))
                            player2.hidePlayer(p);
                }
            }else{
                p.setInvisible(false);
                for(var player2 : Bukkit.getOnlinePlayers()){
                    var container = player2.getPersistentDataContainer();
                    if (!(container.has(VanishCommand.VANISHED)))
                        player2.showPlayer(p);
                }
            }
            PrisonGame.saidcycle.putIfAbsent(p, 0);
            int saidcyclecount = PrisonGame.saidcycle.get(p);
            if(saidcyclecount >= 3) {
                PrisonGame.saidcycle.put(p, 0);
                if(Keys.NOSPY.get(p, 0)==1){
                    p.sendMessage(PrisonGame.mm.deserialize("<gray>You have not gotten Spy! (Bertude toggle thingy)"));
                }else {
                    giveSpy(p);
                }
            }
            if(PrisonGame.roles.get(p).equals(Role.PRISONER)){

            }else{
                p.removePotionEffect(PotionEffectType.FAST_DIGGING);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }
    private void giveSpy(Player target){
        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0));
        target.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 0));
        ItemStack spyDagger = new ItemStack(Material.IRON_SWORD);
        ItemMeta spyDaggerMeta = spyDagger.getItemMeta();
        spyDaggerMeta.setDisplayName(ChatColor.DARK_GRAY+"Spy Dagger");
        spyDaggerMeta.setUnbreakable(true);
        spyDaggerMeta.setLore(Collections.singletonList(ChatColor.GRAY+"Doesn't Show death messages on kill!"));
        spyDaggerMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        spyDaggerMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
        spyDagger.setItemMeta(spyDaggerMeta);
        target.getInventory().addItem(spyDagger);
    }
}
