package forestry.book.gui.elements.encyclopedia;

import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.book.gui.elements.encyclopedia.pages.EncyclopediaPage;
import forestry.book.gui.elements.encyclopedia.pages.EncyclopediaTitlePage;
import forestry.core.gui.elements.layouts.ElementGroup;
import forestry.core.gui.event.EventValueChanged;

public class EncyclopediaContent extends ElementGroup {
	private EncyclopediaPage currentPage;

	public EncyclopediaContent() {
		super(0, 0, GuiEncyclopedia.PAGE_WIDTH, GuiEncyclopedia.PAGE_HEIGHT);
		currentPage = add(new EncyclopediaTitlePage());
		addEventHandler(EventValueChanged.class, event -> {
			Object value = event.getValue();
			if(value instanceof EncyclopediaEntry){
				currentPage.setEntry((EncyclopediaEntry) value);
			}
		});
	}

	public void setCurrentPage(EncyclopediaPage currentPage) {
		currentPage.setEntry(this.currentPage.getEntry());
		remove(this.currentPage);
		this.currentPage = currentPage;
		add(currentPage);
	}
}
