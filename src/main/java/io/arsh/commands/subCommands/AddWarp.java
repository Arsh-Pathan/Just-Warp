package io.arsh.commands.subCommands;

import io.arsh.Messages;
import io.arsh.Sounds;
import io.arsh.Titles;
import io.arsh.WarpManager;
import io.arsh.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWarp extends SubCommand {

    private final WarpManager warpManager;

    public AddWarp(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Use to add warp locations.";
    }

    @Override
    public String getSyntax() {
        return "/justwarp add <name> <delay>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (sender instanceof Player player) {
            if (args.length != 3) {
                Messages.send(player, Messages.Message.INVALID_USE_OF_COMMAND);
                Titles.sendTitle(player, Titles.Title.INVALID_USE_OF_COMMAND);
                Sounds.playSound(player, Sounds.Sound.INVALID_USE_OF_COMMAND);
                return;
            }
            String warp = args[1];
            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException var9) {
                Messages.send(player, Messages.Message.INVALID_INPUT);
                Titles.sendTitle(player, Titles.Title.INVALID_INPUT);
                Sounds.playSound(player, Sounds.Sound.INVALID_INPUT);
                return;
            }
            if (Integer.parseInt(args[2]) < 0) {
                Messages.send(player, Messages.Message.INVALID_INPUT);
                Titles.sendTitle(player, Titles.Title.INVALID_INPUT);
                Sounds.playSound(player, Sounds.Sound.INVALID_INPUT);
                return;
            }
            if (warpManager.exists(warp)) {
                Messages.send(player, Messages.Message.WARP_ALREADY_EXISTS, "{warp}", warp);
                Titles.sendTitle(player, Titles.Title.WARP_ALREADY_EXISTS, "{warp}", warp);
                Sounds.playSound(player, Sounds.Sound.WARP_ALREADY_EXISTS);
                return;
            }
            Messages.send(player, Messages.Message.WARP_ADD, "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
            Titles.sendTitle(player, Titles.Title.WARP_ADD, "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
            Sounds.playSound(player, Sounds.Sound.WARP_ADD);
            warpManager.addWarp(warp, player.getLocation(), Integer.parseInt(args[2]));
        }
    }

}
