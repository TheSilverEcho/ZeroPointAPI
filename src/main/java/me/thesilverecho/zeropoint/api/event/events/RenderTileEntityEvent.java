package me.thesilverecho.zeropoint.api.event.events;

import me.thesilverecho.zeropoint.api.event.BaseEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderTileEntityEvent
{
	public record renderTile(BlockEntityRenderer<BlockEntity> renderer, BlockEntity blockEntity, float tickDelta,
	                         MatrixStack matrices, VertexConsumerProvider vertexConsumers,
	                         CallbackInfo ci) implements BaseEvent
	{
	}
	public record startRenderTile() implements BaseEvent
	{
	}
	public record endRenderTile() implements BaseEvent
	{
	}

}
