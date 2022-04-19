package me.thesilverecho.zeropoint.impl.module.render;

import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.impl.render.ConfigScreen2;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Modern Hotbar",  keyBinding = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class MainGuiHud extends BaseModule
{

	@Override
	protected void toggle()
	{
		MC.setScreen(new ConfigScreen2());
	}

	@Override
	protected void runToggleActions()
	{}
}
