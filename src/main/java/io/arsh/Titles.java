package io.arsh;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Titles {

    private static FileConfiguration titleFile;

    public Titles(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "titles.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("titles.yml", false);
        }
        titleFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void sendTitle(Player player, Title titleType, String... placeholders) {
        String path = "Titles." + titleType.getPath();
        if (!titleFile.getBoolean(path + ".Use", false)) return;

        String rawTitle = titleFile.getString(path + ".Title", "");
        String rawSubTitle = titleFile.getString(path + ".SubTitle", "");
        int fadeIn = titleFile.getInt(path + ".FadIn", 0);
        int stay = titleFile.getInt(path + ".Stay", 30);
        int fadeOut = titleFile.getInt(path + ".FadeOut", 0);

        String title = ChatColor.translateAlternateColorCodes('&', applyPlaceholders(rawTitle, placeholders));
        String subTitle = ChatColor.translateAlternateColorCodes('&', applyPlaceholders(rawSubTitle, placeholders));

        player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
    }

    private static String applyPlaceholders(String text, String... placeholders) {
        if (placeholders == null || placeholders.length % 2 != 0) return text;
        for (int i = 0; i < placeholders.length; i += 2) {
            text = text.replace(placeholders[i], placeholders[i + 1]);
        }
        return text;
    }

    public enum Title {
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

        Title(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
