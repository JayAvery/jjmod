package com.jayavery.jjmod.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME)
public class Main {

    public static final String MODID = "jjmod";
    public static final String VERSION = "0.2";
    public static final String NAME = "JJ Mod";

    @SidedProxy(clientSide = "com.jayavery.jjmod.main.ClientProxy",
            serverSide = "com.jayavery.jjmod.main.CommonProxy")
    public static CommonProxy proxy;

    @Instance
    public static Main instance = new Main();

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        
        // Read config, create + register blocks/items/etc
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        
        // Data structures, crafting recipes, register handler
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {

        // Communicate with other mods
        proxy.postInit();
    }
}