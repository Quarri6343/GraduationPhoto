package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class KeyInputListener {
    private final Minecraft minecraft;

    public static final KeyBinding openScreenKeyBind = new KeyBinding("写真を撮る", GLFW.GLFW_KEY_Z, "集合写真");
    public static final KeyBinding disableFlashKeyBind = new KeyBinding("フラッシュの有効を切り替え", GLFW.GLFW_KEY_M, "集合写真");

    public static final KeyBinding randomFrameKeyBind = new KeyBinding("枠の場所と大きさをランダムにする", GLFW.GLFW_KEY_R, "集合写真");

    public KeyInputListener(Minecraft minecraft) {
        ClientRegistry.registerKeyBinding(openScreenKeyBind);
        ClientRegistry.registerKeyBinding(disableFlashKeyBind);
        ClientRegistry.registerKeyBinding(randomFrameKeyBind);
        this.minecraft = minecraft;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openScreenKeyBind.isDown()) {
            minecraft.setScreen(new ScreenShotRenderer());
        }
        if (disableFlashKeyBind.isDown()) {
            Graduationphoto.doFlash = !Graduationphoto.doFlash;
        }
        if(randomFrameKeyBind.isDown()){
            float x;
            float y;
            float width;
            float height;
            do {
                x = new Random().nextFloat();
                y = new Random().nextFloat();
                width = new Random().nextFloat();
                height = new Random().nextFloat();
            } while (x + width > 1 || y + height > 1);
            Graduationphoto.photoX = x;
            Graduationphoto.photoY = y;
            Graduationphoto.photoWidth = width;
            Graduationphoto.photoHeight = height;
        }
    }
}