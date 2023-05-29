package quarri6343.graduationphoto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class ScreenShotRenderer extends Screen {
    private DynamicTexture texture;

    protected ScreenShotRenderer() {
        super(new TranslationTextComponent("タイトル"));
    }

    @Override
    protected void init() {
        takeScreenshot();
    }

    public void takeScreenshot() {
        Minecraft mc = Minecraft.getInstance();
        NativeImage nativeImage = ScreenShotHelper.takeScreenshot(mc.getWindow().getWidth(), mc.getWindow().getHeight(), mc.getMainRenderTarget());
        
        texture = new DynamicTexture(cropImage(nativeImage,
                (int) (mc.getWindow().getWidth() * 0.3), (int) (mc.getWindow().getHeight() * 0.2), (int) (mc.getWindow().getWidth() * 0.4), (int) (mc.getWindow().getHeight() * 0.6)));
    }

    public NativeImage cropImage(NativeImage sourceImage, int startX, int startY, int width, int height) {
        NativeImage croppedImage = new NativeImage(width, height, false);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sourceX = startX + x;
                int sourceY = startY + y;
                int pixel = sourceImage.getPixelRGBA(sourceX, sourceY);
                croppedImage.setPixelRGBA(x, y, pixel);
            }
        }

        return croppedImage;
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int textureWidth = (int)(width * 0.4);
        int textureHeight = (int)(height * 0.6);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation textureLocation = Minecraft.getInstance().getTextureManager().register("screenshot", texture);
        Minecraft.getInstance().getTextureManager().bind(textureLocation);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2,height / 2,0);
        GL11.glRotatef(15f, 0f, 0f, 1f);
        blit(matrixStack, - textureWidth / 2, - textureHeight / 2, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        GL11.glPopMatrix();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
