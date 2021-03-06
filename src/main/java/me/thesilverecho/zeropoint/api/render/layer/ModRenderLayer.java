package me.thesilverecho.zeropoint.api.render.layer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.impl.ZeroPointClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class ModRenderLayer extends RenderLayer
{

	public static final ArrayList<RenderLayer> ALL_LAYERS = new ArrayList<>();
	public static final RenderLayer POT_OVERLAY;
	public static final RenderLayer BLUR = of("test:glint_direct", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256,
			ModMultiPhaseParameters.builder()
			                       .shader(RenderPhase.GLINT_SHADER)
			                       .writeMaskState(COLOR_MASK)
			                       .texture(new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false))
			                       .cull(DISABLE_CULLING)
			                       .depthTest(/*RenderPhase.EQUAL_DEPTH_TEST*/RenderPhase.ALWAYS_DEPTH_TEST)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .texturing(GLINT_TEXTURING)
//			                       .target(new Target("blur_out", () -> BlurBackground.blurMask.bind(), () -> BlurBackground.blurMask.unbind()))
                                   .build(false));

	public static final RenderLayer CHARMING = of("test:2", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256,
			ModMultiPhaseParameters.builder()
			                       .shader(RenderPhase.COLOR_SHADER)
			                       .cull(DISABLE_CULLING)
			                       .layering(POLYGON_OFFSET_LAYERING)
			                       .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			                       .depthTest(/*RenderPhase.ALWAYS_DEPTH_TEST*/RenderPhase.EQUAL_DEPTH_TEST)
//			                       .target(new Target("blur_out", () -> /*BlurBackground.getBlurMask()*/BlockEntityESP.getFramebuffer().bind(), () -> /*BlurBackground.getBlurMask()*/BlockEntityESP.getFramebuffer().unbind()))
			                       .build(false));

	public static final RenderLayer DG = of(ZeroPointClient.MOD_ID + ":blur",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			ModMultiPhaseParameters.builder()
			                       .shader(SOLID_SHADER)
			                       .writeMaskState(COLOR_MASK)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(EQUAL_DEPTH_TEST)
			                       .transparency(new Transparency("default", () ->
			                       {
				                       RenderSystem.enableBlend();
				                       RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
			                       }, () ->
			                       {
				                       RenderSystem.disableBlend();
				                       RenderSystem.defaultBlendFunc();
			                       }))
			                       /* .target(new Target("blur_out", () ->
									{
										BlurBackground.blurMask.bind();
									}, () ->
									{
										BlurBackground.blurMask.unbind();
									}))*/
			                       .build(false));


	public static final RenderLayer COSMIC_RENDER_TYPE = of(ZeroPointClient.MOD_ID + ":cosmic",
			VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
			VertexFormat.DrawMode.QUADS,
			256,
			true,
			false,
			ModMultiPhaseParameters.builder()
			                       .modShader(new ModRenderPhase.ModShader(APIShaders.FONT_MASK_TEXTURE.getShader()))
//			                       .texture( MultiTextureStateShard.builder().add(BLOCK_ITEM_LOC, false, false).add(new ResourceLocation(Avaritia.MOD_ID, "textures/shader/cosmic_sheet.png")/*TheEndPortalRenderer.END_PORTAL_LOCATION*/, false, false).build())
                                   .transparency(TRANSLUCENT_TRANSPARENCY)
                                   .cull(DISABLE_CULLING)
//                                   .layering(VIEW_OFFSET_Z_LAYERING)
                                   .lightmap(ENABLE_LIGHTMAP)
                                   .build(false));


	public static final RenderLayer TEST = of(ZeroPointClient.MOD_ID + ":test",
			VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.QUADS,
			256,
			true,
			false,
			ModMultiPhaseParameters.builder()
			                       .layering(VIEW_OFFSET_Z_LAYERING)
			                       .transparency(TRANSLUCENT_TRANSPARENCY)
			                       .cull(DISABLE_CULLING)
			                       .depthTest(LEQUAL_DEPTH_TEST)
			                       .lightmap(DISABLE_LIGHTMAP)
			                       .writeMaskState(RenderPhase.ALL_MASK)
			                       .build(false));


	static
	{
		final Texturing glintTexturing = new RenderPhase.Texturing("glint_texturing", () ->
		{
//			RenderSystem.setShaderColor(255, 0, 0, 255);
			long l = Util.getMeasuringTimeMs() * 8L;
			float f = (float) (l % 110000L) / 110000.0F;
			float g = (float) (l % 30000L) / 30000.0F;
			Matrix4f matrix4f = Matrix4f.translate(-f, -g, 0.0F);
			matrix4f.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(10.0F));
			matrix4f.multiply(Matrix4f.scale(8, 8, 8));
			RenderSystem.setTextureMatrix(matrix4f);
		}, RenderSystem::resetTextureMatrix);
		POT_OVERLAY = basic("glint_direct", ModMultiPhaseParameters.builder().shader(DIRECT_GLINT_SHADER).texture(new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(ENABLE_CULLING).depthTest(LEQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(glintTexturing).build(false));

		ALL_LAYERS.add(DG);
		ALL_LAYERS.add(BLUR);
		ALL_LAYERS.add(CHARMING);


	}

	public static final RenderLayer RENDER_LAYER = of("sword_test", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ModMultiPhaseParameters.builder().shader(POSITION_TEXTURE_SHADER).texture(new ModRenderPhase.Texture(new Identifier(ZeroPointClient.MOD_ID, "textures/bg.png"), true, false)).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).build(false));
	public static final RenderLayer POT_OVERLAY2 = basic("test", ModMultiPhaseParameters.builder().modShader(new ModRenderPhase.ModShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader(), shader ->
			new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false).getId().ifPresent(identifier ->
			{
//				RenderUtilV2.setTextureFromLocation(identifier);
				shader.setShaderUniform("u_Radius", new Vec2f(0, 0));
				RenderUtilV2.applyTextureToShader();
			}))).texture(new ModRenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).build(false));

	public static ModRenderLayer.ModMultiPhase basic(String name, ModMultiPhaseParameters parameters)
	{
		final ModMultiPhase of = of(name, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, parameters);
		ALL_LAYERS.add(of);
		return of;
	}

	public ModRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, ModRenderLayer.ModMultiPhaseParameters phaseData)
	{
		return of(name, vertexFormat, drawMode, expectedBufferSize, false, false, phaseData);
	}

	private static ModRenderLayer.ModMultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModRenderLayer.ModMultiPhaseParameters phases)
	{
		return new ModRenderLayer.ModMultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases);
	}

	private static RenderLayer.MultiPhaseParameters of(RenderPhase.Shader shader)
	{
		return RenderLayer.MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(shader).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).transparency(TRANSLUCENT_TRANSPARENCY).target(TRANSLUCENT_TARGET).build(true);
	}

	public static class ModMultiPhaseParameters
	{
		//		TODO: add support for api shaders
		public final ModRenderPhase.TextureBase texture;
		public final ModRenderPhase.Shader shader;

		public final ModRenderPhase.ModShader modShader;

		public final ModRenderPhase.Transparency transparency;
		public final ModRenderPhase.DepthTest depthTest;
		public final ModRenderPhase.Cull cull;
		public final ModRenderPhase.Lightmap lightmap;
		public final ModRenderPhase.Overlay overlay;
		public final ModRenderPhase.Layering layering;
		public final ModRenderPhase.Target target;
		public final ModRenderPhase.Texturing texturing;
		public final ModRenderPhase.WriteMaskState writeMaskState;
		public final ModRenderPhase.LineWidth lineWidth;
		public final OutlineMode outlineMode;
		public final ImmutableList<RenderPhase> phases;

		ModMultiPhaseParameters(ModRenderPhase.TextureBase texture, Shader shader, ModRenderPhase.ModShader modShader, Transparency transparency, DepthTest depthTest, Cull cull, Lightmap lightmap, Overlay overlay, Layering layering, Target target, Texturing texturing, WriteMaskState writeMaskState, LineWidth lineWidth, OutlineMode outlineMode)
		{
			this.texture = texture;
			this.shader = shader;
			this.modShader = modShader;
			this.transparency = transparency;
			this.depthTest = depthTest;
			this.cull = cull;
			this.lightmap = lightmap;
			this.overlay = overlay;
			this.layering = layering;
			this.target = target;
			this.texturing = texturing;
			this.writeMaskState = writeMaskState;
			this.lineWidth = lineWidth;
			this.outlineMode = outlineMode;
			this.phases = ImmutableList.of(this.texture, this.shader, this.modShader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth);
		}

		public String toString()
		{
			return "CompositeState[" + this.phases + ", outlineProperty=" + this.outlineMode + "]";
		}

		public static Builder builder()
		{
			return new Builder();
		}

		@Environment(EnvType.CLIENT)
		public static class Builder
		{
			private ModRenderPhase.TextureBase texture;
			private ModRenderPhase.Shader shader;
			private ModRenderPhase.ModShader modShader;
			private ModRenderPhase.Transparency transparency;
			private ModRenderPhase.DepthTest depthTest;
			private ModRenderPhase.Cull cull;
			private ModRenderPhase.Lightmap lightmap;
			private ModRenderPhase.Overlay overlay;
			private ModRenderPhase.Layering layering;
			private ModRenderPhase.Target target;
			private ModRenderPhase.Texturing texturing;
			private ModRenderPhase.WriteMaskState writeMaskState;
			private ModRenderPhase.LineWidth lineWidth;

			Builder()
			{
				this.texture = ModRenderPhase.NO_TEXTURE;
				this.shader = ModRenderPhase.NO_SHADER;
				this.modShader = ModRenderPhase.NO_MOD_SHADER;
				this.transparency = ModRenderPhase.NO_TRANSPARENCY;
				this.depthTest = ModRenderPhase.LEQUAL_DEPTH_TEST;
				this.cull = ModRenderPhase.ENABLE_CULLING;
				this.lightmap = ModRenderPhase.DISABLE_LIGHTMAP;
				this.overlay = ModRenderPhase.DISABLE_OVERLAY_COLOR;
				this.layering = ModRenderPhase.NO_LAYERING;
				this.target = ModRenderPhase.MAIN_TARGET;
				this.texturing = ModRenderPhase.DEFAULT_TEXTURING;
				this.writeMaskState = ModRenderPhase.ALL_MASK;
				this.lineWidth = ModRenderPhase.FULL_LINE_WIDTH;
			}

			public Builder texture(ModRenderPhase.TextureBase texture)
			{
				this.texture = texture;
				return this;
			}

			public Builder shader(ModRenderPhase.Shader shader)
			{
				this.shader = shader;
				return this;
			}

			public Builder modShader(ModRenderPhase.ModShader modShader)
			{
				this.modShader = modShader;
				return this;
			}


			public Builder transparency(ModRenderPhase.Transparency transparency)
			{
				this.transparency = transparency;
				return this;
			}

			public Builder depthTest(ModRenderPhase.DepthTest depthTest)
			{
				this.depthTest = depthTest;
				return this;
			}

			public Builder cull(ModRenderPhase.Cull cull)
			{
				this.cull = cull;
				return this;
			}

			public Builder lightmap(ModRenderPhase.Lightmap lightmap)
			{
				this.lightmap = lightmap;
				return this;
			}

			public Builder overlay(ModRenderPhase.Overlay overlay)
			{
				this.overlay = overlay;
				return this;
			}

			public Builder layering(ModRenderPhase.Layering layering)
			{
				this.layering = layering;
				return this;
			}

			public Builder target(ModRenderPhase.Target target)
			{
				this.target = target;
				return this;
			}

			public Builder texturing(ModRenderPhase.Texturing texturing)
			{
				this.texturing = texturing;
				return this;
			}

			public Builder writeMaskState(ModRenderPhase.WriteMaskState writeMaskState)
			{
				this.writeMaskState = writeMaskState;
				return this;
			}

			public Builder lineWidth(ModRenderPhase.LineWidth lineWidth)
			{
				this.lineWidth = lineWidth;
				return this;
			}

			public ModMultiPhaseParameters build(boolean affectsOutline)
			{
				return this.build(affectsOutline ? OutlineMode.AFFECTS_OUTLINE : OutlineMode.NONE);
			}

			public ModMultiPhaseParameters build(OutlineMode outlineMode)
			{
				return new ModMultiPhaseParameters(this.texture, this.shader, this.modShader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, outlineMode);
			}

		}

		private enum OutlineMode
		{
			NONE("none"),
			IS_OUTLINE("is_outline"),
			AFFECTS_OUTLINE("affects_outline");
			private final String name;

			OutlineMode(String name)
			{
				this.name = name;
			}

			public String toString()
			{
				return this.name;
			}
		}
	}

	public static class ModMultiPhase extends ModRenderLayer
	{
		//Renders the
		static final BiFunction<Identifier, Cull, ModRenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) -> ModRenderLayer.of("outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, ModMultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new ModRenderPhase.Texture(texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST).target(OUTLINE_TARGET).build(ModMultiPhaseParameters.OutlineMode.IS_OUTLINE)));
		private final ModMultiPhaseParameters phases;
		private final Optional<RenderLayer> affectedOutline;
		private final boolean outline;

		ModMultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ModMultiPhaseParameters phases)
		{
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () -> phases.phases.forEach(RenderPhase::startDrawing), () -> phases.phases.forEach(RenderPhase::endDrawing));
			this.phases = phases;
			this.affectedOutline = phases.outlineMode == ModMultiPhaseParameters.OutlineMode.AFFECTS_OUTLINE ? phases.texture.getId().map((texture) -> CULLING_LAYERS.apply(texture, phases.cull)) : Optional.empty();
			this.outline = phases.outlineMode == ModMultiPhaseParameters.OutlineMode.IS_OUTLINE;
		}

		public Optional<RenderLayer> getAffectedOutline()
		{
			return affectedOutline;
		}

		public boolean isOutline()
		{
			return this.outline;
		}

		protected final ModMultiPhaseParameters getPhases()
		{
			return this.phases;
		}

		public String toString()
		{
			return "ModRenderType[" + this.name + ":" + this.phases + "]";
		}


	}

}
