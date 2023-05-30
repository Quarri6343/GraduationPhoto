package quarri6343.graduationphoto;

import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static quarri6343.graduationphoto.Graduationphoto.*;

public class RenderEventListener {
    public static List<PlayerEntity> playersInPhoto = new ArrayList<>();

    private Vector3 debugGuiPosition;

    private static Matrix4 projectionMatrix;

    public static final IntBuffer VIEWPORT = GLAllocation.createByteBuffer(16 << 2).asIntBuffer();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        playersInPhoto.clear();
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (event.getPlayer() == Minecraft.getInstance().player || projectionMatrix == null) {
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
        
        Matrix4 viewModelMatrix = new Matrix4(event.getMatrixStack().last().pose());
        Vector3 screenPosition = Matrix4.gluProject(targetPos, viewModelMatrix, projectionMatrix, VIEWPORT);

        // スクリーン座標をgui座標に変換
        screenPosition.x = (float) screenPosition.x / Minecraft.getInstance().getWindow().getScreenWidth() * Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenPosition.y = (float) (Minecraft.getInstance().getWindow().getScreenHeight() - screenPosition.y) / Minecraft.getInstance().getWindow().getScreenHeight() * Minecraft.getInstance().getWindow().getGuiScaledHeight();

        this.debugGuiPosition = screenPosition;

        int imageX = (int) (mc.getWindow().getGuiScaledWidth() * photoX);
        int imageY = (int) (mc.getWindow().getGuiScaledHeight() * photoY);
        int imageWidth = (int) (mc.getWindow().getGuiScaledWidth()  * photoWidth);
        int imageHeight = (int) (mc.getWindow().getGuiScaledHeight() * photoHeight);

        BlockRayTraceResult rayTraceResult = mc.getCameraEntity().level.clip(
                new RayTraceContext(
                        mc.player.getEyePosition(1f), 
                        event.getPlayer().getEyePosition(1f), 
                        RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, mc.player));

        playersInPhoto.clear();
        if (screenPosition.x >= imageX && screenPosition.x <= imageX + imageWidth
                && screenPosition.y >= imageY && screenPosition.y <= imageY + imageHeight
                && rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            playersInPhoto.add(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        projectionMatrix = new Matrix4(event.getProjectionMatrix());
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        //debug
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (debugGuiPosition != null) {
                float x = (float) debugGuiPosition.x;
                float y = (float) debugGuiPosition.y;

                // 点を描画
                RenderSystem.disableTexture();

                Tessellator t = Tessellator.getInstance();
                BufferBuilder builder = t.getBuilder();
                builder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
                //builder.vertex(debugGuiPosition.x(), debugGuiPosition.y(), 0).color(255, 0, 0, 255).endVertex();

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
