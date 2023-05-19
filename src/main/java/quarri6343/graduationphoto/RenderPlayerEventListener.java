package quarri6343.graduationphoto;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class RenderPlayerEventListener {
    public static List<PlayerEntity> playersOnPhotoFrame = new ArrayList<>();

    private Vector3f viewportPosition;

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (event.getPlayer() == Minecraft.getInstance().player) {
            return;
        }

        Matrix4f viewModelMatrix = event.getMatrixStack().last().pose();
        Vector3f viewportPosition = ProjectionUtility.worldToViewportPosition(viewModelMatrix, event.getPlayer().position());
        this.viewportPosition = viewportPosition;

        if (viewportPosition.x() >= -0.4 && viewportPosition.x() <= 0.4
                && viewportPosition.y() >= -0.6 && viewportPosition.y() <= 0.6) {
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
            if (viewportPosition != null) {
                // スクリーンのサイズ取得
                int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                // ビューポートの位置をスクリーン座標に変換
                float x = (viewportPosition.x() + 1) * screenWidth / 2f;
                float y = (-viewportPosition.y() + 1) * screenHeight / 2f;

                // 点を描画
                RenderSystem.disableTexture();

                Tessellator t = Tessellator.getInstance();
                BufferBuilder builder = t.getBuilder();
                builder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
                //builder.vertex(viewportPosition.x(), viewportPosition.y(), 0).color(255, 0, 0, 255).endVertex();

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
