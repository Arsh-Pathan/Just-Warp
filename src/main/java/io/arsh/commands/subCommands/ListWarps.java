package io.arsh.commands.subCommands;

import io.arsh.Messages;
import io.arsh.Sounds;
import io.arsh.Titles;
import io.arsh.WarpManager;
import io.arsh.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListWarps extends SubCommand {

    private final WarpManager warpManager;

    public ListWarps(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Use to get the list of warps.";
    }

    @Override
    public String getSyntax() {
        return "/justwarp list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 1) {
                Messages.send(player, Messages.Message.INVALID_USE_OF_COMMAND);
                Titles.sendTitle(player, Titles.Title.INVALID_USE_OF_COMMAND);
                Sounds.playSound(player, Sounds.Sound.INVALID_USE_OF_COMMAND);
                return;
            }
            if (warpManager.getWarpList().isEmpty()) {
                Messages.send(player, Messages.Message.WARP_LIST_EMPTY);
                Sounds.playSound(player, Sounds.Sound.INVALID_USE_OF_COMMAND);
                return;
            }
            Messages.send(player, Messages.Message.WARP_LIST);
            String message = "";
            for (String warpName : warpManager.getWarpList()) {
                message += " " + ChatColor.GREEN + warpName + ChatColor.YELLOW + ",";
            }
            message = message.substring(0, message.length() - 1);
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 1);
        }
    }

}
