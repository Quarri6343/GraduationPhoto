package quarri6343.graduationphoto;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static quarri6343.graduationphoto.KeyInputListener.disableFlashKeyBind;
import static quarri6343.graduationphoto.KeyInputListener.openScreenKeyBind;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("graduationphoto")
public class Graduationphoto {
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static float photoX = 0.3f;
    public static float photoY = 0.2f;
    public static float photoWidth = 0.4f;
    public static float photoHeight = 0.6f;
    
    public static boolean doFlash = true;

    public static final SoundEvent FILM_SOUND = new SoundEvent(
            new ResourceLocation("graduationphoto", "film")
    ).setRegistryName("custom_sound");

    public Graduationphoto() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        if (FMLEnvironment.dist != Dist.CLIENT) {
            return;
        }
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        
        MinecraftForge.EVENT_BUS.register(new KeyInputListener(Minecraft.getInstance()));

        MinecraftForge.EVENT_BUS.register(new PhotoFrameRenderer());
        MinecraftForge.EVENT_BUS.register(new RenderEventListener());
        MinecraftForge.EVENT_BUS.register(new MouseHandler());
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    FILM_SOUND
            );
        }
    }
}
