package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.util.APIColour;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@ClientModule(name = "Direction hud")
public class Compass extends BaseModule
{
	private int direction;

	@EventListener
	public void renderEvent(Render2dEvent.Pre e)
	{
		final MatrixStack matrixStack = e.matrixStack();
		final float scaledHeight = e.scaledHeight() / 2;
		final float scaledWidth = e.scaledWidth() / 2;

		final int textureFromLocation = RenderUtilV2.getTextureFromLocation(new Identifier(ZeroPointClient.MOD_ID, "textures/zero-point_background_start.png"));
		RenderUtilV2.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureFromLocation);
//		shader.setShaderUniform("u_Radius", new Vec2f(0, 0));
		final APIColour x = APIColour.WHITE;

		RenderUtilV2.quadTexture(matrixStack.peek().getPositionMatrix(), 3, 3, 100, 100, direction / 300f, 0, 1, 1, x, x, x, x);

//		RenderUtilV2.rectangleTexture(matrixStack,0,0,100,100,0,textureFromLocation,x);

	}


	@EventListener
	public void updateEvent(TickEvent.EndTickEvent e)
	{
		final ClientPlayerEntity player = e.minecraftClient().player;
		if (player != null)
			this.direction = MathHelper.floor(player.headYaw * 256.0f / 360.0f + 0.5) & 0xFF;
	}

}
