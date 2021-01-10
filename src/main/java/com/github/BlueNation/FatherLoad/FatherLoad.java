package com.github.BlueNation.FatherLoad;

import com.github.BlueNation.FatherLoad.proxy.CommonProxy;
import com.github.BlueNation.FatherLoad.world.SLBiomes;
import com.github.BlueNation.FatherLoad.world.SLDimensionRegistry;
import com.github.BlueNation.FatherLoad.world.SLWorldRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import static com.github.BlueNation.FatherLoad.Reference.MODID;
import static com.github.BlueNation.FatherLoad.Reference.VERSION;

//Uncomment as part of shadow example
//import reactor.core.publisher.Flux;

@Mod(modid = MODID, name = Reference.NAME, version = VERSION)
public class FatherLoad {
    @Mod.Instance(MODID)
    public static FatherLoad instance;

    @SidedProxy(clientSide = "com.github.BlueNation.FatherLoad.proxy.ClientProxy",
                serverSide = "com.github.BlueNation.FatherLoad.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Logger modLog;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modLog = event.getModLog();

        SLBiomes.mainRegistry();
        SLDimensionRegistry.mainRegistry();
        SLWorldRegistry.mainRegistry();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {

    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {

    }
}
