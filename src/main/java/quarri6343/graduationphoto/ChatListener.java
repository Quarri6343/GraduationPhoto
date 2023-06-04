package quarri6343.graduationphoto;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    }
}
