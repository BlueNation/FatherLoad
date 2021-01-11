package com.github.BlueNation.FatherLoad.thing.item;

import com.github.BlueNation.FatherLoad.FatherLoad;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

import static com.github.BlueNation.FatherLoad.Reference.MODID;

public class DebugDrillerPlacer extends Item {
    public static DebugDrillerPlacer INSTANCE;

    public DebugDrillerPlacer() {
        setUnlocalizedName(MODID + ".diggerplacer");
        setHasSubtypes(false);
        setTextureName(MODID + ":debugDrillerPlacer");
        setCreativeTab(FatherLoad.fatherLoadTab);
        this.maxStackSize = 64;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
                             int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            Block block = par3World.getBlock(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double d0 = 0.0D;

            if (par7 == 1 && block.getRenderType() == 11) {
                d0 = 0.5D;
            }

            Entity entity = spawnEntity(par3World, par4 + 0.5D, par5 + d0, par6 + 0.5D);

            if (entity != null) {
                if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                }

                if (!par2EntityPlayer.capabilities.isCreativeMode) {
                    --par1ItemStack.stackSize;
                }
            }

        }
        return true;
    }


    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            MovingObjectPosition movingobjectposition =
                    getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

            if (movingobjectposition != null) {
                if (movingobjectposition.typeOfHit == MovingObjectPosition
                        .MovingObjectType.BLOCK) {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!par2World.canMineBlock(par3EntityPlayer, i, j, k)) {
                        return par1ItemStack;
                    }

                    if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition
                            .sideHit, par1ItemStack)) {
                        return par1ItemStack;
                    }

                    if (par2World.getBlock(i, j, k) instanceof BlockLiquid) {
                        Entity entity = spawnEntity(par2World, i, j, k);

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && par1ItemStack
                                    .hasDisplayName()) {
                                ((EntityLiving) entity).setCustomNameTag(par1ItemStack
                                        .getDisplayName());
                            }

                            if (!par3EntityPlayer.capabilities.isCreativeMode) {
                                --par1ItemStack.stackSize;
                            }
                        }
                    }
                }

            }
        }
        return par1ItemStack;
    }

    public Entity spawnEntity(World parWorld, double parX, double parY, double parZ) {
        Entity entityToSpawn = null;
        if (!parWorld.isRemote) {
            String entityToSpawnNameFull = MODID + "." + "driller";
            if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull)) {
                entityToSpawn = EntityList.createEntityByName(entityToSpawnNameFull, parWorld);
                entityToSpawn.setLocationAndAngles(parX, parY, parZ, MathHelper.wrapAngleTo180_float(parWorld.rand.nextFloat() * 360.0F), 0.0F);
                parWorld.spawnEntityInWorld(entityToSpawn);
            } else {
                //DEBUG
                System.out.println("Entity not found " + entityToSpawnNameFull);
            }
        }
        return entityToSpawn;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item parItem, CreativeTabs parTab, List parList) {
        parList.add(new ItemStack(parItem, 1, 0));
    }

    public static void mainRegistry() {
        INSTANCE = new DebugDrillerPlacer();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
