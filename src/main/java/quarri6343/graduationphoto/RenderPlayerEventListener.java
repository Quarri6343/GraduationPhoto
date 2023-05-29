package quarri6343.graduationphoto;

import codechicken.lib.vec.Vector3;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class RenderPlayerEventListener {
    public static List<PlayerEntity> playersOnPhotoFrame = new ArrayList<>();

    private Vector3 screenPosition;

    public static final IntBuffer VIEWPORT = GLAllocation.createByteBuffer(16 << 2).asIntBuffer();

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (event.getPlayer() == Minecraft.getInstance().player) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, VIEWPORT);
        Entity entity = mc.getCameraEntity();
        float x = (float) (entity.xo + (entity.getX() - entity.xo) * (double) event.getPartialRenderTick());
        float y = (float) (entity.yo + (entity.getY() - entity.yo) * (double) event.getPartialRenderTick());
        float z = (float) (entity.zo + (entity.getZ() - entity.zo) * (double) event.getPartialRenderTick());
        Vector3 targetPos = Vector3.fromEntity(event.getPlayer());
        targetPos.subtract(x, y, z);
        
        Matrix4f viewModelMatrix = event.getMatrixStack().last().pose();
        Vector3 screenPosition = ProjectionUtility.worldToScreenPosition(viewModelMatrix, targetPos, VIEWPORT);
        
        this.screenPosition = screenPosition;

        int imageX = (int) (mc.getWindow().getScreenWidth() * 0.3);
        int imageY = (int) (mc.getWindow().getScreenHeight() * 0.2);
        int imageWidth = (int) (mc.getWindow().getScreenWidth()  * 0.4);
        int imageHeight = (int) (mc.getWindow().getScreenHeight() * 0.6);
        
        if (screenPosition.x >= imageX && screenPosition.x <= imageX + imageWidth
                && screenPosition.y >= imageY && screenPosition.y <= imageY + imageHeight) {
            playersOnPhotoFrame.add(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Matrix4f viewModelMatrix = event.getMatrixStack().last().pose();
        Matrix4f projectionMatrix = event.getProjectionMatrix();
        ProjectionUtility.updateViewProjectionMatrix(viewModelMatrix, projectionMatrix);
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (screenPosition != null) {
                // スクリーンのサイズ取得
                int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                // ビューポートの位置をスクリーン座標に変換
//                float x = (float) (screenPosition.x + 1) * screenWidth / 2f;
//                float y = (float) (-screenPosition.y + 1) * screenHeight / 2f;
                float x = (float) screenPosition.x / Minecraft.getInstance().getWindow().getScreenWidth() * Minecraft.getInstance().getWindow().getGuiScaledWidth();
                float y = (float) (Minecraft.getInstance().getWindow().getScreenHeight() - screenPosition.y) / Minecraft.getInstance().getWindow().getScreenHeight() * Minecraft.getInstance().getWindow().getGuiScaledHeight();

                // 点を描画
                RenderSystem.disableTexture();

                Tessellator t = Tessellator.getInstance();
                BufferBuilder builder = t.getBuilder();
                builder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
                //builder.vertex(screenPosition.x(), screenPosition.y(), 0).color(255, 0, 0, 255).endVertex();

                builder.vertex(x - 4f, y + 4f, -90.0D).endVertex();
                builder.vertex(x + 4f, y + 4f, -90.0D).endVertex();
                builder.vertex(x + 4f, y - 4f, -90.0D).endVertex();
                builder.vertex(x - 4f, y - 4f, -90.0D).endVertex();

                t.end();

                RenderSystem.enableTexture();
            }
        }
    }
}
