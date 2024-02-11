package com.mirapose.client;


import com.mojang.blaze3d.systems.RenderSystem;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MiraOverlay {
    private static final ResourceLocation INACTIVE_BAR = new ResourceLocation("mirapose",
            "textures/whole_bar.png");
//    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation("mirapose",
//            "textures/thirst/empty_thirst.png");
    public static final IGuiOverlay HUD_MIRA = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, INACTIVE_BAR);
    guiGraphics.blit(INACTIVE_BAR, x + 8 + 100, y -30,0,0,83,25,83,25);

//        for(int i = 0; i < 10; i++) {
//            guiGraphics.blit(INACTIVE_BAR, x + 8 + (i * 8), y -51, 0, 0, 13, 12,
//                    13, 12);
//        }

       // RenderSystem.setShaderTexture(0, FILLED_THIRST);

//        for(int i = 0; i < 100; i++) {
//            if(ClientMiraData.getMira() > i) {
//                guiGraphics.blit(FILLED_THIRST, x + 8 + (i * 8), y -51, 0, 0, 13, 12,
//                        13, 12);
//            } else {
//                break;
//            }
//        }
    });
}
