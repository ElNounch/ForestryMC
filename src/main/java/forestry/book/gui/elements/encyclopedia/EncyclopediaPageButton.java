package forestry.book.gui.elements.encyclopedia;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import forestry.api.book.IEncySpeciesPage;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.Drawable;
import forestry.core.gui.GuiUtil;
import forestry.core.gui.elements.ButtonElement;

public class EncyclopediaPageButton extends ButtonElement {
	public static final Drawable TEXTURE = new Drawable(GuiEncyclopedia.TEXTURE, 0, 202, 24, 21);

	private final IEncySpeciesPage page;

	public EncyclopediaPageButton(int xPos, int yPos, EncyclopediaContent content, IEncySpeciesPage page) {
		super(xPos, yPos, TEXTURE, element -> content.setCurrentPage(page));
		this.page = page;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		super.drawElement(mouseX, mouseY);
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.translate(8.0F, 4.0F, zLevel);
		GlStateManager.scale(0.85F, 0.85F, 0.85F);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();

		GuiUtil.drawItemStack(Minecraft.getMinecraft().fontRenderer, page.getButtonIcon(), 0, 0);

		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}
