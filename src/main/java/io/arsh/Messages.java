package io.arsh;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Messages {

    private static FileConfiguration messageFile;
    private static String prefix = "";

    public Messages(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }
        messageFile = YamlConfiguration.loadConfiguration(file);
        prefix = ChatColor.translateAlternateColorCodes('&', messageFile.getString("Prefix", ""));
    }

    public static void send(CommandSender sender, Message type, String... placeholders) {
        String raw = messageFile.getString("Messages." + type.getPath(), "");
        String msg = ChatColor.translateAlternateColorCodes('&', applyPlaceholders(raw, placeholders));
        sender.sendMessage(prefix + " " + msg);
    }

    public static String get(Message type, String... placeholders) {
        String raw = messageFile.getString("Messages." + type.getPath(), "");
        return ChatColor.translateAlternateColorCodes('&', applyPlaceholders(prefix + " " + raw, placeholders));
    }

    private static String applyPlaceholders(String text, String... placeholders) {
        if (placeholders == null || placeholders.length % 2 != 0) return text;
        for (int i = 0; i < placeholders.length; i += 2) {
            text = text.replace(placeholders[i], placeholders[i + 1]);
        }
        return text;
    }

    public enum Message {
        TELEPORTED("Teleported"),
        TELEPORTING("Teleporting"),
        TELEPORTATION_CANCELED("Teleportation_Canceled"),
        WARP_ADD("Warp-Add"),
        WARP_SET("Warp-Set"),
        WARP_LIST("Warp-List"),
        WARP_LIST_EMPTY("Warp-List-Empty"),
        WARP_DELETE("Warp-Delete"),
        WARP_ALREADY_EXISTS("Warp-Already-Exists"),
        WARP_NOT_EXISTS("Warp-Not-Exists"),
        INVALID_USE_OF_COMMAND("Invalid-Use-Of-Command"),
        INVALID_INPUT("Invalid-Input"),
        UNKNOWN_COMMAND("Unknown-Command"),
        NO_PERMISSION("No-Permission"),
        NO_WARP_PERMISSION("No-Warp-Permission");

        private final String path;

        Message(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

}
