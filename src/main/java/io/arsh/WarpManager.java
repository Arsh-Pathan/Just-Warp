package io.arsh;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpManager {

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration data;

    private ConfigurationSection warpSection;

    public WarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create data.yml!");
                e.printStackTrace();
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
        this.warpSection = data.getConfigurationSection("Warps");

        if (this.warpSection == null) {
            this.warpSection = data.createSection("Warps");
            saveData();
        }
    }

    private void saveData() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml!");
            e.printStackTrace();
        }
    }

    public void addWarp(String name, Location location, int delay) {
        if (exists(name)) return;
        setWarp(name, location, delay);
    }

    public boolean exists(String name) {
        return warpSection.contains(name);
    }

    public void setWarp(String name, Location location, int delay) {
        ConfigurationSection section = warpSection.createSection(name);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        section.set("delay", delay);
        saveData();
    }

    public void deleteWarp(String name) {
        if (!exists(name)) return;
        warpSection.set(name, null);
        saveData();
    }

    public void warp(Player player, String name) {
        Location location = getWarpLocation(name);
        if (location != null) {
            player.teleport(location);
        }
    }

    public Location getWarpLocation(String name) {
        if (!exists(name)) return null;

        ConfigurationSection section = warpSection.getConfigurationSection(name);
        if (section == null) return null;

        String worldName = section.getString("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public int getWarpDelay(String name) {
        if (!exists(name)) return 0;
        return warpSection.getInt(name + ".delay", 0);
    }

    public List<String> getWarpList() {
        return new ArrayList<>(warpSection.getKeys(false));
    }
}
