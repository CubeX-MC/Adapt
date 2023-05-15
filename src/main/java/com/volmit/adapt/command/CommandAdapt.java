package com.volmit.adapt.command;


import com.volmit.adapt.command.boost.CommandBoost;
import com.volmit.adapt.command.gui.CommandGUI;
import com.volmit.adapt.command.item.CommandExperience;
import com.volmit.adapt.command.item.CommandItem;
import com.volmit.adapt.command.item.CommandKnowledge;
import com.volmit.adapt.util.C;
import com.volmit.adapt.util.command.FConst;
import io.github.mqzn.commands.annotations.Command;
import io.github.mqzn.commands.annotations.Default;
import io.github.mqzn.commands.annotations.subcommands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command(name = "adapt", permission = "adapt.main")
@SubCommand(CommandBoost.class)
//@SubCommand(CommandDebug.class)
//@SubCommand(CommandParticle.class)
//@SubCommand(CommandPGA.class)
//@SubCommand(CommandPGS.class)
//@SubCommand(CommandSound.class)
//@SubCommand(CommandVerbose.class)
@SubCommand(CommandGUI.class)
@SubCommand(CommandExperience.class) // child command
@SubCommand(CommandItem.class) // parent command
@SubCommand(CommandKnowledge.class) // child command
public class CommandAdapt {

    @Default
    public void info(@NotNull CommandSender sender) {
        FConst.success(" --- === " + C.GRAY + "[" + C.DARK_RED + "Adapt Help" + C.GRAY + "]: " + " === ---");
        FConst.info("/adapt (this command)").send(sender);
        FConst.info("/adapt item (The Cheat Items Subcommand)").send(sender);
        FConst.info("/adapt gui (The GUI Subcommand)").send(sender);
        FConst.info("/adapt test (The developer testing Subcommands)").send(sender);
        FConst.info("/adapt verbose (The verbose Subcommand)").send(sender);

    }

}