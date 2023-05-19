package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputListener {
    private final Minecraft minecraft;
    private final KeyBinding keybind;

    public KeyInputListener(Minecraft minecraft, KeyBinding keybind) {
        this.minecraft = minecraft;
        this.keybind = keybind;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keybind.isDown()) {
            minecraft.setScreen(new ScreenShotRenderer());
        }
    }
}