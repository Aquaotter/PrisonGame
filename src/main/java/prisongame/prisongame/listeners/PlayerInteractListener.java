package prisongame.prisongame.listeners;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.MyTask;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.config.Prison;
import prisongame.prisongame.features.Schedule;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.util.*;

import static prisongame.prisongame.MyListener.playerJoinignoreAsc;
import static prisongame.prisongame.config.ConfigKt.getConfig;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onSmokerAnvil(PlayerInteractEvent event) {
        var block = event.getClickedBlock();

        if (block == null)
            return;

        var type = block.getType();

        if (type == Material.SMOKER || type == Material.ANVIL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (PrisonGame.active.getName().equals("The End?"))
                event.getPlayer().sendMessage("Wow! You managed to interact with a block in survival mode! This means the server is completely fucking broken, or it's reloading. Please tell agmass. Please.");
            event.setCancelled(true);
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
        if (event.getPlayer().getActivePotionEffects().contains(PotionEffectType.CONFUSION)) {
            event.setCancelled(true);
            return;
        }
        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.IRON_DOOR)) {
                if (!event.getPlayer().hasCooldown(Material.IRON_DOOR)) {
                    if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
                        event.getPlayer().setCooldown(Material.IRON_DOOR, 20);
                        if (PrisonGame.warden != event.getPlayer()) {
                            event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 1924, -60, -2027));
                        }
                    }
                }
            }
        }
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().equals(Material.BAMBOO_DOOR)) {
                if (PrisonGame.active.getName().equals("Barreled")) {
                    if (!Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"),-1023,-57,-994)).getType().equals(Material.AIR)) {
                        event.setCancelled(true);
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.BELL)) {
                if (PrisonGame.active.getName().equals("Barreled")) {
                    if (PrisonGame.roles.get(event.getPlayer()).equals(Role.PRISONER)) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "Bell.");
                        event.getPlayer().setMaxHealth(10);
                        event.getPlayer().getInventory().clear();
                        event.getPlayer().getInventory().setHelmet(new ItemStack(Material.YELLOW_WOOL));
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.BELL));

                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.BAMBOO_BUTTON)) {
                if (PrisonGame.active.getName().equals("Barreled")) {
                    if (PrisonGame.roles.get(event.getPlayer()).equals(Role.PRISONER)) {
                        Boolean justUnpowered = false;
                        if (PrisonGame.BBpower > 0) {
                            justUnpowered = true;
                        }
                        PrisonGame.BBpower -= new Random().nextInt(1, 3);
                        if (PrisonGame.BBpower <= 0) {
                            PrisonGame.BBpower = 0;
                            if (justUnpowered) {
                                Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"),-1023,-57,-994)).setType(Material.AIR);
                                Bukkit.broadcastMessage(ChatColor.RED + "The facility power has gone off! " + ChatColor.GREEN + "Prisoner now get speed, and a door has opened somewhere...");
                            }
                        }
                    } else {
                        Boolean justUnpowered = false;
                        if (PrisonGame.BBpower < 100) {
                            justUnpowered = true;
                        }
                        PrisonGame.BBpower += new Random().nextInt(2, 4);
                        if (PrisonGame.BBpower >= 100) {
                            PrisonGame.BBpower = 100;
                            if (justUnpowered) {
                                Location[] doors5 = {
                                        new Location(Bukkit.getWorld("world"), -979, -53, -984),
                                        new Location(Bukkit.getWorld("world"), -980, -53, -984)
                                };
                                for (Location l : doors5) {
                                    BlockState state = l.getBlock().getState();
                                    Door openable = (Door) state.getBlockData();
                                    openable.setOpen(false);
                                    state.setBlockData(openable);
                                    state.update();
                                    l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                                }
                                Bukkit.broadcastMessage(ChatColor.RED + "Power has returned.");
                                Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"),-1023,-57,-994)).setType(Material.REDSTONE_BLOCK);
                            }
                        }
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.COARSE_DIRT)) {
                if (event.getItem() != null) {
                    if (event.getItem().getType().equals(Material.WOODEN_SHOVEL)) {
                        if (!event.getPlayer().hasCooldown(Material.WOODEN_SHOVEL)) {
                            double paidedamount = 0;
                            double shovelingLvl = Keys.SHOVELING_UPGRADE.get(event.getPlayer(), 0);
                            double increasearg = 0;
                            double normalamount = 8.0;
                            double escapeamount = 10.0;
                            if(shovelingLvl==1) increasearg=1.25;
                            if(shovelingLvl==2) increasearg=1.5;
                            if(shovelingLvl == 0) shovelingLvl=0;
                            if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                                paidedamount=(escapeamount*increasearg)+escapeamount;
                                Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + paidedamount * Schedule.jobMultiplier);
                            }else {
                                paidedamount=(normalamount*increasearg)+normalamount;
                                Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + paidedamount * Schedule.jobMultiplier);
                            }
                            Random rand = new Random();
                            float chance = 0.3f;
                            float comparison = rand.nextFloat() * 100;
                            if(chance >= comparison){
                                event.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                            }
                            event.getClickedBlock().setType(Material.BEDROCK);
                            event.getPlayer().setCooldown(Material.WOODEN_SHOVEL, 10);
                            TryAxe(event.getPlayer());
                            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                                event.getClickedBlock().setType(Material.COARSE_DIRT);
                            }, 20 * 10);
                        }

                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.GRINDSTONE)) {
                event.setCancelled(true);
            }
            if (event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                if (PrisonGame.prisonerlevel.getOrDefault(event.getPlayer(), 0) != 1) {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.COD));
                    event.setCancelled(true);
                } else {
                    event.getPlayer().sendMessage(ChatColor.GOLD + "As an F-CLASS, You can only mine!");
                }
            }
            if (event.getClickedBlock().getType().equals(Material.BLAST_FURNACE)) {
                event.setCancelled(true);
                if (event.getItem() != null) {
                    if (event.getItem().getType().equals(Material.COD)) {
                        if (!event.getPlayer().hasCooldown(Material.COD)) {
                            event.getItem().setAmount(event.getItem().getAmount() - 1);
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
                            event.getPlayer().setCooldown(Material.COD, 2);
                            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                                    Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 2.5 * Schedule.jobMultiplier);
                                }else {
                                    Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 2.0 * Schedule.jobMultiplier);
                                }
                            }, 20 * 4);
                        }
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.SHULKER_BOX) || event.getClickedBlock().getType().equals(Material.ENDER_CHEST) || event.getClickedBlock().getType().equals(Material.SMOKER) || event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.BARREL) || event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.HOPPER) || event.getClickedBlock().getType().equals(Material.DROPPER) || event.getClickedBlock().getType().equals(Material.DISPENSER)) {
                if (PrisonGame.roles.get(event.getPlayer()) != Role.PRISONER || PrisonGame.hardmode.get(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You can't access this!");
                    event.setCancelled(true);
                } else {
                    PrisonGame.worryachieve.put(event.getPlayer(), -1);
                }
            }
            if (event.getClickedBlock().getType().equals(Material.COBBLESTONE) || event.getClickedBlock().getType().equals(Material.STONE) || event.getClickedBlock().getType().equals(Material.ANDESITE)) {
                event.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
            }
            if (event.getClickedBlock().getType().equals(Material.DISPENSER)) {
                event.setCancelled(true);
            }
            if(event.getClickedBlock().getType().equals(Material.WARPED_HANGING_SIGN)){
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if(sign.getLine(1).equals("Zulfiqar")){
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 450000.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 450000);
                        ItemStack trident = new ItemStack(Material.TRIDENT);
                        ItemMeta itemMeta = trident.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.BLUE + "Zulfiqar");
                        List<String> lore = new ArrayList<String>();
                        lore.add(ChatColor.DARK_PURPLE + "backrooms?");
                        lore.add("");
                        itemMeta.setLore(lore);
                        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
                        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier1);
                        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
                        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", 0.02, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier3);
                        itemMeta.setUnbreakable(true);
                        trident.setItemMeta(itemMeta);
                        trident.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
                        trident.addUnsafeEnchantment(Enchantment.LOYALTY, 2);
                        trident.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
                        trident.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                        event.getPlayer().getInventory().addItem(trident);
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if(sign.getLine(1).equalsIgnoreCase("not meth")){
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 2500.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 2500);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+ event.getPlayer().getName()+" potion{custom_potion_effects:[{id:strength,duration:400,amplifier:0},{id:speed,duration:200,amplifier:2},{id:nausea,duration:140,amplifier:1},{id:poison,duration:100,amplifier:3}],display:{Name:'[\"\",{\"text\":\"Not Meth\",\"italic\":false,\"color\":\"yellow\"}]'}}");
                    }
                }
                if (Schedule.bossBar.getTitle().equals("Breakfast") || Schedule.bossBar.getTitle().equals("Lunch")) {
                    if (sign.getLine(1).equals("Get Cafe")) {
                        if (PrisonGame.gotcafefood.getOrDefault(event.getPlayer(), false)) {
                            PrisonGame.gotcafefood.put(event.getPlayer(), true);
                            event.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                            event.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                            event.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                            event.getPlayer().sendMessage(ChatColor.RED + "You recieved your meal.");
                        }
                    }
                }
                if (sign.getLine(1).equals("Leave")) {
                    event.getPlayer().stopAllSounds();
                    playerJoinignoreAsc(event.getPlayer(), false);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Welcome back!");
                }
                if (sign.getLine(1).equals("ASCENSION")) {
                    Inventory inv = Bukkit.createInventory(null, 9, ChatColor.AQUA + "ASCENSION");
                    if (!Keys.SEMICLOAK.has(event.getPlayer()))
                        inv.addItem(PrisonGame.createGuiItem(Material.LEATHER_CHESTPLATE, ChatColor.DARK_AQUA + "Semi Cloak", ChatColor.GREEN + "You will not warn guards for breaking bars/opening doors", ChatColor.AQUA + "5 Ascension Coins"));
                    if (!Keys.REINFORCEMENT.has(event.getPlayer()))
                        inv.addItem(PrisonGame.createGuiItem(Material.DIAMOND_AXE, ChatColor.DARK_AQUA + "Reinforcements", ChatColor.GREEN + "Gives you a diamond axe and iron helmet on FIRST SPAWN as warden", ChatColor.AQUA + "30 Ascension Coins"));
                    if (!Keys.SPAWN_PROTECTION.has(event.getPlayer()))
                        inv.addItem(PrisonGame.createGuiItem(Material.DIAMOND_CHESTPLATE, ChatColor.DARK_AQUA + "ProtSpawn", ChatColor.GREEN + "Spawns you with PROT 1 as a warden, guard, nurse or swat.", ChatColor.AQUA + "10 Ascension Coins"));
                    if (!Keys.RANDOM_ITEMS.has(event.getPlayer()))
                        inv.addItem(PrisonGame.createGuiItem(Material.SPLASH_POTION, ChatColor.DARK_AQUA + "Random Items", ChatColor.GREEN + "Gives you 3 random items when spawning.", ChatColor.AQUA + "3 Ascension Coins"));
                    event.getPlayer().openInventory(inv);
                }
                if (sign.getLine(0).equals("ASCEND")) {
                    sign.setType(Material.AIR);
                    event.getPlayer().sendMessage(ChatColor.RED + "Congratulations! You just deleted an old sign. You must feel very accomplished");
                }
                if (sign.getLine(1).equals("+1 time tick")) {
                    Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 1);
                }
            }
            if (event.getClickedBlock().getType().equals(Material.MANGROVE_WALL_SIGN)) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equals("LOCKDOWN")) {
                    if (PrisonGame.lockdowncool < 1) {
                        Bukkit.getWorld("world").setTime(16000);
                        PrisonGame.lockdowncool = (20 * 60) * 10;
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "That's on cooldown! " + ChatColor.YELLOW + PrisonGame.lockdowncool / 20 + " seconds left");
                    }
                }
            }
            if (event.getItem() != null) {
                if (event.getItem().getType().equals(Material.WOLF_SPAWN_EGG)) {
                    event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.WOLF);
                    event.getPlayer().getInventory().removeItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                }
            }
            if (event.getClickedBlock().getType().equals(Material.BIRCH_WALL_SIGN)) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if(sign.getLine(1).equals("Mining Upgrade")){
                    if(Keys.PICKAXE_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.PICKAXE_UPGRADE.set(event.getPlayer(), 3);
                    OpenMiningUpgradeGui(event.getPlayer());
                }
                if(sign.getLine(1).equals("Bounty Upgrade")){
                    if(Keys.SWORD_UPGRADE.get(event.getPlayer(), 0) >= 2) Keys.SWORD_UPGRADE.set(event.getPlayer(), 2);
                    OpenBountyUpgradeGui(event.getPlayer());
                }
                if(sign.getLine(1).equals("Shovel Upgrade")){
                    if(Keys.SHOVELING_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.SHOVELING_UPGRADE.set(event.getPlayer(), 3);
                    OpenShovelingUpgradeGui(event.getPlayer());
                }
                if(sign.getLine(1).equals("Plumber Upgrade")){
                    if(Keys.PLUMBER_UPGRADE.get(event.getPlayer(), 0) >= 2) Keys.PLUMBER_UPGRADE.set(event.getPlayer(), 2);
                    OpenPlumberUpgradeGui(event.getPlayer());
                }
                if (sign.getLine(1).equals("TP To Island")) {
                    if (PrisonGame.roles.get(event.getPlayer()) != Role.PRISONER && PrisonGame.roles.get(event.getPlayer()) != Role.WARDEN && PrisonGame.warden != event.getPlayer()) {
                        event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 1924, -60, -2027));
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You're not a guard/You can't come here as warden");
                    }
                }
                if (sign.getLine(1).equals("Market Shop")) {
                    event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                    Inventory inv = Bukkit.createInventory(null, 9, "Shop");
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.BEETROOT_SOUP, ChatColor.YELLOW + "Soup", ChatColor.GREEN + "$2"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.STICK, ChatColor.YELLOW + "Supreme Stick", ChatColor.GREEN + "$50"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.COAL, ChatColor.YELLOW + "Coal", ChatColor.GRAY + "$30"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    event.getPlayer().openInventory(inv);
                }
                if(sign.getLine(1).equals("Sharpshooter")){
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())) {
                        if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 3000.0) {
                            Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 3000);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+event.getPlayer().getName()+" bow{display:{Name:'[{\"text\":\"Sharpshooter\",\"italic\":false,\"color\":\"red\"}]',Lore:['[{\"text\":\"\",\"italic\":false}]','[{\"text\":\"(CRIMINAL EXCLUSIVE)\",\"italic\":false}]']},Enchantments:[{id:vanishing_curse,lvl:1},{id:power,lvl:1},{id:punch,lvl:1}]} 1");
                        }
                    }else{
                        event.getPlayer().sendMessage(ChatColor.RED+"ONLY CRIMINAL CAN USE THIS SIGN!");
                    }
                }
                if (sign.getLine(1).equals("Switch Maps")) {
                    if (!event.getPlayer().hasCooldown(Material.IRON_DOOR)) {
                        if (event.getPlayer() == PrisonGame.warden && event.getPlayer().getPassengers().size() == 0) {
                            if (PrisonGame.swapcool <= 0) {
                                Inventory inv = Bukkit.createInventory(null, 9 * 3, "Map Switch");

                                for (Prison prison : getConfig().getPrisons().values().stream().sorted(Comparator.comparingInt((p) -> p.getPriority())).toList()) {
                                    if (!prison.getShowInSelector())
                                        continue;

                                    var displayName = prison.getDisplayName();
                                    inv.addItem(PrisonGame.createGuiItem(prison.getMaterial(), displayName));
                                }
//                            inv.addItem(PrisonGame.createGuiItem(Material.COBBLESTONE, ChatColor.GRAY + "Fortress Of Gaeae"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.QUARTZ_BLOCK, ChatColor.WHITE + "Hypertech"));
                                //inv.addItem(PrisonGame.createGuiItem(Material.END_CRYSTAL, ChatColor.DARK_PURPLE + "The End?"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.CRIMSON_PLANKS, ChatColor.YELLOW + "Train"));
                                //inv.addItem(PrisonGame.createGuiItem(Material.RED_STAINED_GLASS, ChatColor.RED + "MAP DISABLED"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.STONE_BRICK_SLAB, ChatColor.WHITE + "Gladiator"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.SAND, ChatColor.GOLD + "Island"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.SNOW_BLOCK, ChatColor.BOLD + "Santa's Workshop"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.LAVA_BUCKET, ChatColor.RED + "Volcano"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.GRAY_CONCRETE, ChatColor.GRAY + "Skeld"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.DEEPSLATE_TILES, ChatColor.DARK_GRAY + "Maximum Security"));
//                            inv.addItem(PrisonGame.createGuiItem(Material.DEEPSLATE_TILES, "§aRocksNGrass"));

                                //inv.addItem(PrisonGame.createGuiItem(Material.QUARTZ, ChatColor.BLUE + "Boat"));
                                //inv.addItem(PrisonGame.createGuiItem(Material.NETHERRACK, ChatColor.RED + "Nether"));
                                event.getPlayer().openInventory(inv);
                            } else {
                                event.getPlayer().sendMessage(ChatColor.RED + "That's on cooldown! " + ChatColor.YELLOW + PrisonGame.swapcool / 20 + " seconds left.");
                            }

                        }
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You're in combat!");
                    }
                }

                if (sign.getLine(2).equals("Scrap Metal")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 150.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 150);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.RAW_IRON));
                    }
                }
                if (sign.getLine(2).equals("Not Drugs")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 30.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 30.0);

                        ItemStack card = new ItemStack(Material.GOLDEN_APPLE, 1);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Gapple " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);

                        event.getPlayer().getInventory().addItem(card);
                    }
                }
                if (sign.getLine(2).equals("Chainmail Helmet")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 300.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 300.0);

                        ItemStack card = new ItemStack(Material.CHAINMAIL_HELMET);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Helmet " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);

                        event.getPlayer().getInventory().addItem(card);
                    }
                }
                if (sign.getLine(2).equals("Tax Skip")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 15.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 15.0);

                        ItemStack card = new ItemStack(Material.BOOK);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.LIGHT_PURPLE + "Tax Skip");
                        card.setItemMeta(cardm);

                        event.getPlayer().getInventory().addItem(card);
                    }
                }
                if (sign.getLine(2).equals("Dagger")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 1000.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 1000.0);

                        ItemStack card = new ItemStack(Material.IRON_SWORD);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                        cardm.setDisplayName(ChatColor.BLUE + "Dagger " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);

                        event.getPlayer().getInventory().addItem(card);
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.CRAFTING_TABLE)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    Inventory inv = Bukkit.createInventory(null, 9, "Crafting");
                    inv.addItem(PrisonGame.createGuiItem(Material.CRAFTING_TABLE, ChatColor.LIGHT_PURPLE + "Normal Crafting"));
                    inv.addItem(PrisonGame.createGuiItem(Material.COBBLESTONE, ChatColor.LIGHT_PURPLE + "Rock", "§aRecipe:", "§b9 Pebbles"));
                    inv.addItem(PrisonGame.createGuiItem(Material.PAPER, ChatColor.WHITE + "Paper", "§aRecipe:", "§b1 Coal", "§b1 Scrap Metal", "§a15$"));
                    inv.addItem(PrisonGame.createGuiItem(Material.TRIPWIRE_HOOK, ChatColor.LIGHT_PURPLE + "Fake Card", "§aRecipe:", "§b3 Paper", "§b2 Sticks"));
                    inv.addItem(PrisonGame.createGuiItem(Material.SHEARS, ChatColor.GRAY + "WireCutters", "§aRecipe:", "§b4 Scrap Metal", "§b2 Sticks", "§b1 Rock"));
                    inv.addItem(PrisonGame.createGuiItem(Material.LEATHER_CHESTPLATE, ChatColor.DARK_GRAY + "Cloak", "§aRecipe:", "§b1 Coal", "§ba15$"));
                    event.getPlayer().openInventory(inv);
                }, 1L);
            }
            if (event.getClickedBlock().getType().equals(Material.FURNACE)) {
                event.setCancelled(true);
            }

            if (event.getClickedBlock().getType().equals(Material.SPRUCE_WALL_SIGN)) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equals("TP To Prison")) {
                    if (!Schedule.bossBar.getTitle().equals("LIGHTS OUT")) {
                        event.getPlayer().setCooldown(Material.IRON_DOOR, 20);
                        event.getPlayer().teleport(PrisonGame.active.getPrisoner().getLocation());
                    }
                }
                if (sign.getLine(1).equals("Shop")) {
                    Inventory inv = Bukkit.createInventory(null, 9, "Shop");
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.BEETROOT_SOUP, ChatColor.YELLOW + "Soup", ChatColor.GREEN + "$2"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.STICK, ChatColor.YELLOW + "Supreme Stick", ChatColor.GREEN + "$50"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.COAL, ChatColor.YELLOW + "Coal", ChatColor.GRAY + "$30"));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    inv.addItem(PrisonGame.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, ""));
                    event.getPlayer().openInventory(inv);
                }
                if (sign.getLine(1).equals("Guard Dogs")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 500.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 500.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 4));
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.BONE, 32));
                    }
                }
                if (sign.getLine(1).equals("PROT 1")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 500.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 500.0);
                        if (event.getPlayer().getInventory().getHelmet() != null)
                            event.getPlayer().getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        if (event.getPlayer().getInventory().getChestplate() != null)
                            event.getPlayer().getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        if (event.getPlayer().getInventory().getLeggings() != null)
                            event.getPlayer().getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        if (event.getPlayer().getInventory().getBoots() != null)
                            event.getPlayer().getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    }
                }
                if (sign.getLine(1).equals("Restore Kit")) {
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.WARDEN) {
                        Player nw = event.getPlayer();
                        nw.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                        nw.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                        nw.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                        nw.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

                        ItemStack wardenSword = new ItemStack(Material.DIAMOND_SWORD);
                        wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        wardenSword.addEnchantment(Enchantment.DURABILITY, 2);


                        ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                        ItemMeta cardm2 = card2.getItemMeta();
                        cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                        cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                        card2.setItemMeta(cardm2);
                        nw.getInventory().addItem(card2);

                        if (!event.getPlayer().getInventory().contains(wardenSword))
                            nw.getInventory().addItem(wardenSword);
                        if (!event.getPlayer().getInventory().contains(Material.BOW))
                            nw.getInventory().addItem(new ItemStack(Material.BOW));

                        if (!event.getPlayer().getInventory().contains(Material.TRIPWIRE_HOOK)) {
                            ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                            ItemMeta cardm = card.getItemMeta();
                            cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                            card.setItemMeta(cardm);
                            nw.getInventory().addItem(card);
                        }
                    }
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.SWAT) {
                        Player g = event.getPlayer();
                        ItemStack orangechest = new ItemStack(Material.NETHERITE_CHESTPLATE);

                        ItemStack orangeleg = new ItemStack(Material.NETHERITE_LEGGINGS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

                        ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                        orangeboot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                        orangelegItemMeta.setColor(Color.GRAY);
                        orangeboot.setItemMeta(orangelegItemMeta);

                        g.getInventory().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                        g.getInventory().setChestplate(orangechest);
                        g.getInventory().setLeggings(orangeleg);
                        g.getInventory().setBoots(orangeboot);

                        ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                        ItemMeta cardm2 = card2.getItemMeta();
                        cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                        cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                        card2.setItemMeta(cardm2);
                        g.getInventory().addItem(card2);

                        ItemStack wardenSword = new ItemStack(Material.DIAMOND_SWORD);

                        g.getInventory().addItem(wardenSword);

                        g.getInventory().addItem(new ItemStack(Material.BOW));
                        g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                        g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                        ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);
                        g.getInventory().addItem(card);
                    }
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.NURSE) {
                        Player g = event.getPlayer();

                        ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                        ItemMeta cardm2 = card2.getItemMeta();
                        cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                        cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                        card2.setItemMeta(cardm2);
                        g.getInventory().addItem(card2);

                        ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                        chestmeta.setColor(Color.PURPLE);
                        orangechest.setItemMeta(chestmeta);

                        ItemStack orangeleg = new ItemStack(Material.LEATHER_LEGGINGS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeleg.getItemMeta();
                        orangelegItemMeta.setColor(Color.PURPLE);
                        orangeleg.setItemMeta(orangelegItemMeta);

                        ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta orangebootItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                        orangebootItemMeta.setColor(Color.PURPLE);
                        orangeboot.setItemMeta(orangebootItemMeta);

                        g.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                        g.getInventory().setChestplate(orangechest);
                        g.getInventory().setLeggings(orangeleg);
                        g.getInventory().setBoots(orangeboot);

                        ItemStack wardenSword = new ItemStack(Material.STONE_SWORD);
                        wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        wardenSword.addEnchantment(Enchantment.DURABILITY, 1);

                        g.getInventory().addItem(wardenSword);

                        g.getInventory().addItem(new ItemStack(Material.CROSSBOW));
                        g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                        g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                        ItemStack pot = new ItemStack(Material.SPLASH_POTION);
                        PotionMeta potionMeta = (PotionMeta) pot.getItemMeta();
                        potionMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(10, 0), true);
                        pot.setItemMeta(potionMeta);

                        g.getInventory().addItem(pot);

                        ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);
                        g.getInventory().addItem(card);

                    }
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.GUARD) {
                        Player g = event.getPlayer();

                        ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                        ItemMeta cardm2 = card2.getItemMeta();
                        cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                        cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                        card2.setItemMeta(cardm2);
                        g.getInventory().addItem(card2);

                        ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                        chestmeta.setColor(Color.fromRGB(126, 135, 245));
                        orangechest.setItemMeta(chestmeta);

                        ItemStack orangeleg = new ItemStack(Material.LEATHER_LEGGINGS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeleg.getItemMeta();
                        orangelegItemMeta.setColor(Color.fromRGB(126, 135, 245));
                        orangeleg.setItemMeta(orangelegItemMeta);

                        ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta orangebootItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                        orangebootItemMeta.setColor(Color.fromRGB(126, 135, 245));
                        orangeboot.setItemMeta(orangebootItemMeta);

                        g.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                        g.getInventory().setChestplate(orangechest);
                        g.getInventory().setLeggings(orangeleg);
                        g.getInventory().setBoots(orangeboot);

                        ItemStack wardenSword = new ItemStack(Material.IRON_SWORD);
                        wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        wardenSword.addEnchantment(Enchantment.DURABILITY, 1);

                        g.getInventory().addItem(wardenSword);

                        g.getInventory().addItem(new ItemStack(Material.CROSSBOW));
                        g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                        g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                        ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);
                        g.getInventory().addItem(card);


                    }
                }
                if (sign.getLine(1).equals("SWAT Guards")) {
                    if (!PrisonGame.swat) {
                        if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 2500.0) {
                            Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 2500.0);
                            PrisonGame.swat = true;
                            Bukkit.broadcastMessage(ChatColor.GREEN + event.getPlayer().getName() + " has enabled SWAT guards!");
                        }
                    }
                }
                if (sign.getLine(2).equals("Non-Illegal Drugs")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 70.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 70.0);

                        ItemStack card = new ItemStack(Material.GOLDEN_APPLE, 1);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Gapple ");
                        card.setItemMeta(cardm);

                        event.getPlayer().getInventory().addItem(card);
                    }
                }
                if (sign.getLine(2).equals("Soup")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 2.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 2.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                    }
                }
                if (sign.getLine(2).equals("Steak")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 5.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 5.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.COOKED_BEEF));
                    }
                }
                if (sign.getLine(2).equals("Milk")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 5.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 5.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.MILK_BUCKET));
                    }
                }
                if (sign.getLine(2).equals("Vomit Pot")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 30.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 30.0);

                        ItemStack pot = new ItemStack(new ItemStack(Material.SPLASH_POTION));
                        PotionMeta potMeta = (PotionMeta) pot.getItemMeta();
                        potMeta.addCustomEffect(PotionEffectType.CONFUSION.createEffect(20 * 10, 0), true);
                        potMeta.setColor(Color.LIME);
                        pot.setItemMeta(potMeta);

                        event.getPlayer().getInventory().addItem(pot);
                    }
                }
                if (sign.getLine(1).equals("Cafeteria")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 1000.0) {
                        /*if (PrisonGame.active.cafedoor2.getBlock().getType().equals(Material.MUD_BRICKS)) {
                            Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 1000.0);
                            Bukkit.broadcastMessage(ChatColor.GREEN + event.getPlayer().getName() + " Bought the cafeteria!");
                            if (!PrisonGame.active.equals(Config.prisons.get("hyper"))) {
                                for (Integer x = PrisonGame.active.cafedoor1.getBlockX(); x <= PrisonGame.active.cafedoor2.getBlockX(); x++) {
                                    for (Integer y = PrisonGame.active.cafedoor1.getBlockY(); y <= PrisonGame.active.cafedoor2.getBlockY(); y++) {
                                        for (Integer z = PrisonGame.active.cafedoor1.getBlockZ(); z <= PrisonGame.active.cafedoor2.getBlockZ(); z++) {
                                            event.getPlayer().getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                                        }
                                    }
                                }
                                for (Integer x = PrisonGame.active.cafedoor2.getBlockX(); x <= PrisonGame.active.cafedoor1.getBlockX(); x++) {
                                    for (Integer y = PrisonGame.active.cafedoor2.getBlockY(); y <= PrisonGame.active.cafedoor1.getBlockY(); y++) {
                                        for (Integer z = PrisonGame.active.cafedoor2.getBlockZ(); z <= PrisonGame.active.cafedoor1.getBlockZ(); z++) {
                                            event.getPlayer().getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                                        }
                                    }
                                }
                            }
                        }*/
                        if (new Location(Bukkit.getWorld("world"), 3, -58, -1008).getBlock().getType().equals(Material.MUD_BRICKS)) {
                            if (PrisonGame.active.equals(getConfig().getPrisons().get("hyper"))) {
                                Bukkit.getWorld("world").getBlockAt(1, -58, -1008).setType(Material.AIR);
                                Bukkit.getWorld("world").getBlockAt(2, -58, -1008).setType(Material.AIR);
                                Bukkit.getWorld("world").getBlockAt(3, -58, -1008).setType(Material.AIR);

                            }
                        }
                    }
                }
                if (sign.getLine(2).equals("Piss Pot")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 30.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 30.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.HONEY_BOTTLE));
                    }
                }
                if (sign.getLine(2).equals("Strong Chest")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 1000.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 1000.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                    }
                }
                if (sign.getLine(2).equals("Arrows")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 16.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 16.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 16));
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_WOOD_PLACE, 1, 1);
                    }
                }
                if (sign.getLine(2).equals("Supreme Stick")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 50.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 50.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.STICK));
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_WOOD_PLACE, 1, 1);
                    }
                }
                if (sign.getLine(2).equals("Coal")) {
                    if (Keys.MONEY.get(event.getPlayer(), 0.0) >= 30.0) {
                        Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) - 30.0);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.COAL));
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_WOOD_PLACE, 1, 1);
                    }
                }
                if (sign.getLine(2).equals("Mining")) {
                    if (!event.getPlayer().getInventory().contains(Material.IRON_PICKAXE)) {
                        if(Keys.PICKAXE_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.PICKAXE_UPGRADE.set(event.getPlayer(), 3);
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
                        int pickaxelvl = Keys.PICKAXE_UPGRADE.get(event.getPlayer(), 0);
                        if(pickaxelvl == 0) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + event.getPlayer().getName() + " iron_pickaxe{Damage:36,Unbreakable:1b,display:{Name:'[{\"text\":\"Prisoner\\'s Pickaxe\",\"italic\":false}]'},CanDestroy:[deepslate_copper_ore,deepslate_emerald_ore,deepslate_gold_ore,deepslate_lapis_ore,deepslate_redstone_ore]} 1");
                        }else{
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+event.getPlayer().getName()+" iron_pickaxe{display:{Name:'[{\"text\":\"Prisoner\\'s Pickaxe\",\"italic\":false}]'},Enchantments:[{id:vanishing_curse,lvl:1},{id:efficiency,lvl:"+pickaxelvl+"}],CanDestroy:[deepslate_gold_ore,deepslate_lapis_ore,deepslate_copper_ore,deepslate_emerald_ore,deepslate_redstone_ore]} 1");
                        }
                        event.getPlayer().sendMessage(ChatColor.GRAY + "Mine Ores with the pickaxe.");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You already have a pickaxe!");
                    }
                }
                if (PrisonGame.prisonerlevel.getOrDefault(event.getPlayer(), 0) != 1) {
                    if (sign.getLine(2).equals("Lumberjack")) {
                        if (!event.getPlayer().getInventory().contains(Material.WOODEN_AXE)) {
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            ItemStack card = new ItemStack(Material.WOODEN_AXE);
                            ItemMeta cardm = card.getItemMeta();
                            cardm.setDisplayName(ChatColor.GOLD + "Lumber's Axe");
                            card.setItemMeta(cardm);
                            event.getPlayer().getInventory().addItem(card);
                            event.getPlayer().sendMessage(ChatColor.GOLD + "Go right click the spruce logs in the Lumberjack Station.");
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "You already have an axe!");
                        }
                    }
                    if (sign.getLine(2).equals("Shovelling")) {
                        if (!event.getPlayer().getInventory().contains(Material.WOODEN_SHOVEL)) {
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 1);
                            ItemStack card = new ItemStack(Material.WOODEN_SHOVEL);
                            ItemMeta cardm = card.getItemMeta();
                            cardm.setDisplayName(ChatColor.LIGHT_PURPLE + "Shovel");
                            card.setItemMeta(cardm);
                            event.getPlayer().getInventory().addItem(card);
                            event.getPlayer().sendMessage(ChatColor.GRAY + "Right click on coarse dirt with the shovel.");
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "You already have a shovel!");
                        }
                    }
                    if (sign.getLine(2).equals("Plumber")) {
                        if (!event.getPlayer().getInventory().contains(Material.CARROT_ON_A_STICK)) {
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                            ItemStack card = new ItemStack(Material.CARROT_ON_A_STICK);
                            ItemMeta cardm = card.getItemMeta();
                            sign.setLine(1, "$0.5/click");
                            cardm.setDisplayName(ChatColor.LIGHT_PURPLE + "Plumber");
                            card.setItemMeta(cardm);
                            event.getPlayer().getInventory().addItem(card);
                            event.getPlayer().sendMessage(ChatColor.BLUE + "Click on iron trapdoors with the plumber.");
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "You already have a plumber!");
                        }
                    }
                    if (sign.getLine(2).equals("Bounty Hunter")) {
                        if (!event.getPlayer().getInventory().contains(Material.WOODEN_SWORD)) {
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                            ItemStack card = new ItemStack(Material.WOODEN_SWORD);
                            ItemMeta cardm = card.getItemMeta();
                            cardm.setDisplayName(ChatColor.RED + "Bounty Hunter's Knife");
                            if(Keys.SWORD_UPGRADE.get(event.getPlayer(), 0) >= 3) Keys.SWORD_UPGRADE.set(event.getPlayer(), 2);
                            int bountyhunterlvl = Keys.SWORD_UPGRADE.get(event.getPlayer(), 0);
                            if(bountyhunterlvl >= 1)cardm.addEnchant(Enchantment.LOOT_BONUS_MOBS, bountyhunterlvl, false);
                            card.setItemMeta(cardm);
                            event.getPlayer().getInventory().addItem(card);
                            event.getPlayer().sendMessage(ChatColor.RED + "Kill criminals (Glowing people).");
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "You already have a sword!");
                        }
                    }
                    if (sign.getLine(2).equals("Cafe Worker")) {
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                        sign.setLine(1, "$2/fish");
                        event.getPlayer().sendMessage(ChatColor.WHITE + "Click on the trapped chest to get food, cook it in the blast furnace.");
                    }
                } else {
                    event.getPlayer().sendMessage(ChatColor.GOLD + "As an F-CLASS, You can only mine!");
                }
            }
            if (event.getClickedBlock().getType().equals(Material.JUNGLE_WALL_SIGN)) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equals("Leave Market")) {
                    event.getPlayer().teleport(PrisonGame.active.getBlackMarketOut().getLocation());
                    event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);
                    event.getPlayer().addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 3, 0));
                    event.getPlayer().removePotionEffect(PotionEffectType.UNLUCK);
                }
                if (sign.getLine(1).equals("Get Gear")) {
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())) {
                        Player g = event.getPlayer();
                        ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                        chestmeta.setColor(Color.RED);
                        chestmeta.setDisplayName("Armor " + ChatColor.RED + "[CONTRABAND]");
                        orangechest.setItemMeta(chestmeta);

                        ItemStack orangeleg = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        ItemMeta orangelegItemMeta = orangeleg.getItemMeta();
                        orangelegItemMeta.setDisplayName("Armor " + ChatColor.RED + "[CONTRABAND]");
                        orangeleg.setItemMeta(orangelegItemMeta);


                        g.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                        g.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                        g.getInventory().setChestplate(orangechest);
                        g.getInventory().setLeggings(orangeleg);
                    }
                    if (PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && !PrisonGame.escaped.get(event.getPlayer())) {
                        Player g = event.getPlayer();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getPlayer().getName() + " only prison:escape");
                        g.playSound(g, Sound.ITEM_GOAT_HORN_SOUND_1, 1, 1);
                        PrisonGame.escaped.put(event.getPlayer(), true);
                        Bukkit.broadcastMessage(ChatColor.RED + g.getName() + " escaped...");
                        event.getPlayer().addPotionEffect(PotionEffectType.GLOWING.createEffect(999999999, 0));

                        g.setCustomName(ChatColor.GRAY + "[" + ChatColor.RED + "CRIMINAL" + ChatColor.GRAY + "] " + ChatColor.GRAY + g.getName());
                        g.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "CRIMINAL" + ChatColor.GRAY + "] " + ChatColor.GRAY + g.getName());
                        g.setDisplayName(ChatColor.GRAY + "[" + ChatColor.RED + "CRIMINAL" + ChatColor.GRAY + "] " + ChatColor.GRAY + g.getName());


                        ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                        chestmeta.setColor(Color.RED);
                        chestmeta.setDisplayName("Armor " + ChatColor.RED + "[CONTRABAND]");
                        orangechest.setItemMeta(chestmeta);

                        ItemStack orangeleg = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                        orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        ItemMeta orangelegItemMeta = orangeleg.getItemMeta();
                        orangelegItemMeta.setDisplayName("Armor " + ChatColor.RED + "[CONTRABAND]");
                        orangeleg.setItemMeta(orangelegItemMeta);


                        g.sendMessage(ChatColor.LIGHT_PURPLE + "Reclick the sign to get armor; it will override any current armor!");

                        ItemStack wardenSword = new ItemStack(Material.STONE_SWORD);
                        wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                        wardenSword.addEnchantment(Enchantment.DURABILITY, 1);

                        g.getInventory().addItem(wardenSword);

                        g.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 4));


                        if (PrisonGame.hardmode.get(g)) {
                            g.setCustomName(ChatColor.GRAY + "[" + ChatColor.DARK_RED + "CRIMINAL" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY  + "Criminal " + PrisonGame.prisonnumber.get(g));
                            g.setDisplayName(ChatColor.GRAY + "[" + ChatColor.DARK_RED + "CRIMINAL" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY  + "Criminal " + PrisonGame.prisonnumber.get(g));
                            g.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "HARD MODE" + ChatColor.DARK_GRAY + "] " + g.getName());
                        }

                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {
                if (event.getItem() != null && event.getItem().getType().equals(Material.CARROT_ON_A_STICK)) {
                    if (!event.getPlayer().hasCooldown(Material.CARROT_ON_A_STICK)) {
                        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.75f, 1.75f);
                        if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                            Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 0.625 * Schedule.jobMultiplier);
                        }else {
                            Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 0.5 * Schedule.jobMultiplier);
                        }
                        Random rand = new Random();
                        float chance = 0.2f;
                        float comparison = rand.nextFloat() * 100;
                        if(chance >= comparison){
                            event.getPlayer().getInventory().addItem(new ItemStack(Material.PAPER));
                        }
                        double plumberlvl = Keys.PLUMBER_UPGRADE.get(event.getPlayer(), 0);
                        int decreaseperctange = 5;
                        if(plumberlvl==1) decreaseperctange=4; // 4.5
                        if(plumberlvl==2) decreaseperctange=3; // 4
                        if(plumberlvl==3) decreaseperctange=2; // 3.25
                        event.getPlayer().setCooldown(Material.CARROT_ON_A_STICK, decreaseperctange);
                        TryAxe(event.getPlayer());
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.SPRUCE_LOG)) {
                if (event.getItem() != null) {
                    if (event.getItem().getType().equals(Material.WOODEN_AXE)) {
                        if (!event.getPlayer().hasCooldown(Material.WOODEN_AXE)) {
                            event.getPlayer().setCooldown(Material.WOODEN_AXE, 10);
                            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_WOOD_BREAK, 1, 1);
                            if(PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && PrisonGame.escaped.get(event.getPlayer())){
                                Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 2.5 * Schedule.jobMultiplier);
                            }else {
                                Keys.MONEY.set(event.getPlayer(), Keys.MONEY.get(event.getPlayer(), 0.0) + 2.0 * Schedule.jobMultiplier);
                            }
                            Random rand = new Random();
                            float chance = 0.75f;
                            float comparison = rand.nextFloat() * 100;
                            if(chance >= comparison){
                                event.getPlayer().getInventory().addItem(new ItemStack(Material.STICK));
                            }
                            TryAxe(event.getPlayer());
                        }
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.CAULDRON)) {
                event.setCancelled(true);
                if (!event.getPlayer().hasCooldown(Material.IRON_DOOR)) {
                    if (PrisonGame.roles.get(event.getPlayer()) != Role.GUARD && PrisonGame.roles.get(event.getPlayer()) != Role.NURSE && PrisonGame.roles.get(event.getPlayer()) != Role.SWAT) {
                        event.getPlayer().teleport(PrisonGame.active.getBlackMarketIn().getLocation());
                        event.getPlayer().sendTitle("", ChatColor.GRAY + "-= Black Market =-");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getPlayer().getName() + " only prison:market");
                        event.getPlayer().playSound(event.getPlayer(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 0.75f);
                        event.getPlayer().addPotionEffect(PotionEffectType.UNLUCK.createEffect(999999, 2));
                    } else {
                        event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        event.getPlayer().sendMessage(ChatColor.RED + "You wouldn't want to get yourself dirty in there!");
                    }
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "You're in combat!");
                }
            }
            if (event.getClickedBlock().getType().equals(Material.JUNGLE_DOOR)) {
                if (PrisonGame.roles.get(event.getPlayer()) != Role.WARDEN) {
                    event.setCancelled(true);
                    event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    event.getPlayer().sendMessage(ChatColor.RED + "This door can only be opened by the warden!");
                }
            }
            if (event.getClickedBlock().getType().equals(Material.ACACIA_DOOR)) {
                BlockState state = event.getClickedBlock().getState();
                Door openable = (Door) state.getBlockData();

                if (Bukkit.getWorld("world").getTime() > 15000 && Bukkit.getWorld("world").getTime() < 24000) {
                    if (event.getItem() != null) {
                        if (event.getItem().getItemMeta() != null) {
                            if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]")) {
                                event.setCancelled(true);
                                if (!openable.isOpen()) {
                                    openable.setOpen(true);
                                    state.setBlockData(openable);
                                    state.update();
                                } else {
                                    openable.setOpen(false);
                                    state.setBlockData(openable);
                                    state.update();
                                }
                            } else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(ChatColor.RED + "You can't open this during lockdown/cell time!");
                                event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(ChatColor.RED + "You can't open this during lockdown/cell time!");
                            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        }
                    } else {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "You can't open this during lockdown/cell time!");
                        event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                }
            }
            if (event.getClickedBlock().getType().equals(Material.IRON_DOOR)) {
                BlockState state = event.getClickedBlock().getState();
                Door openable = (Door) state.getBlockData();

                if (event.getItem() != null) {
                    if (event.getItem().getItemMeta() != null) {
                        if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]")) {
                            grantRealGuardIfEligible(event.getPlayer());
                            event.setCancelled(true);
                            if (!openable.isOpen()) {
                                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_IRON_DOOR_OPEN, 1, 1);
                                Boolean yesdothat = true;
                                if (event.getPlayer().getInventory().getChestplate() != null) {
                                    if (event.getPlayer().getInventory().getChestplate().getItemMeta() != null) {
                                        if (event.getPlayer().getInventory().getChestplate().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Cloak Chestplate")) {
                                            yesdothat = false;
                                        }
                                    }
                                }
                                if (yesdothat && !event.getPlayer().hasPotionEffect(PotionEffectType.GLOWING) && !Keys.SEMICLOAK.has(event.getPlayer())) {
                                    if (PrisonGame.roles.get(event.getPlayer()) == Role.PRISONER && !PrisonGame.escaped.get(event.getPlayer())) {
                                        event.getPlayer().sendMessage(ChatColor.RED + "You were caught opening a door! Get a cloak next time!");
                                        event.getPlayer().addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                                        for (Player g : Bukkit.getOnlinePlayers()) {
                                            if (PrisonGame.roles.get(g) != Role.PRISONER) {
                                                g.playSound(g, Sound.ENTITY_SILVERFISH_DEATH, 1, 0.5f);
                                                g.sendMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.DARK_RED + " was caught opening a door!");
                                            }
                                        }
                                    }
                                }
                                openable.setOpen(true);
                                state.setBlockData(openable);
                                state.update();
                            } else {
                                openable.setOpen(false);
                                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
                                state.setBlockData(openable);
                                state.update();

                            }
                        }
                    }
                }
            }
        }
    }

    private void grantRealGuardIfEligible(Player player) {
        if (PrisonGame.roles.get(player) != Role.PRISONER)
            return;

        var inventory = player.getInventory();
        var color = Color.fromRGB(126, 135, 245);
        var advancement = Bukkit.getAdvancement(new NamespacedKey("prison", "real_guard"));

        if (getColor(inventory.getBoots()).equals(color) &&
            getColor(inventory.getLeggings()).equals(color) &&
            getColor(inventory.getChestplate()).equals(color) &&
            inventory.getHelmet().getType() == Material.IRON_HELMET
        ) {
            player.getAdvancementProgress(advancement).awardCriteria("no");
        }
    }

    private Color getColor(ItemStack item) {
        if (item == null)
            return Color.BLACK;

        var meta = item.getItemMeta();

        if (!(meta instanceof LeatherArmorMeta leatherMeta))
            return Color.BLACK;

        return leatherMeta.getColor();
    }
    public void TryAxe(Player p){
        Boolean IsEscaped = PrisonGame.roles.get(p) == Role.PRISONER && PrisonGame.escaped.get(p);
        Random rand = new Random();
        float crimchance = 0.0133315f;
        float chance = 	0.00010f;
        if(IsEscaped)chance = crimchance;
        float comparison = rand.nextFloat() * 100;
        if(chance >= comparison){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" netherite_axe{Unbreakable:1,display:{Name:'[{\"text\":\"Brachydios\",\"bold\":true,\"color\":\"gold\"}]',Lore:['[{\"text\":\"\",\"italic\":false}]','[{\"text\":\"\",\"italic\":false}]','[{\"text\":\"(1 in 10,000)\",\"italic\":false}]']},Enchantments:[{id:vanishing_curse,lvl:1},{id:knockback,lvl:1},{id:sharpness,lvl:2}]} 1");
            Bukkit.broadcastMessage(ChatColor.GOLD+p.getName() + " got Branchy axe (RUN FOR YOUR LIFE)");
        }
    }
    public void OpenMiningUpgradeGui(Player p){
        Inventory miningUpgradeGui = Bukkit.createInventory(null, 9*3, "Mining Upgrade");
        ItemStack guiItem1 = new ItemStack(Material.IRON_PICKAXE); // LVL 1 Mining Upgrade
        ItemStack guiItem2 = new ItemStack(Material.IRON_PICKAXE); // LVL 2 Mining Upgrade
        ItemStack guiItem3 = new ItemStack(Material.IRON_PICKAXE); // LVL 3 Mining Upgrade
        ItemMeta guiItem1m = guiItem1.getItemMeta();
        ItemMeta guiItem2m = guiItem2.getItemMeta();
        ItemMeta guiItem3m = guiItem3.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD+"Cost: 7k "+ChatColor.GRAY+"(7000)");
        guiItem1m.setLore(lore);
        guiItem1m.setDisplayName(ChatColor.GOLD+"Lvl 1 Mining Upgrade");
        lore.add("");
        lore.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 1");
        guiItem1.setItemMeta(guiItem1m);
        List<String> lore2 = new ArrayList<String>();
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Cost: 20k "+ChatColor.GRAY+"(20000)");
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 2");
        guiItem2m.setLore(lore2);
        guiItem2m.setDisplayName(ChatColor.GOLD+"Lvl 2 Mining Upgrade");
        guiItem2.setItemMeta(guiItem2m);
        List<String> lore3 = new ArrayList<String>();
        lore3.add("");
        lore3.add(ChatColor.GOLD+"Cost: 55k "+ChatColor.GRAY+"(55000)");
        lore3.add("");
        lore3.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 3");
        guiItem3m.setLore(lore3);
        guiItem3m.setDisplayName(ChatColor.GOLD+"Lvl 3 Mining Upgrade [MAX]");
        guiItem3.setItemMeta(guiItem3m);
        for (int i = 0; i < 9*3; i++) {
            miningUpgradeGui.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        miningUpgradeGui.setItem(10,guiItem1);
        miningUpgradeGui.setItem(13,guiItem2);
        miningUpgradeGui.setItem(16,guiItem3);
        p.openInventory(miningUpgradeGui);
    }
    public void OpenBountyUpgradeGui(Player p){
        Inventory miningUpgradeGui = Bukkit.createInventory(null, 9*3, "Bounty Upgrade");
        ItemStack guiItem1 = new ItemStack(Material.WOODEN_SWORD); // LVL 1 Bounty Upgrade
        ItemStack guiItem2 = new ItemStack(Material.WOODEN_SWORD); // LVL 2 Bounty Upgrade
        ItemMeta guiItem1m = guiItem1.getItemMeta();
        ItemMeta guiItem2m = guiItem2.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD+"Cost: 7k "+ChatColor.GRAY+"(7000)");
        lore.add("");
        lore.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Fortune 1 \nIncreased Money Guard Roles Drop!");
        guiItem1m.setLore(lore);
        guiItem1m.setDisplayName(ChatColor.GOLD+"Lvl 1 Bounty Upgrade");
        guiItem1.setItemMeta(guiItem1m);
        List<String> lore2 = new ArrayList<String>();
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Cost: 20k "+ChatColor.GRAY+"(20000)");
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Fortune 2 \nIncreased Money Guard Roles Drop!");
        guiItem2m.setLore(lore2);
        guiItem2m.setDisplayName(ChatColor.GOLD+"Lvl 2 Bounty Upgrade [MAX]");
        guiItem2.setItemMeta(guiItem2m);
        for (int i = 0; i < 9*3; i++) {
            miningUpgradeGui.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        miningUpgradeGui.setItem(10,guiItem1);
        miningUpgradeGui.setItem(16,guiItem2);
        p.openInventory(miningUpgradeGui);
    }
    public void OpenShovelingUpgradeGui(Player p){
        Inventory miningUpgradeGui = Bukkit.createInventory(null, 9*3, "Shoveling Upgrade");
        ItemStack guiItem1 = new ItemStack(Material.WOODEN_SHOVEL); // LVL 1 Shoveling Upgrade
        ItemStack guiItem2 = new ItemStack(Material.WOODEN_SHOVEL); // LVL 2 Shoveling Upgrade
        ItemMeta guiItem1m = guiItem1.getItemMeta();
        ItemMeta guiItem2m = guiItem2.getItemMeta();
        //
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD+"Cost: 15k "+ChatColor.GRAY+"(15000)");
        lore.add("");
        lore.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Fortune 1");
        guiItem1m.setLore(lore);
        guiItem1m.setDisplayName(ChatColor.GOLD+"Lvl 1 Shoveling Upgrade");
        guiItem1.setItemMeta(guiItem1m);
        //
        List<String> lore2 = new ArrayList<String>();
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Cost: 30k "+ChatColor.GRAY+"(30000)");
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Fortune 2");
        guiItem2m.setLore(lore2);
        guiItem2m.setDisplayName(ChatColor.GOLD+"Lvl 2 Shoveling Upgrade");
        guiItem2.setItemMeta(guiItem2m);
        //
        for (int i = 0; i < 9*3; i++) {
            miningUpgradeGui.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        miningUpgradeGui.setItem(10,guiItem1);
        miningUpgradeGui.setItem(16,guiItem2);
        p.openInventory(miningUpgradeGui);
    }
    public void OpenPlumberUpgradeGui(Player p){
        Inventory miningUpgradeGui = Bukkit.createInventory(null, 9*3, "Plumber Upgrade");
        ItemStack guiItem1 = new ItemStack(Material.CARROT_ON_A_STICK); // LVL 1 Plumber Upgrade
        ItemStack guiItem2 = new ItemStack(Material.CARROT_ON_A_STICK); // LVL 2 Plumber Upgrade
        ItemStack guiItem3 = new ItemStack(Material.CARROT_ON_A_STICK); // LVL 3 Plumber Upgrade
        ItemMeta guiItem1m = guiItem1.getItemMeta();
        ItemMeta guiItem2m = guiItem2.getItemMeta();
        ItemMeta guiItem3m = guiItem3.getItemMeta();
        //
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD+"Cost: 7.5k "+ChatColor.GRAY+"(7500)");
        lore.add("");
        lore.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 1");
        guiItem1m.setLore(lore);
        guiItem1m.setDisplayName(ChatColor.GOLD+"Lvl 1 Plumber Upgrade");
        guiItem1.setItemMeta(guiItem1m);
        //
        List<String> lore2 = new ArrayList<String>();
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Cost: 17.5k "+ChatColor.GRAY+"(17500)");
        lore2.add("");
        lore2.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 2");
        guiItem2m.setLore(lore2);
        guiItem2m.setDisplayName(ChatColor.GOLD+"Lvl 2 Plumber Upgrade");
        guiItem2.setItemMeta(guiItem2m);
        //
        List<String> lore3 = new ArrayList<String>();
        lore3.add("");
        lore3.add(ChatColor.GOLD+"Cost: 50k "+ChatColor.GRAY+"(50000)");
        lore3.add("");
        lore3.add(ChatColor.GOLD+"Perks:\n "+ChatColor.GRAY+"Efficacy 3");
        guiItem3m.setLore(lore3);
        guiItem3m.setDisplayName(ChatColor.GOLD+"Lvl 3 Plumber Upgrade");
        guiItem3.setItemMeta(guiItem3m);
        //
        for (int i = 0; i < 9*3; i++) {
            miningUpgradeGui.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        miningUpgradeGui.setItem(10,guiItem1);
        miningUpgradeGui.setItem(13,guiItem2);
        miningUpgradeGui.setItem(16,guiItem3);
        p.openInventory(miningUpgradeGui);
    }
}
