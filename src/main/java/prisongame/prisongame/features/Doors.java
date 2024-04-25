package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import prisongame.prisongame.PrisonGame;

import java.util.ArrayList;

public class Doors implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var world = Bukkit.getWorld("world");
        
        if (world.getTime() != 16000)
            return;
        
        var doors = new ArrayList<Location>();
        
        switch (PrisonGame.active.getName()) {
            case "Gaeae Fort":
                doors.add(new Location(Bukkit.getWorld("world"), 48, -59, -135));
                doors.add(new Location(Bukkit.getWorld("world"), 47, -59, -134));
                doors.add(new Location(Bukkit.getWorld("world"), 29, -59, -107));
                doors.add(new Location(Bukkit.getWorld("world"),  21, -60, -102));
                doors.add(new Location(Bukkit.getWorld("world"), 14, -60, -108));
                doors.add(new Location(Bukkit.getWorld("world"),  12, -60, -117));
                doors.add(new Location(Bukkit.getWorld("world"),23, -59, -120));
                break;
            case "Hypertech":
                doors.add(new Location(Bukkit.getWorld("world"), -18, -59, -996));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -59, -996));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -59, -991));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -59, -1006));
                doors.add(new Location(Bukkit.getWorld("world"), -18, -59, -1006));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -59, -1011));
                doors.add(new Location(Bukkit.getWorld("world"), -18, -59, -1011));
                doors.add(new Location(Bukkit.getWorld("world"), -18, -52, -996));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -52, -996));
                doors.add(new Location(Bukkit.getWorld("world"), -14, -52, -991));
                break;
            case "Train":
                doors.add(new Location(Bukkit.getWorld("world"), 92, -58, 961));
                doors.add(new Location(Bukkit.getWorld("world"), 91, -58, 961));
                doors.add(new Location(Bukkit.getWorld("world"), 92, -58, 964));
                doors.add(new Location(Bukkit.getWorld("world"), 91, -58, 964));
                doors.add(new Location(Bukkit.getWorld("world"), 92, -58, 969));
                doors.add(new Location(Bukkit.getWorld("world"), 91, -58, 969));
                doors.add(new Location(Bukkit.getWorld("world"), 92, -58, 972));
                doors.add(new Location(Bukkit.getWorld("world"), 91, -58, 972));
                doors.add(new Location(Bukkit.getWorld("world"), 92, -58, 980));
                doors.add(new Location(Bukkit.getWorld("world"), 91, -58, 980));
                break;
            case "Gladiator":
                doors.add(new Location(Bukkit.getWorld("world"), -2049, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2043, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2037, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2021, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2015, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2009, -60, 1950));
                doors.add(new Location(Bukkit.getWorld("world"), -2049, -60, 2002));
                doors.add(new Location(Bukkit.getWorld("world"), -2043, -60, 2002));
                doors.add(new Location(Bukkit.getWorld("world"), -2037, -60, 2002));
                doors.add(new Location(Bukkit.getWorld("world"), -2021, -60, 2002));
                doors.add(new Location(Bukkit.getWorld("world"), -2015, -60, 2002));
                doors.add(new Location(Bukkit.getWorld("world"), -2009, -60, 2002));
                break;
            case "Island":
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -56, -1987));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -56, -1987));
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -56, -1983));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -56, -1983));
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -56, -1979));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -56, -1979));
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -60, -1987));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -60, -1987));
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -60, -1983));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -60, -1983));
                doors.add(new Location(Bukkit.getWorld("world"), 1955, -60, -1979));
                doors.add(new Location(Bukkit.getWorld("world"), 1962, -60, -1979));
                break;
            case "Santa's Workshop":
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1931));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1931));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1926));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1926));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1921));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1921));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1916));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1916));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1911));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1911));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1906));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1906));
                doors.add(new Location(Bukkit.getWorld("world"), 1964, -60, 1901));
                doors.add(new Location(Bukkit.getWorld("world"), 1958, -60, 1901));
                break;
            case "Volcano":
                doors.add(new Location(Bukkit.getWorld("world"), -2019, -60, -1976));
                doors.add(new Location(Bukkit.getWorld("world"), -2020, -60, -1976));
                break;
        }

        for (Location location : doors) {
            BlockState state = location.getBlock().getState();
            Door openable = (Door) state.getBlockData();
            openable.setOpen(false);
            state.setBlockData(openable);
            state.update();
            world.playSound(location, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
        }
    }
}
