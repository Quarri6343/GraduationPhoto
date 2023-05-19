package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class PhotoFrameRenderer {
    private final ResourceLocation imageLocation = new ResourceLocation("graduationphoto", "frame.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        int imageX = (int) (mc.getWindow().getGuiScaledWidth() * 0.3);
        int imageY = (int) (mc.getWindow().getGuiScaledHeight() * 0.2);
        int imageWidth = (int) (mc.getWindow().getGuiScaledWidth()  * 0.4);
        int imageHeight = (int) (mc.getWindow().getGuiScaledHeight() * 0.6);
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft.getInstance().getTextureManager().bind(imageLocation);
            AbstractGui.blit(event.getMatrixStack(), imageX, imageY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        }


        if(RenderPlayerEventListener.playersOnPhotoFrame.size() > 0){
            AbstractGui.drawCenteredString(event.getMatrixStack(), Minecraft.getInstance().font, "枠内にプレイヤーがいます", 100, 100, 0);
        }
        RenderPlayerEventListener.playersOnPhotoFrame.clear();
    }
}
