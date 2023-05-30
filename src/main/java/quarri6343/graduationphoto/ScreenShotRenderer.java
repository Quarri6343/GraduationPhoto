package quarri6343.graduationphoto;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static quarri6343.graduationphoto.Graduationphoto.*;

public class ScreenShotRenderer extends Screen {

    public static int flashTime = 10;

    /**
     * 0でflash開始、flashTimeでflash終了
     */
    public int flashScreen = 0;
    
    private DynamicTexture texture;
    private List<GameProfile> playersNotInPhoto = new ArrayList<>();

    protected ScreenShotRenderer() {
        super(new TranslationTextComponent("タイトル"));
    }

    @Override
    protected void init() {
        takeScreenshot();
        listPlayerNotOnScreenshot();
    }

    public void takeScreenshot() {
        Minecraft mc = Minecraft.getInstance();
        NativeImage nativeImage = ScreenShotHelper.takeScreenshot(mc.getWindow().getWidth(), mc.getWindow().getHeight(), mc.getMainRenderTarget());
        
        texture = new DynamicTexture(cropImage(nativeImage,
                (int) (mc.getWindow().getWidth() * photoX), (int) (mc.getWindow().getHeight() * photoY), (int) (mc.getWindow().getWidth() * photoWidth), (int) (mc.getWindow().getHeight() * photoHeight)));
        
        mc.player.playSound(FILM_SOUND, 1.0f, 1.0f);
    }
    
    public void listPlayerNotOnScreenshot(){
        this.playersNotInPhoto.clear();

        Collection<NetworkPlayerInfo> playersC=Minecraft.getInstance().getConnection().getOnlinePlayers();
        playersC.forEach((loadedPlayer) -> {
            String loadedPlayerName = loadedPlayer.getProfile().getName();
            if(RenderEventListener.playersInPhoto.stream().noneMatch(playerEntity -> playerEntity.getName().getString().equals(loadedPlayerName))){
                if(!loadedPlayerName.equals(Minecraft.getInstance().player.getName().getString()))
                    playersNotInPhoto.add(loadedPlayer.getProfile());
            }
        });
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

        for (int i = 0; i < playersNotInPhoto.size(); i++) {
            Minecraft minecraft = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(playersNotInPhoto.get(i));
            ResourceLocation resourceLocation =  map.containsKey(MinecraftProfileTexture.Type.SKIN) ? minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) : DefaultPlayerSkin.getDefaultSkin(PlayerEntity.createPlayerUUID(playersNotInPhoto.get(i)));
            Minecraft.getInstance().getTextureManager().bind(resourceLocation);
            blit(matrixStack, - textureWidth / 2 + i * 36, - textureHeight / 2,  32, 32, 8.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.enableBlend();
            blit(matrixStack, - textureWidth / 2 + i * 36, - textureHeight / 2,  32, 32, 40.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.disableBlend();
            //blit(MatrixStack matrixStack, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight)
        }
        
        GL11.glPopMatrix();
        
        renderFlash();
    }
    
    public void renderFlash(){
        if (flashScreen < flashTime) {
            if(!Graduationphoto.doFlash){
                flashScreen = flashTime;
                return;
            }

            RenderSystem.enableBlend();
            // 点を描画
            RenderSystem.disableTexture();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F - (float) flashScreen / (float) flashTime);

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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
