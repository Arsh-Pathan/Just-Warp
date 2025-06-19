package io.arsh.commands;

import io.arsh.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCmd implements CommandExecutor, TabCompleter {

    private final WarpManager warpManager;

    public WarpCmd(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 1) {
                Messages.send(player, Messages.Message.INVALID_USE_OF_COMMAND);
                Titles.sendTitle(player, Titles.Title.INVALID_USE_OF_COMMAND);
                Sounds.playSound(player, Sounds.Sound.INVALID_USE_OF_COMMAND);
                return true;
            }
            String warp = args[0];
            if (!warpManager.exists(warp)) {
                Messages.send(player, Messages.Message.WARP_NOT_EXISTS, "{warp}", warp);
                Titles.sendTitle(player, Titles.Title.WARP_NOT_EXISTS, "{warp}", warp);
                Sounds.playSound(player, Sounds.Sound.WARP_NOT_EXISTS);
                return true;
            }
            if (!player.hasPermission("justwarp.warp." + warp)) {
                Messages.send(player, Messages.Message.NO_WARP_PERMISSION);
                Titles.sendTitle(player, Titles.Title.NO_PERMISSION);
                Sounds.playSound(player, Sounds.Sound.NO_PERMISSION);
                return true;
            }
            if (warpManager.getWarpDelay(warp) == 0 || player.hasPermission("justwarp.delay.bypass") ) {
                warpManager.warp(player, warp);
                Messages.send(player, Messages.Message.TELEPORTED);
                Titles.sendTitle(player, Titles.Title.TELEPORTED);
                Sounds.playSound(player, Sounds.Sound.TELEPORTED);
                return true;
            }

            WarpHandler.warp(player, warp);

            return true;
        }
     return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return warpManager.getWarpList();
        }
        return null;
    }
}
