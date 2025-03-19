package com.sg.compass;

import java.util.Arrays;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.CompassItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SGCompassClient implements ClientModInitializer {
	private static final Identifier HUD_LAYER = Identifier.of(SGCompass.MOD_ID, "compass-layer");
	@Override
	public void onInitializeClient() {
		HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.HOTBAR_AND_BARS, HUD_LAYER, SGCompassClient::render));
		
	}

	private static void render(DrawContext ctx, RenderTickCounter tickCounter) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;
		TextRenderer renderer = client.textRenderer;
		BlockPos pos = player.getBlockPos();
		PlayerInventory inv = client.player.getInventory();
		boolean hasCompass = inv.contains(is -> {
			return (is.getItem().toString().endsWith("compass") || is.getItem() instanceof CompassItem);
		});
		float degrees = MathHelper.wrapDegrees(player.getYaw());
		if (degrees < 0) {
			degrees += 360;
		}
		int facing = Math.round(degrees / 45);
		List<String> direction = Arrays.asList("S", "SW", "W", "NW", "N", "NE", "E", "SE", "S");
		if (hasCompass) {
			ctx.drawText(renderer, String.format("%s %s", direction.get(facing), pos.toShortString()), 10, 200,
					0xFFFFFFFF, false);
		}
	}
}