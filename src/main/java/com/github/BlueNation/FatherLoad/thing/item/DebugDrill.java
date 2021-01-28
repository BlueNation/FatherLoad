package com.github.BlueNation.FatherLoad.thing.item;

import com.github.BlueNation.FatherLoad.FatherLoad;
import com.github.BlueNation.FatherLoad.Reference;
import com.github.BlueNation.FatherLoad.mechanics.BigBlockHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

import static com.github.BlueNation.FatherLoad.Reference.MODID;


public class DebugDrill extends Item {
    public static DebugDrill INSTANCE;

    public DebugDrill() {
        setMaxStackSize(1);
        setUnlocalizedName(MODID + ".drill");
        setTextureName(MODID + ":itemFrontRotate");
        setCreativeTab(FatherLoad.fatherLoadTab);
        setHasSubtypes(true);
    }

    //rightclick on block serv/client
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world,
                                  int x, int y, int z, int side,
                                  float hitX, float hitY, float hitZ) {
        //FatherLoad.proxy.bigBlockBreakingParticle(world, x,y,z, side, getMaxItemUseDuration(stack));
        return false;
    }

    //rightclick on block serv/client if use first didnt returned true
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world,
                             int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        return false;
    }

    //when pre-break serv/client
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        return false;//prevent harvesting
    }

    //when break serv/client
    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block,
                                    int x, int y, int z,
                                    EntityLivingBase entityLivingBase) {
        int[] abc = BigBlockHandler.getABC(x, y, z);
        BigBlockHandler.setBigBlockToAir(world,abc[0],abc[1],abc[2]);
        return true;//add stats
    }

    //in air serv/client
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        entityPlayer.setItemInUse(itemStack,getMaxItemUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return itemStack.stackTagCompound.getInteger("speeds");
    }

    //when in inventory each tick serv/client
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
        super.onUpdate(itemStack, world, entity, slot, isHeld);
    }

    //when using each tick serv/client
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if(count<getMaxItemUseDuration(stack)){
            //duration -1 times
            if(player instanceof EntityClientPlayerMP)
                FatherLoad.proxy.renderDrillingParticlesOn(player);//Force run only on client side of client
        }else {
            //final tick
        }
    }

    //when stopped using or timed out serv/client, not triggered when swapping dropping etc.. so cancellable
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int remainingDuration) {
        super.onPlayerStoppedUsing(itemStack, world, entityPlayer, remainingDuration);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return false;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(withSpeeds(1));
        list.add(withSpeeds(2));
        list.add(withSpeeds(4));
        list.add(withSpeeds(8));
        list.add(withSpeeds(16));
        list.add(withSpeeds(32));
        list.add(withSpeeds(64));
    }

    private ItemStack withSpeeds(int speeds){
        ItemStack stack = new ItemStack(this, 1, 0);
        stack.stackTagCompound=new NBTTagCompound();
        stack.stackTagCompound.setInteger("speeds",speeds);
        return stack;
    }

    public static void mainRegistry() {
        INSTANCE = new DebugDrill();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return super.getHarvestLevel(stack, toolClass);
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        return super.getDigSpeed(itemstack, block, metadata);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List list, boolean shift) {
        list.add(Reference.MARKING_DEBUG);
        list.add("Speeds: "+stack.stackTagCompound.getInteger("speeds"));
    }
}
