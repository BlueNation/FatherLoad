package com.github.BlueNation.FatherLoad.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

public class StartFatherLoad extends CommandBase {
    private static final List<String> ALIASES =new ArrayList<>();
    static {
        ALIASES.add(MODID);
    }

    @Override
    public String getCommandName() {
        return ALIASES.get(0);
    }

    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {

    }

    @Override
    public int getRequiredPermissionLevel() {
        return super.getRequiredPermissionLevel();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return super.canCommandSenderUseCommand(p_71519_1_);
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return super.addTabCompletionOptions(p_71516_1_, p_71516_2_);
    }
}
