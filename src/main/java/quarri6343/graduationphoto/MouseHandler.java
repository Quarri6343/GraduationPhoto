package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseHandler {

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseClickedEvent.Pre event) {
        if (event.getGui() instanceof net.minecraft.client.gui.screen.ChatScreen) {
            if(event.getButton() == 0){ //左クリックで始点を設定
                Graduationphoto.photoX = (float) event.getMouseX() / event.getGui().width;
                Graduationphoto.photoY = (float) event.getMouseY() / event.getGui().height;
                if(Graduationphoto.photoX + Graduationphoto.photoWidth > 1)
                    Graduationphoto.photoWidth = 1 - Graduationphoto.photoX;
                if(Graduationphoto.photoY + Graduationphoto.photoHeight > 1)
                    Graduationphoto.photoHeight = 1 - Graduationphoto.photoY;
            }
            else if(event.getButton() == 1){ //右クリックで終点を設定
                float x = (float) event.getMouseX() / event.getGui().width;
                float y = (float) event.getMouseY() / event.getGui().height;
                if(Graduationphoto.photoX > x || Graduationphoto.photoY > y){
                    Minecraft.getInstance().gui.getChat().addMessage(new StringTextComponent("枠の始点より右下側を右クリックしてください"));
                    return;
                }
                Graduationphoto.photoWidth = x - Graduationphoto.photoX;
                Graduationphoto.photoHeight = y - Graduationphoto.photoY;
            }
        }
    }
}
