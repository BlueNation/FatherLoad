package com.github.BlueNation.FatherLoad.mechanics;

import net.minecraft.world.World;

public interface IBlockPositionFunction<T> {
    T apply(World world, int x, int y, int z);
}
