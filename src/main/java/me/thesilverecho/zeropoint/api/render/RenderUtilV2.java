package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.APIColour;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vector4f;

import java.util.ArrayList;


public class RenderUtilV2
{
	public static float zIndex;
	public static Shader shader;
	private static int textureId = -1;

	private static APIColour.ColourQuad COLOUR_QUAD_COLOUR_HOLDER = new APIColour.ColourQuad(APIColour.WHITE);

	public static int getTextureFromLocation(Identifier identifier)
	{
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		final AbstractTexture texture = textureManager.getTexture(identifier);
		return texture.getGlId();
	}

	public static void setQuadColourHolder(APIColour newColour)
	{
		COLOUR_QUAD_COLOUR_HOLDER.setTopLeft(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setTopRight(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setBottomRight(newColour);
		COLOUR_QUAD_COLOUR_HOLDER.setBottomLeft(newColour);
	}

	public static void setQuadColourHolder(APIColour.ColourQuad newColour)
	{
		COLOUR_QUAD_COLOUR_HOLDER = newColour;
	}

	public static void setShader(Shader shader)
	{
		RenderUtilV2.shader = shader;
	}

	public static void setTextureId(int textureId)
	{
		RenderUtilV2.textureId = textureId;
	}

	public static float getZIndex()
	{
		return zIndex;
	}

	public static void setZIndex(float zIndex)
	{
		RenderUtilV2.zIndex = zIndex;
	}


	public static void setShaderUniform(String var, Object value)
	{
		shader.setShaderUniform(var, value);
	}


	public static void applyTextureToShader()
	{
		GLWrapper.activateTexture(0, textureId);
		shader.setShaderUniform("Sampler0", 0);
	}

	/*-----------------------2D QUAD DRAW---------------------*/
	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, APIColour color)
	{
		quad(matrixStack, x, y, x + width, y + height, color, color, color, color);
	}

	public static void line(MatrixStack matrixStack, float x, float y, float width, float height, APIColour color)
	{
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}

	public static void line(MatrixStack matrixStack, float x, float y, float width, float height, float radius, APIColour APIColour)
	{
		setShader(APIShaders.RECTANGLE_SHADER.getShader());
		shader.setShaderUniform("Radius", new Vec2f(radius, radius));
		line(matrixStack, x, y, width, height, APIColour);
	}

	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, APIColour cTopLeft, APIColour cTopRight, APIColour cBottomRight, APIColour cBottomLeft)
	{
		GLWrapper.enableGL2D();

		if (x > width)
		{
			float temp = x;
			x = width;
			width = temp;

		}
		if (y > height)
		{
			float temp = height;
			height = y;
			y = temp;
		}
		final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.getRed(), cTopLeft.getGreen(), cTopLeft.getBlue(), cTopLeft.getAlpha()).next();
		builder.vertex(matrix4f, x, height, zIndex).color(cBottomLeft.getRed(), cBottomLeft.getGreen(), cBottomLeft.getBlue(), cBottomLeft.getAlpha()).next();
		builder.vertex(matrix4f, width, height, zIndex).color(cBottomRight.getRed(), cBottomRight.getGreen(), cBottomRight.getBlue(), cBottomRight.getAlpha()).next();
		builder.vertex(matrix4f, width, y, zIndex).color(cTopRight.getRed(), cTopRight.getGreen(), cTopRight.getBlue(), cTopRight.getAlpha()).next();

	/*	builder.vertex(matrix4f, x, height, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).next();
		builder.vertex(matrix4f, width, height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).next();
		builder.vertex(matrix4f, width, y, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).next();
		builder.vertex(matrix4f, x, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).next();*/

		builder.end();
		shader.bind();
		BufferRenderer.postDraw(builder);
		shader.unBind();
		GLWrapper.disableGL2D();
	}

	public static void rectangle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, APIColour APIColour)
	{
		setShader(APIShaders.RECTANGLE_SHADER.getShader());
		shader.setShaderUniform("Radius", new Vec2f(radius, radius));
		quad(matrixStack, x, y, width, height, APIColour);
	}

	public static void trail(MatrixStack matrixStack, float x, float y, float width, float height, APIColour APIColour)
	{
//		setShader(APIShaders.TRAIL_SHADER.getShader());
		//TODO: add time control unit(Game time only works when in world).
		shader.setShaderUniform("Time", RenderSystem.getShaderGameTime());
		quadTexture(matrixStack, x, y, width, height, APIColour);
//		FontRenderer.renderText(APIFonts.REGULAR.getFont(), 0.5f, matrixStack, RenderSystem.getShaderGameTime() + "", x, y);
	}


	public static void rectangle(MatrixStack matrixStack, float x, float y, float width, float height, APIColour APIColour)
	{
		RenderUtilV2.rectangle(matrixStack, x, y, width, height, 0, APIColour);
	}

	public static void roundRect(MatrixStack matrixStack, float x, float y, float width, float height, float radius, APIColour APIColour)
	{

		RenderUtilV2.roundRect(matrixStack, x, y, width, height, x + radius, y + radius, x + width - radius, y + height - radius, radius, APIColour);
	}

	public static void roundRect(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_SHADER.getShader());
		roundRectanglePass(matrixStack, x, y, width, height, innerX, innerY, innerWidth, innerHeight, radius, APIColour);
	}

	public static void roundRectLine(MatrixStack matrixStack, float x, float y, float width, float height, float radius, float thickness, APIColour APIColour)
	{
		RenderUtilV2.roundRectLine(matrixStack, x, y, width, height, x + radius + thickness, y + radius + thickness, x + width - radius - thickness, y + height - radius - thickness, radius, thickness, APIColour);
	}

	public static void roundRectLineAdjust(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, float thickness, APIColour APIColour)
	{
		roundRectLine(matrixStack, x, y, width, height, x + innerX, y + innerY, x + width + innerWidth, y + height + innerHeight, radius, thickness, APIColour);
	}

	public static void roundRectLine(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, float thickness, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_LINE_SHADER.getShader());
		shader.setShaderUniform("Thickness", thickness);
		roundRectanglePass(matrixStack, x, y, width, height, innerX, innerY, innerWidth, innerHeight, radius, APIColour);
	}

	public static void roundRectAdjust(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_SHADER.getShader());
		roundRectanglePass(matrixStack, x, y, width, height, x + innerX, y + innerY, x + width + innerWidth, y + height + innerHeight, radius, APIColour);
	}

	public static void roundRectanglePass(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, APIColour APIColour)
	{
		shader.setShaderUniform("Radius", new Vec2f(radius, 1));
		shader.setShaderUniform("Rectangle", new Vector4f(innerX, innerY, innerWidth, innerHeight));
		RenderUtilV2.quad(matrixStack, x, y, width, height, APIColour);
	}


	public static void circle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, APIColour APIColour)
	{
		circle(matrixStack, x, y, width, height, radius, 1, APIColour);
	}

	public static void circle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int feather, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.CIRCLE_SHADER.getShader());
		shader.setShaderUniform("Radius", new Vec2f(radius, Math.min(radius, feather)));
		shader.setShaderUniform("CenterPosition", new Vec2f(x + height / 2, y + width / 2));
		RenderUtilV2.quad(matrixStack, x, y, width, height, APIColour);
	}

	public static void colourPicker(MatrixStack matrixStack, float x, float y, float width, float height, float hue, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.COLOUR_PICKER.getShader());
		shader.setShaderUniform("Hue", hue);
		RenderUtilV2.quad(matrixStack, x, y, width, height, APIColour);
//		Font font = new Font();
//		font.createGlyphVector()
	}


	public static void quadTexture(Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1)
	{
		GLWrapper.enableGL2D();
	/*	if (textureId == -1)
			return;*/
		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		final APIColour tL = COLOUR_QUAD_COLOUR_HOLDER.getTopLeft();
		final APIColour bL = COLOUR_QUAD_COLOUR_HOLDER.getBottomLeft();
		final APIColour bR = COLOUR_QUAD_COLOUR_HOLDER.getTopRight();
		final APIColour tR = COLOUR_QUAD_COLOUR_HOLDER.getBottomRight();
		builder.vertex(matrix4f, x, y, zIndex).color(tL.getRed(), tL.getGreen(), tL.getBlue(), tL.getAlpha()).texture(u0, v0).next();
		builder.vertex(matrix4f, x, height, zIndex).color(bL.getRed(), bL.getGreen(), bL.getBlue(), bL.getAlpha()).texture(u0, v1).next();
		builder.vertex(matrix4f, width, height, zIndex).color(bR.getRed(), bR.getGreen(), bR.getBlue(), bR.getAlpha()).texture(u1, v1).next();
		builder.vertex(matrix4f, width, y, zIndex).color(tR.getRed(), tR.getGreen(), tR.getBlue(), tR.getAlpha()).texture(u1, v0).next();
		builder.end();
		applyTextureToShader();
		shader.bind();
		BufferRenderer.postDraw(builder);
		GLWrapper.activateTexture(0, 0);
		shader.unBind();
		GLWrapper.disableGL2D();
	}

	public static void quadTexture(Matrix4f matrix4f, float x, float y, float width, float height, float u0, float v0, float u1, float v1, APIColour cTopLeft, APIColour cTopRight, APIColour cBottomRight, APIColour cBottomLeft)
	{
		GLWrapper.enableGL2D();
	/*	if (textureId == -1)
			return;*/
		final BufferBuilder builder = RenderSystem.renderThreadTesselator().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		final APIColour tL = COLOUR_QUAD_COLOUR_HOLDER.getTopLeft();
		final APIColour bL = COLOUR_QUAD_COLOUR_HOLDER.getBottomLeft();
		final APIColour bR = COLOUR_QUAD_COLOUR_HOLDER.getTopRight();
		final APIColour tR = COLOUR_QUAD_COLOUR_HOLDER.getBottomRight();
		builder.vertex(matrix4f, x, y, zIndex).color(tL.getRed(), tL.getGreen(), tL.getBlue(), tL.getAlpha()).texture(u0, v0).next();
		builder.vertex(matrix4f, x, height, zIndex).color(bL.getRed(), bL.getGreen(), bL.getBlue(), bL.getAlpha()).texture(u0, v1).next();
		builder.vertex(matrix4f, width, height, zIndex).color(bR.getRed(), bR.getGreen(), bR.getBlue(), bR.getAlpha()).texture(u1, v1).next();
		builder.vertex(matrix4f, width, y, zIndex).color(tR.getRed(), tR.getGreen(), tR.getBlue(), tR.getAlpha()).texture(u1, v0).next();
		builder.end();
		applyTextureToShader();
		shader.bind();
		BufferRenderer.postDraw(builder);
		GLWrapper.activateTexture(0, 0);
		shader.unBind();
		GLWrapper.disableGL2D();
	}

	//	TODO: change this so that we dont need a width and height, using midPoint for the max/min y of the quad
	public static void bezier(MatrixStack matrixStack, float x, float y, Vec2f point1, Vec2f midPoint, Vec2f point2, float thickness, APIColour APIColour)
	{
		final Vec2f offset = new Vec2f(x, y);
		bezier(matrixStack, x, y, point2.add(offset).x + thickness * 10, midPoint.add(offset).y + thickness * 10, point1.add(offset), midPoint.add(offset), point2.add(offset), thickness, APIColour);
	}

	public static void bezier(MatrixStack matrixStack, float x, float y, float width, float height, Vec2f point1, Vec2f midPoint, Vec2f point2, float thickness, APIColour APIColour)
	{
		setShader(APIShaders.BEZIER_SHADER.getShader());
		shader.setShaderUniform("Point1", point1);
		shader.setShaderUniform("Point2", midPoint);
		shader.setShaderUniform("Point3", point2);
		shader.setShaderUniform("Thickness", thickness);
		shader.setShaderUniform("SmoothR", 0.6f);
		quad(matrixStack, x, y, width, height, APIColour);
	}

	public static void multiBezier(MatrixStack matrixStack, float x, float y, float width, float height, ArrayList<Vec2f> points, float thickness, APIColour APIColour)
	{

		for (int i = 1; i < points.size() - 2; i++)
		{
			final Vec2f point1 = points.get(i);
			final Vec2f point2 = points.get(i + 1);
			final Vec2f point3 = point1.add(point2).multiply(0.5f);
			bezier(matrixStack, x, y, width, height, point1, point2, point3, thickness, APIColour);
			if (i == 1)
			{
				bezier(matrixStack, x, y, width, height, points.get(0), point2, point3, thickness, APIColour);
			}
			if (i == points.size() - 2)
			{
				bezier(matrixStack, x, y, width, height, point1, point2, points.get(points.size() - 1), thickness, APIColour);
			}
		}

	}


	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, APIColour APIColour)
	{
		quadTexture(matrixStack, x, y, x + width, y + height, 0, 0, 1, 1, APIColour, APIColour, APIColour, APIColour);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, APIColour colour)
	{
		quadTexture(matrixStack, x, y, width, height, u0, v0, u1, v1, colour, colour, colour, colour);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, APIColour cTopLeft, APIColour cTopRight, APIColour cBottomRight, APIColour cBottomLeft)
	{
		quadTexture(matrixStack.peek().getPositionMatrix(), x, y, width, height, u0, v0, u1, v1, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
	}

	public static void quadTexture1(Matrix4f m, float x, float y, float width, float height, float u0, float v0, float u1, float v1, APIColour cTopLeft, APIColour cTopRight, APIColour cBottomRight, APIColour cBottomLeft)
	{
		quadTexture(m, x, y, width, height, u0, v0, u1, v1, cTopLeft, cTopRight, cBottomRight, cBottomLeft);
	}

	public static void rectangleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, APIColour APIColour)
	{
		setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(radius, 0));
		quadTexture(matrixStack, x, y, width, height, APIColour);
	}

	public static void rectangleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, int textureLoc, APIColour APIColour)
	{
		setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(0, 0));
		quadTexture(matrixStack, x, y, x + width, y + height, u0, v0, u1, v1, APIColour);
	}

	public static void rectangleBokeh(MatrixStack matrixStack, float x, float y, float width, float height, float size, int textureLoc, APIColour APIColour)
	{
		setShader(APIShaders.BOKEH_TEXTURE_SHADER.getShader());
		setTextureId(textureLoc);
		shader.setShaderUniform("Size", size);
		quadTexture(matrixStack, x, y, width, height, APIColour);
	}

	public static void rectangleTexture(MatrixStack matrixStack, float x, float y, float width, float height, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.rectangleTexture(matrixStack, x, y, width, height, 0, textureLoc, APIColour);
	}

	public static void roundRectTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.roundRectTexture(matrixStack, x, y, width, height, x + radius, y + radius, x + width - radius, y + height - radius, radius, textureLoc, APIColour);
	}

	public static void roundRectTexture(MatrixStack matrixStack, float x, float y, float width, float height, float innerX, float innerY, float innerWidth, float innerHeight, float radius, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(radius, 1));
		shader.setShaderUniform("Rectangle", new Vector4f(innerX, innerY, innerWidth, innerHeight));
		RenderUtilV2.quadTexture(matrixStack, x, y, width, height, APIColour);
	}

	public static void roundRectTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, float innerX, float innerY, float innerWidth, float innerHeight, float radius, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.ROUND_RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(radius, 1));
		shader.setShaderUniform("Rectangle", new Vector4f(innerX, innerY, innerWidth, innerHeight));
		RenderUtilV2.quadTexture(matrixStack, x, y, width, height, u0, v0, u1, v1, APIColour);
	}


	public static void circleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int textureLoc, APIColour APIColour)
	{
		circleTexture(matrixStack, x, y, width, height, radius, 1, textureLoc, APIColour);
	}

	public static void circleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int feather, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.CIRCLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(radius, Math.min(radius, feather)));
		shader.setShaderUniform("CenterPosition", new Vec2f(x + height / 2, y + width / 2));
		RenderUtilV2.quadTexture(matrixStack, x, y, width, height, APIColour);
	}

	public static void circleTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, float radius, int textureLoc, APIColour APIColour)
	{
		RenderUtilV2.setShader(APIShaders.CIRCLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(textureLoc);
		shader.setShaderUniform("Radius", new Vec2f(radius, Math.min(radius, 1f)));
		shader.setShaderUniform("CenterPosition", new Vec2f(x + height / 2, y + width / 2));
		RenderUtilV2.quadTexture(matrixStack, x, y, x + width, y + height, u0, v0, u1, v1, APIColour, APIColour, APIColour, APIColour);
	}

	public static void postProcessRect(float width, float height)
	{
		postProcessRect(width, height, 0, 0, 1, 1);
	}

	public static void postProcessRect(float width, float height, float u0, float v0, float u1, float v1)
	{
//		GLWrapper.enableGL2D();
		GlStateManager._disableDepthTest();
		GlStateManager._depthMask(false);

		final Matrix4f matrix = RenderSystem.getProjectionMatrix();

		final Matrix4f m = Matrix4f.projectionMatrix(width, -height, 1000.0F, 3000.0F);
		RenderSystem.setProjectionMatrix(m);

		final BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0D, height, 0.0D).texture(u0, v0).next();
		bufferBuilder.vertex(width, height, 0.0D).texture(u1, v0).next();
		bufferBuilder.vertex(width, 0.0D, 0.0D).texture(u1, v1).next();
		bufferBuilder.vertex(0.0D, 0.0D, 0.0D).texture(u0, v1).next();
		bufferBuilder.end();
		shader.setShaderUniform("TextureSize", new Vec2f(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight));
		applyTextureToShader();
//      TODO: when a framebuffer uses a mipmap, the glGenerateMipmap needs to be called so that the new frame data will be used in shader.
//	    glGenerateMipmap(GL_TEXTURE_2D);
		shader.bind();
		BufferRenderer.postDraw(bufferBuilder);
		shader.unBind();

		GlStateManager._depthMask(true);
//		GLWrapper.disableGL2D();
//		RenderSystem.setProjectionMatrix(matrix);
	}


	public static double getAnimationState(double animation, double finalState, double speed)
	{
		float add = (float) (/*0.055*/speed);
		return animation < finalState ? (Math.min(animation + (double) add, finalState)) : (Math.max(animation - (double) add, finalState));
	}

	public static void rectangleTexturePos(MatrixStack matrixStack, float x, float y, float width, float height, int textureLoc)
	{
		setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		shader.setShaderUniform("Radius", new Vec2f(0, 0));
		setTextureId(textureLoc);
		quadTexture(matrixStack, x, y, width, height, 1, 0.5f, 0, 1, APIColour.WHITE, APIColour.WHITE, APIColour.WHITE, APIColour.WHITE);
	}
}
