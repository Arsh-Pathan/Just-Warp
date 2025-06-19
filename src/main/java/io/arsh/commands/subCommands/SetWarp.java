package io.arsh.commands.subCommands;


import io.arsh.Messages;
import io.arsh.Sounds;
import io.arsh.Titles;
import io.arsh.WarpManager;
import io.arsh.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarp extends SubCommand {

    private final WarpManager warpManager;

    public SetWarp(io.arsh.WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Use to set warp.";
    }

    @Override
    public String getSyntax() {
        return "/justwarp set <name> <delay>";
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
            if (!warpManager.exists(warp)) {
                Messages.send(player, Messages.Message.WARP_NOT_EXISTS, "{warp}", warp);
                Titles.sendTitle(player, Titles.Title.WARP_NOT_EXISTS, "{warp}", warp);
                Sounds.playSound(player, Sounds.Sound.WARP_NOT_EXISTS);
                return;
            }
            Messages.send(player, Messages.Message.WARP_SET, "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
            Titles.sendTitle(player, Titles.Title.WARP_SET, "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
            Sounds.playSound(player, Sounds.Sound.WARP_SET);
            warpManager.setWarp(warp, player.getLocation(), Integer.parseInt(args[2]));
        }
    }

}
