package com.github.BlueNation.FatherLoad;

import com.github.BlueNation.FatherLoad.thing.item.DebugDrill;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class FatherLoadTab extends CreativeTabs {
    public FatherLoadTab(String name) {
        super(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem() {
        return DebugDrill.INSTANCE;
    }

}
