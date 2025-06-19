package io.arsh.commands;

import io.arsh.Messages;
import io.arsh.Sounds;
import io.arsh.Titles;
import io.arsh.WarpManager;
import io.arsh.commands.subCommands.AddWarp;
import io.arsh.commands.subCommands.DeleteWarp;
import io.arsh.commands.subCommands.ListWarps;
import io.arsh.commands.subCommands.SetWarp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JustWarpCmd implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    private final WarpManager warpManager;

    private final ArrayList<SubCommand> subCmds = new ArrayList<>();
    public ArrayList<SubCommand> getSubCmds() {
        return subCmds;
    }

    public JustWarpCmd(JavaPlugin plugin, WarpManager warpManager) {
        this.plugin = plugin;
        this.warpManager = warpManager;
        subCmds.add(new AddWarp(warpManager));
        subCmds.add(new SetWarp(warpManager));
        subCmds.add(new DeleteWarp(warpManager));
        subCmds.add(new ListWarps(warpManager));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("justwarp.admin")) {
                Messages.send(player, Messages.Message.NO_PERMISSION);
                Titles.sendTitle(player, Titles.Title.NO_PERMISSION);
                Sounds.playSound(player, Sounds.Sound.NO_PERMISSION);
                return true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                player.sendMessage("");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r               &b&lWARP COMMANDS"));
                player.sendMessage("");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f1.&b /justwarp help &7- &fHelp command&f."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f2.&b /justwarp reload &7- &fUse to reload the plugin&f."));
                for (int i = 0; i < getSubCmds().size(); i++) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f" + (i + 3) + ".&b " + getSubCmds().get(i).getSyntax() + " &7- &f" + getSubCmds().get(i).getDescription()));
                }
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 1);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                List<String> fileNames = List.of("config", "data", "messages", "sounds", "titles");
                for (String name : fileNames) {
                    File file = new File(plugin.getDataFolder(), name + ".yml");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        plugin.saveResource(name + ".yml", false);
                    }
                    FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    try {
                        configuration.save(file);
                    } catch (IOException e) {
                        plugin.getLogger().severe("An error occurred while saving file " + plugin.getDataFolder() + "/" + name + ".yml.");
                    }
                }
                String message = ChatColor.translateAlternateColorCodes('&', "&b&lWARP &fSuccessfully reload &bJust Warp&f.");
                player.sendMessage(message);
                return true;
            }
            for (int i = 0; i < getSubCmds().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCmds().get(i).getName())) {
                    getSubCmds().get(i).perform(player, args);
                    return true;
                }
            }
            Messages.send(player, Messages.Message.UNKNOWN_COMMAND);
            Titles.sendTitle(player, Titles.Title.UNKNOWN_COMMAND);
            Sounds.playSound(player, Sounds.Sound.UNKNOWN_COMMAND);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            List<String> fileNames = List.of("config", "data", "messages", "sounds", "titles");
            for (String name : fileNames) {
                File file = new File(plugin.getDataFolder(), name + ".yml");
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    plugin.saveResource(name + ".yml", false);
                }
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    plugin.getLogger().severe("An error occurred while saving file " + plugin.getDataFolder() + "/" + name + ".yml.");
                }
            }
            String message = "Successfully reload Just Warp.";
            plugin.getLogger().warning(message);
            return true;
        }
        plugin.getLogger().severe("You can't use this command.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("?");
            completions.add("reload");
            for (SubCommand cmd : getSubCmds()) {
                completions.add(cmd.getName());
            }
            return completions;
        }
        if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                completions.add("<name>");
                return completions;
            }
            if (args.length == 3) {
                completions.add("<delay>");
                return completions;
            }
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                completions.add("<name>");
                completions.addAll(warpManager.getWarpList());
                return completions;
            }
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length == 2) {
                completions.add("<name>");
                completions.addAll(warpManager.getWarpList());
                return completions;
            }
            if (args.length == 3) {
                completions.add("<delay>");
                return completions;
            }
        } return completions;
    }

}