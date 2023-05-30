package quarri6343.graduationphoto;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class FlashScreenRenderer {

    /**
     * 0でflash開始、10でflash終了
     */
    public static int flashScreen = 15;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (flashScreen < 15) {
            RenderSystem.enableBlend();
            // 点を描画
            RenderSystem.disableTexture();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F - (float) flashScreen / 15f);

            Tessellator t = Tessellator.getInstance();
            BufferBuilder builder = t.getBuilder();
            builder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
            //builder.vertex(debugScreenPosition.x(), debugScreenPosition.y(), 0).color(255, 0, 0, 255).endVertex();

            builder.vertex(0, Minecraft.getInstance().getWindow().getGuiScaledHeight(), -90.0D).endVertex();
            builder.vertex(Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), -90.0D).endVertex();
            builder.vertex(Minecraft.getInstance().getWindow().getGuiScaledWidth(), 0, -90.0D).endVertex();
            builder.vertex(0, 0, -90.0D).endVertex();

            t.end();

            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            flashScreen++;
        }
    }
}
