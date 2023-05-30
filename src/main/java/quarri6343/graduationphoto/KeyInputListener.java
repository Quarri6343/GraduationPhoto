package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeyInputListener {
    private final Minecraft minecraft;
    private final KeyBinding keyBind1;
    private final KeyBinding keyBind2;

    public static final KeyBinding openScreenKeyBind = new KeyBinding("写真を撮る", GLFW.GLFW_KEY_Q, "集合写真");
    public static final KeyBinding disableFlashKeyBind = new KeyBinding("フラッシュの有効を切り替え", GLFW.GLFW_KEY_M, "集合写真");

    public KeyInputListener(Minecraft minecraft, KeyBinding keyBind1, KeyBinding keyBind2) {
        this.minecraft = minecraft;
        this.keyBind1 = keyBind1;
        this.keyBind2  = keyBind2;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keyBind1.isDown()) {
            minecraft.setScreen(new ScreenShotRenderer());
        }
        if (keyBind2.isDown()) {
            Graduationphoto.doFlash = !Graduationphoto.doFlash;
        }
    }
}