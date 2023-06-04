package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ChatListener {
    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event) {
        if(event.getMessage().getContents().equals("#photo")){
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(new ScreenShotRenderer());
        }
        else if(event.getMessage().getContents().equals("#reset")){
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(null);
        }
        else if(event.getMessage().getContents().equals("#random")){
            event.setCanceled(true);
            
            float x;
            float y;
            float width;
            float height;
            do {
                x = new Random().nextFloat();
                y = new Random().nextFloat();
                width = new Random().nextFloat();
                height = new Random().nextFloat();
            } while (x + width > 1 || y + height > 1);
            Graduationphoto.photoX = x;
            Graduationphoto.photoY = y;
            Graduationphoto.photoWidth = width;
            Graduationphoto.photoHeight = height;
        }
    }
}
