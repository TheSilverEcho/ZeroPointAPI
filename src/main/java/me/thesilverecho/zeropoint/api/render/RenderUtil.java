/*
package me.thesilverecho.zeropoint.api.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.shader.Shader;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil
{
	public static float zIndex;
	public static Shader shader;
	private static int id = -1;
	private static Consumer<Shader> postShaderBind;

	*/
/*-----------------------2D QUAD DRAW---------------------*//*


	public static void rect(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		setPostShaderBind(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(1, 1));
			shader.setArgument("u_InnerRect", new Vector4f(x + 1, y + 1, width - 1, height - 1));
		});
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}

	public static void roundRect(MatrixStack matrixStack, float x, float y, float width, float height, float radius, ColourHolder color)
	{
		setShader(APIShaders.ROUND_RECTANGLE_SHADER.getShader());
		setPostShaderBind(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, 1));
			shader.setArgument("u_InnerRect", new Vector4f(x + radius, y + radius, width - radius, height - radius));
		});
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}

	public static void drawCircle(MatrixStack matrixStack, float x, float y, float width, float height, float radius, ColourHolder color)
	{
		setShader(APIShaders.CIRCLE_SHADER.getShader());
		setPostShaderBind(shader ->
		{
			shader.setArgument("u_Radius", new Vec2f(radius, 1));
			shader.setArgument("u_InnerRect", new Vector4f(x + radius, y + radius, width - radius, height - radius));
		});
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}


	public static void quad(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		quad(matrixStack, x, y, width, height, color, color, color, color);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u, float v, float u1, float v1, ColourHolder color)
	{
		quadTexture(matrixStack, x, y, width, height, u, v, u1, v1, color, color, color, color);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, ColourHolder color)
	{
		quadTexture(matrixStack, x, y, width, height, 0, 0, 1, 1, color, color, color, color);
	}

	public static void quadTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u0, float v0, float u1, float v1, ColourHolder cTopLeft, ColourHolder cTopRight, ColourHolder cBottomRight, ColourHolder cBottomLeft)
	{
		enableGL2D();
		final Matrix4f matrix4f = matrixStack.peek().getModel();
		final BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		builder.vertex(matrix4f, x, y, zIndex).color(cTopLeft.red(), cTopLeft.green(), cTopLeft.blue(), cTopLeft.alpha()).texture(u0, v0).next();
		builder.vertex(matrix4f, x, height, zIndex).color(cBottomLeft.red(), cBottomLeft.green(), cBottomLeft.blue(), cBottomLeft.alpha()).texture(u0, v1).next();
		builder.vertex(matrix4f, width, height, zIndex).color(cBottomRight.red(), cBottomRight.green(), cBottomRight.blue(), cBottomRight.alpha()).texture(u1, v1).next();
		builder.vertex(matrix4f, width, y, zIndex).color(cTopRight.red(), cTopRight.green(), cTopRight.blue(), cTopRight.alpha()).texture(u1, v0).next();
		builder.end();
		shader.bind();
		if (postShaderBind != null)
			postShaderBind.accept(shader);
		if (id != -1)
		{
			GlStateManager._bindTexture(id);
			GL20.glUniform1i(2, 0);
			RenderSystem.activeTexture(GL43.GL_TEXTURE0);
			id = -1;
		}
		BufferRenderer.postDraw(builder);
		shader.unBind();
		disableGL2D();

	}

	public static void postProcessText(int id)
	{
		enableGL2D();
		shader.bind();
		final int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
		final int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
		final BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		builder.vertex(0, 0, zIndex).color(0, 100, 0, 255).texture(0, 1).next();
		builder.vertex(0, height, zIndex).color(0, 100, 0, 255).texture(0, 0).next();
		builder.vertex(width, height, zIndex).color(0, 100, 0, 255).texture(1, 0).next();
		builder.vertex(width, 0, zIndex).color(0, 100, 0, 255).texture(1, 1).next();
		builder.end();
		if (postShaderBind != null)
			postShaderBind.accept(shader);
		GlStateManager._bindTexture(id);
		GL20.glUniform1i(2, 0);
		RenderSystem.activeTexture(GL43.GL_TEXTURE0);
		BufferRenderer.postDraw(builder);
		shader.unBind();
		disableGL2D();

	}

	*/
/*-----------------------3D DRAW---------------------*//*

	public static void drawTextInWorld(BlockPos pos, String text)
	{

		final MatrixStack matrices = getMatrixFromPos(pos);

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

//		matrices.translate(offX, offY, 0);
		matrices.scale(-0.025f, -0.025f, 1);

		int halfWidth = MinecraftClient.getInstance().textRenderer.getWidth(text) / 2;
		matrices.push();
		matrices.translate(1, 1, 0);
		APIFonts.REGULAR.getFont().render(matrices, text, -halfWidth, 0);
		matrices.pop();
//		mc.textRenderer.draw(text, -halfWidth, 0f, -1, false, matrices.peek().getModel(), immediate, true, 0, 0xf000f0);
//		immediate.draw();

		RenderSystem.disableBlend();


		final CustomFont font = APIFonts.REGULAR.getFont();
		font.setFontScale(0.5f);
		font.getWidth(text);
//		font.render()
	}


	public static void setPostShaderBind(Consumer<Shader> postShaderBind)
	{
		RenderUtil.postShaderBind = postShaderBind;
	}


	public static void setShaderTexture(Identifier identifier)
	{
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		AbstractTexture abstractTexture = textureManager.getTexture(identifier);
		setShaderTexture(abstractTexture.getGlId());
	}


	public static void setShaderTexture(int id)
	{
		RenderUtil.id = id;
	}

	public static void setShader(Shader shader)
	{
		RenderUtil.shader = shader;
	}

	public static void enableGL2D()
	{
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(true);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
	}

	public static void disableGL2D()
	{
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
	}

	public static MatrixStack getMatrixFromPos(BlockPos position)
	{
		MatrixStack matrices = new MatrixStack();
		final int x = position.getX();
		final int y = position.getY();
		final int z = position.getZ();
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));

		matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

		return matrices;
	}


	public static float getZIndex()
	{
		return zIndex;
	}

	public static void setZIndex(float zIndex)
	{
		RenderUtil.zIndex = zIndex;
	}
}
*/
