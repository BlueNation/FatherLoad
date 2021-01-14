package com.github.BlueNation.FatherLoad;

import com.github.BlueNation.FatherLoad.client.renderer.entity.SidewaysRender;
import com.github.BlueNation.FatherLoad.entities.EntityDriller;
import com.github.BlueNation.FatherLoad.models.ModelDriller;
import com.github.BlueNation.FatherLoad.proxy.CommonProxy;
import com.github.BlueNation.FatherLoad.thing.item.DebugDrill;
import com.github.BlueNation.FatherLoad.thing.item.DebugDrillerPlacer;
import com.github.BlueNation.FatherLoad.world.*;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.EntityRegistry;
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

    public static FatherLoadTab fatherLoadTab;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modLog = event.getModLog();
        fatherLoadTab = new FatherLoadTab(MODID);

        //Debug Items
        DebugDrill.mainRegistry();
        DebugDrillerPlacer.mainRegistry();

        //WorldGen
        SLBiomes.mainRegistry();
        SLDimensionRegistry.mainRegistry();
        SLWorldRegistry.mainRegistry();

        // FIXME: 11/01/2021 This actually belongs the client proxy
        RenderingRegistry.registerEntityRenderingHandler(EntityDriller.class, new SidewaysRender(new ModelDriller(),
                MODID + ":textures/models/driller.png", 0F));

        //Entities
        EntityRegistry.registerModEntity(EntityDriller.class, "driller",100, instance, 100,1,false);
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
