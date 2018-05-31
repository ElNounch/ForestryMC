package forestry.book.gui.elements;

import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.events.GuiEvent;
import forestry.book.data.IndexEntry;
import forestry.book.gui.GuiForesterBook;
import forestry.book.gui.GuiForestryBookPages;
import forestry.core.gui.elements.TextElement;
import forestry.core.gui.elements.layouts.VerticalLayout;

@SideOnly(Side.CLIENT)
public class IndexElement extends VerticalLayout {
	public IndexElement(int xPos, int yPos, IndexEntry[] data) {
		super(xPos, yPos, 108);
		for (IndexEntry index : data) {
			add(new IndexEntryElement(index));
		}
	}

	private class IndexEntryElement extends TextElement {
		private final IndexEntry data;

		public IndexEntryElement(IndexEntry data) {
			super(-1, 9, data.title, GuiElementAlignment.TOP_LEFT, 0, true);
			setWidth(width + TextElement.FONT_RENDERER.getStringWidth(" > "));
			this.data = data;
			addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
				GuiForesterBook bookGui = GuiForesterBook.getGuiScreen();
				if (bookGui instanceof GuiForestryBookPages) {
					GuiForestryBookPages pagesGui = (GuiForestryBookPages) bookGui;
					pagesGui.switchPage(data.page);
				}
			});
		}

		@Override
		public void drawElement(int mouseX, int mouseY) {
			boolean mouseOver = isMouseOver(mouseX, mouseY);
			boolean unicode = FONT_RENDERER.getUnicodeFlag();
			String preFix = mouseOver ? TextFormatting.GOLD + " > " : TextFormatting.DARK_GRAY + "- ";
			FONT_RENDERER.setUnicodeFlag(this.unicode);
			FONT_RENDERER.drawString(preFix + text, 0, 0, color);
			FONT_RENDERER.setUnicodeFlag(unicode);
		}

		@Override
		public boolean canMouseOver() {
			return true;
		}
	}
}
