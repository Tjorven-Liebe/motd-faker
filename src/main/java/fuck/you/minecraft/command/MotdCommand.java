package fuck.you.minecraft.command;

import de.tjorven.commandhandler.CommandHandler;
import de.tjorven.commandhandler.ICommand;
import fuck.you.minecraft.MinecraftServerHandler;

public class MotdCommand implements ICommand {

    @Override
    public void run(String label, String[] args) {
        if (args.length == 0) {
            return;
        }
        MinecraftServerHandler.imageToUse = args[0];
        System.out.println(args[0]);
    }
}
