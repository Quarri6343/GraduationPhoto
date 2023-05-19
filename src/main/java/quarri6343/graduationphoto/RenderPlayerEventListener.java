package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class RenderPlayerEventListener {
    public static List<PlayerEntity> playersOnPhotoFrame = new ArrayList<>();

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (event.getPlayer() == Minecraft.getInstance().player) {
            return;
        }

        Vector3f viewportPosition = ProjectionUtility.worldToViewportPosition(event.getPlayer().position());
        
        if (viewportPosition.x() >= -0.4 && viewportPosition.x() <= 0.4
                && viewportPosition.y() >= -0.6 && viewportPosition.y() <= 0.6) {
            playersOnPhotoFrame.add(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ProjectionUtility.updateViewProjectionMatrix();
    }
}
