package me.thesilverecho.zeropoint.api.uiv2;

import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Component
{
	private float offsetX, offsetY;

	protected float x, y, w, h;

	private APIColour background = APIColour.decode("#2b2b2b").setAlpha(20);

	public Component(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}


	public Component setOffsetX(float offsetX)
	{
		this.offsetX = offsetX;
		return this;
	}

	public Component setOffsetY(float offsetY)
	{
		this.offsetY = offsetY;
		return this;
	}

	public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

	public APIColour getBackground()
	{
		return background;
	}

	public Component setBackground(APIColour background)
	{
		this.background = background;
		return this;
	}

	public void repaint() {}

}
