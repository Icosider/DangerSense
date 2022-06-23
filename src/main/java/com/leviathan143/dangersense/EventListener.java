package com.leviathan143.dangersense;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = DangerSense.MOD_ID)
public class EventListener {
    private static boolean dangerNear;
    private static final ResourceLocation DANGER_OVERLAY = new ResourceLocation(DangerSense.MOD_ID, "misc/danger_sense_overlay.png");

    private EventListener() {
        throw new UnsupportedOperationException();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null || event.phase == TickEvent.Phase.START) {
            return;
        }
        if (player.ticksExisted % 20 == 0) {
            dangerNear = nearMonsters(player, SenseConfig.searchRadius);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && dangerNear) {
            GlStateManager.depthMask(false);
            GlStateManager.disableAlpha();
            Minecraft.getMinecraft().renderEngine.bindTexture(DANGER_OVERLAY);

            renderRect(event.getResolution().getScaledWidth_double(), event.getResolution().getScaledHeight_double());

            GlStateManager.enableAlpha();
            GlStateManager.depthMask(true);
        }
    }

    private static boolean nearMonsters(EntityPlayer owner, double radius) {
        AxisAlignedBB searchBox = owner.getEntityBoundingBox().grow(radius, 4.0, radius);
        return !owner.world.getEntitiesWithinAABB(EntityLiving.class, searchBox, IMob.MOB_SELECTOR).isEmpty();
    }

    private static void renderRect(double w, double h) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(0.0, 0.0 + h, -90.0).tex(0.0, 1.0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos(0.0 + w, 0.0 + h, -90.0).tex(1.0, 1.0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos(0.0 + w, 0.0, -90.0).tex(1.0, 0.0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).color(1F, 1F, 1F, 1F).endVertex();

        tess.draw();
    }
}