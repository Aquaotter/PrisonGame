package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.PrisonGame;

import java.util.Collections;

public class Spy implements Feature {
    @Override
    public void schedule() {scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);}

    @Override
    public void execute() {
        for(Player p : Bukkit.getOnlinePlayers()){
            PrisonGame.saidcycle.putIfAbsent(p, 1);
            int saidcyclecount = PrisonGame.saidcycle.get(p);
            if(saidcyclecount >= 3) {
                PrisonGame.saidcycle.put(p, 0);
                giveSpy(p);
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
