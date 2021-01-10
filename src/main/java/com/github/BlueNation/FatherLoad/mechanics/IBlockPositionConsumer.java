package com.github.BlueNation.FatherLoad.mechanics;

import net.minecraft.world.World;

public interface IBlockPositionConsumer {
    void consume(World world, int x, int y, int z);
}
