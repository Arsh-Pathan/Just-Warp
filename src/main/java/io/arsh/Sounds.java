package io.arsh;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Sounds {

    private static JavaPlugin plugin;
    private static FileConfiguration soundFile;

    public Sounds(JavaPlugin plugin) {
        Sounds.plugin = plugin;
        File file = new File(plugin.getDataFolder(), "sounds.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("sounds.yml", false);
        }
        soundFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void playSound(Player player, Sound sound) {
        String path = "Sounds." + sound.getPath();
        if (!soundFile.getBoolean(path + ".Use", false)) return;

        String soundName = soundFile.getString(path + ".Sound");
        float volume = (float) soundFile.getDouble(path + ".Volume", 100);
        float pitch = (float) soundFile.getDouble(path + ".Pitch", 1);

        try {
            org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(soundName);
            player.playSound(player.getLocation(), bukkitSound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid sound name in sounds.yml: " + soundName);
            plugin.getLogger().severe("Make sure you are using supported sounds from https://helpch.at/docs/1.21/org/bukkit/Sound.html.");
        }
    }

    public enum Sound {
        TELEPORTED("Teleported"),
        TELEPORTING("Teleporting"),
        TELEPORTATION_CANCELED("Teleportation_Canceled"),
        WARP_ADD("Warp-Add"),
        WARP_SET("Warp-Set"),
        WARP_DELETE("Warp-Delete"),
        WARP_ALREADY_EXISTS("Warp-Already-Exists"),
        WARP_NOT_EXISTS("Warp-Not-Exists"),
        INVALID_USE_OF_COMMAND("Invalid-Use-Of-Command"),
        INVALID_INPUT("Invalid-Input"),
        UNKNOWN_COMMAND("Unknown-Command"),
        NO_PERMISSION("No-Permission");

        private final String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

}
