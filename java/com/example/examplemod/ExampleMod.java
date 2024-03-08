package com.example.examplemod;

import com.mojang.logging.LogUtils;
import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.item.ModCreativeModTabs;
import com.example.examplemod.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ExampleMod.MOD_ID)

public class ExampleMod {
	public static final String MOD_ID = "examplemod"; //<---
	public static final String MODNAME = "Lapdqpiston";
	public static String VERSION = "0.0.1";
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public ExampleMod() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

	    ModCreativeModTabs.register(modEventBus);

	    ModItems.register(modEventBus);
	    ModBlocks.register(modEventBus);

	    modEventBus.addListener(this::commonSetup);

	    MinecraftForge.EVENT_BUS.register(this);
	    modEventBus.addListener(this::addCreative);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			//event.accept(ModItems.PISTON);
	        //event.accept(ModItems.RAW_SAPPHIRE);
	    }
	}

	    // You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

	    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
	    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
	        @SubscribeEvent
	    public static void onClientSetup(FMLClientSetupEvent event) {

	    }
	}
}