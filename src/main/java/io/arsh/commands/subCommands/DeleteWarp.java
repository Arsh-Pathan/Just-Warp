package io.arsh.commands.subCommands;


import io.arsh.Messages;
import io.arsh.Sounds;
import io.arsh.Titles;
import io.arsh.WarpManager;
import io.arsh.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteWarp extends SubCommand {

    private final WarpManager warpManager;

    public DeleteWarp(io.arsh.WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Use to delete the warps.";
    }

    @Override
    public String getSyntax() {
        return "/justwarp delete <name>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 2) {
                Messages.send(player, Messages.Message.INVALID_USE_OF_COMMAND);
                Titles.sendTitle(player, Titles.Title.INVALID_USE_OF_COMMAND);
                Sounds.playSound(player, Sounds.Sound.INVALID_USE_OF_COMMAND);
                return;
            }

            String warp = args[1];
            if (!warpManager.exists(warp)) {
                Messages.send(player, Messages.Message.WARP_NOT_EXISTS, "{warp}", warp);
                Titles.sendTitle(player, Titles.Title.WARP_NOT_EXISTS, "{warp}", warp);
                Sounds.playSound(player, Sounds.Sound.WARP_NOT_EXISTS);
                return;
            }
            Messages.send(player, Messages.Message.WARP_DELETE, "{warp}", warp);
            Titles.sendTitle(player, Titles.Title.WARP_DELETE, "{warp}", warp);
            Sounds.playSound(player, Sounds.Sound.WARP_DELETE);
            warpManager.deleteWarp(warp);
        }
    }

}
