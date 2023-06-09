package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

import static quarri6343.graduationphoto.Graduationphoto.*;

public class PhotoFrameRenderer {
    private final ResourceLocation imageLocation = new ResourceLocation("graduationphoto", "frame.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.screen != null && !(mc.screen instanceof net.minecraft.client.gui.screen.ChatScreen))
            return;
        
        int imageX = (int) (mc.getWindow().getGuiScaledWidth() * photoX);
        int imageY = (int) (mc.getWindow().getGuiScaledHeight() * photoY);
        int imageWidth = (int) (mc.getWindow().getGuiScaledWidth()  * photoWidth);
        int imageHeight = (int) (mc.getWindow().getGuiScaledHeight() * photoHeight);
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft.getInstance().getTextureManager().bind(imageLocation);
            AbstractGui.blit(event.getMatrixStack(), imageX, imageY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        }
        
        if(RenderEventListener.playersInPhoto.size() > 0){
            String text = "枠内の人数:" + RenderEventListener.playersInPhoto.size();
            AbstractGui.drawCenteredString(event.getMatrixStack(), Minecraft.getInstance().font, text, 
                     Minecraft.getInstance().font.width(text) / 2, 0, 0);
        }
    }
}
