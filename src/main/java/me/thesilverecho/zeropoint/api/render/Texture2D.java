package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12C.GL_UNPACK_IMAGE_HEIGHT;
import static org.lwjgl.opengl.GL12C.GL_UNPACK_SKIP_IMAGES;
import static org.lwjgl.opengl.GL45.glTextureParameteri;


public class Texture2D
{
	private int textureId = -1;

	public void bindTexture()
	{
		if (!RenderSystem.isOnRenderThreadOrInit())
			RenderSystem.recordRenderCall(() -> GlStateManager._bindTexture(this.getID()));
		else
			GlStateManager._bindTexture(this.getID());
	}

	public Texture2D(int width, int height, ByteBuffer buffer, Format format)
	{
		bindTexture();
		glPixelStorei(GL_UNPACK_SWAP_BYTES, GL_FALSE);
		glPixelStorei(GL_UNPACK_LSB_FIRST, GL_FALSE);
		glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
		glPixelStorei(GL_UNPACK_IMAGE_HEIGHT, 0);
		glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
		glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		buffer.rewind();
		glTexImage2D(GL_TEXTURE_2D, 0, format.toOpenGL(), width, height, 0, format.toOpenGL(), GL_UNSIGNED_BYTE, buffer);
	}


	public void setWrap(int wrapS, int wrapT)
	{
		final int texture = getID();
		glTextureParameteri(texture, GL_TEXTURE_WRAP_S, wrapS);
		glTextureParameteri(texture, GL_TEXTURE_WRAP_T, wrapT);
	}

	public void setFilter(int minFilter, int magFilter)
	{
		final int texture = getID();
		glTextureParameteri(texture, GL_TEXTURE_MIN_FILTER, minFilter);
		glTextureParameteri(texture, GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public int getID()
	{
		if (this.textureId == -1)
			this.textureId = TextureUtil.generateTextureId();

		return this.textureId;
	}

	public enum Format
	{
		A,
		RGB,
		RGBA;

		public int toOpenGL()
		{
			return switch (this)
					{
						case A -> GL_RED;
						case RGB -> GL_RGB;
						case RGBA -> GL_RGBA;
					};
		}
	}
}