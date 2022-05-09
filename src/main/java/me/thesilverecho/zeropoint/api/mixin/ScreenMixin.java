package me.thesilverecho.zeropoint.api.mixin;

import me.thesilverecho.zeropoint.api.event.EventManager;
import me.thesilverecho.zeropoint.api.event.events.render.ScreenEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin
{
	@Shadow public int width;

	@Shadow public int height;

	@Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
	protected void preTooltipRenderItem(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci)
	{
		ScreenEvent.TooltipEvent.setStack(stack);
	}

	@Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("RETURN"))
	protected void postTooltipItemRender(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci)
	{
		ScreenEvent.TooltipEvent.setStack(ItemStack.EMPTY);
	}


	@Inject(method = "renderTooltipFromComponents", at = @At("HEAD"), cancellable = true)
	protected void tooltipRender(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci)
	{
		if (!components.isEmpty())
			EventManager.call(new ScreenEvent.TooltipEvent(matrices, components, x, y, this.width, this.height, ci));
	}

	@Inject(method = "render", at = @At("HEAD"))
	protected void preScreenRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		EventManager.call(new ScreenEvent.PRE((Screen) (Object) this, matrices, mouseX, mouseY, delta));
	}

	@Inject(method = "render", at = @At("RETURN"))
	protected void postScreenRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
	{
		EventManager.call(new ScreenEvent.POST((Screen) (Object) this, matrices, mouseX, mouseY, delta));
	}

	@Inject(method = "close", at = @At("HEAD"),cancellable = true)
	protected void closeScreen(CallbackInfo ci)
	{
		EventManager.call(new ScreenEvent.CLOSE_SCREEN((Screen) (Object) this, ci));
	}

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
	protected void openScreen(CallbackInfo ci)
	{
		EventManager.call(new ScreenEvent.OPEN_SCREEN((Screen) (Object) this, ci));
	}

}
