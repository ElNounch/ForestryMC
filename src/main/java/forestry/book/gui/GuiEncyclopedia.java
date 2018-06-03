package forestry.book.gui;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import forestry.api.book.IEncySpeciesPage;
import forestry.api.book.IEncyclopediaCategory;
import forestry.book.gui.elements.encyclopedia.EncyclopediaContent;
import forestry.book.gui.elements.encyclopedia.EncyclopediaPageButton;
import forestry.book.gui.elements.encyclopedia.EncyclopediaSpeciesList;
import forestry.core.config.Constants;
import forestry.core.genetics.SpeciesManager;
import forestry.core.gui.Drawable;
import forestry.core.gui.GuiWindow;
import forestry.core.gui.IGuiSizable;
import forestry.core.gui.elements.GeneticAnalyzer;

public class GuiEncyclopedia extends GuiWindow implements IGuiSizable {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, Constants.TEXTURE_PATH_GUI + "/encyclopedia/encyclopedia.png");

	/* Drawables */
	public static final Drawable SCROLLBAR_BACKGROUND = new Drawable(GeneticAnalyzer.TEXTURE, 202, 0, 3, 142);
	public static final Drawable SCROLLBAR_SLIDER = new Drawable(GeneticAnalyzer.TEXTURE, 205, 0, 3, 5);

	public static final int LEFT_PAGE_START_X = 16;
	public static final int RIGHT_PAGE_START_X = 132;
	public static final int PAGE_START_Y = 12;
	public static final int LEFT_PAGE_START_Y = 25;
	public static final int RIGHT_PAGE_START_Y = PAGE_START_Y;
	public static final int PAGE_WIDTH = 108;
	public static final int PAGE_HEIGHT = 155;
	public static final int X_SIZE = 256;
	public static final int Y_SIZE = 181;
	private final EncyclopediaContent content;
	@Nullable
	private IEncyclopediaCategory<?> category;

	public GuiEncyclopedia() {
		super(X_SIZE, Y_SIZE);
		content = window.add(new EncyclopediaContent());
		category = SpeciesManager.INSTANCE.getEncyclopediaCategory("rootBees");
		int yPos = 5;
		if(category != null) {
			for (IEncySpeciesPage page : category.createPages(PAGE_WIDTH, PAGE_HEIGHT)) {
				content.add(page);
				window.add(new EncyclopediaPageButton(-24, yPos, content, page));
				yPos+=21 + 5;
			}
		}
		content.setLocation(GuiEncyclopedia.LEFT_PAGE_START_X, GuiEncyclopedia.PAGE_START_Y);
		window.add(new EncyclopediaSpeciesList()).setLocation(GuiEncyclopedia.RIGHT_PAGE_START_X, GuiEncyclopedia.PAGE_START_Y);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		TextureManager manager = mc.renderEngine;

		manager.bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

		super.drawScreen(mouseX, mouseY, partialTicks);

		/*boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(true);
		drawCenteredString(fontRenderer, TextFormatting.UNDERLINE + getTitle(), guiLeft + LEFT_PAGE_START_X + 52, guiTop + PAGE_START_Y, 0xD3D3D3);

		drawText();

		fontRenderer.setUnicodeFlag(unicode);*/
	}

}
