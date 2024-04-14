package prisongame.prisongame.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.MyTask;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.discord.listeners.Messages;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.util.Arrays;
import java.util.Random;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            event.getPlayer().sendMessage("Wow! You managed to break a block in survival mode! This means the server is completely broken, or it's reloading. Please tell Aquaotter. Please.");
            event.setCancelled(true);
        } else if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            event.setCancelled(true);
            if (event.getBlock().getType().equals(Material.IRON_BARS)) {
                event.setCancelled(false);
                if (PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER) {
                    Boolean yesdothat = true;
                    if (event.getPlayer().getInventory().getChestplate() != null) {
                        if (event.getPlayer().getInventory().getChestplate().getItemMeta() != null) {
                            if (event.getPlayer().getInventory().getChestplate().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Cloak Chestplate")) {
                                yesdothat = false;
                            }
                        }
                    }
                    if (yesdothat && !event.getPlayer().hasPotionEffect(PotionEffectType.GLOWING) && !Keys.SEMICLOAK.has(event.getPlayer())) {
                        event.getPlayer().sendMessage(ChatColor.RED + "You were caught breaking bars! Get a cloak next time!");
                        for (Player g : Bukkit.getOnlinePlayers()) {
                            if (PrisonGame.roles.get(g) != Role.PRISONER) {
                                g.playSound(g, Sound.ENTITY_SILVERFISH_DEATH, 1, 0.5f);
                                g.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                                g.sendMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.DARK_RED + " was caught breaking bars!");
                            }
                        }
                    }
                }
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    event.getBlock().setType(Material.IRON_BARS);
                }, 20 * 30);
            }
            if (Arrays.asList(PrisonGame.oretypes).contains(event.getBlock().getType())) {
                event.setCancelled(false);
                event.getBlock().setType(Material.DEEPSLATE);
                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                    Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 37.5 * MyTask.jobm);
                }else{
                Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 30.0 * MyTask.jobm);
                }
                event.getPlayer().setCooldown(Material.IRON_PICKAXE, 5);
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    event.getBlock().setType(PrisonGame.oretypes[new Random().nextInt(0, PrisonGame.oretypes.length - 1)]);
                }, 20 * 2);
                Random rand = new Random();
                float chance = 1.0f;
                float comparison = rand.nextFloat() * 100;
                if (chance >= comparison) {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.COAL));
                    rand = new Random();
                }
                chance = 2.0f;
                comparison = rand.nextFloat() * 100;
                if (chance >= comparison) {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.RAW_IRON));
                }
                TryAxe(event.getPlayer());
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
}
