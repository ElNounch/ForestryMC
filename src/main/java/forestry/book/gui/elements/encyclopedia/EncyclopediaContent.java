package forestry.book.gui.elements.encyclopedia;

import javax.annotation.Nullable;

import forestry.api.book.IEncySpeciesPage;
import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.book.gui.elements.encyclopedia.pages.EncySpeciesOverview;
import forestry.core.gui.elements.ScrollBarElement;
import forestry.core.gui.elements.ScrollableElement;
import forestry.core.gui.event.EventValueChanged;

public class EncyclopediaContent extends ScrollableElement {
	@Nullable
	private IEncySpeciesPage currentPage;
	private final ScrollBarElement scrollBar;

	public EncyclopediaContent() {
		super(0, 0, GuiEncyclopedia.PAGE_WIDTH, GuiEncyclopedia.PAGE_HEIGHT);
		scrollBar = add(new ScrollBarElement(width - 10, 4, GuiEncyclopedia.SCROLLBAR_BACKGROUND, false, GuiEncyclopedia.SCROLLBAR_SLIDER));
		setCurrentPage(new EncySpeciesOverview());
		addEventHandler(EventValueChanged.class, event -> {
			Object value = event.getValue();
			if(value instanceof EncyclopediaEntry){
				currentPage.setEntry((EncyclopediaEntry) value);
				int invisibleArea = getInvisibleArea();
				if (invisibleArea > 0) {
					scrollBar.setParameters(this, 0, invisibleArea, 1);
					scrollBar.setVisible(true);
				} else {
					scrollBar.setValue(0);
					scrollBar.setVisible(false);
				}
			}
		});
	}

	public void setCurrentPage(IEncySpeciesPage currentPage) {
		add(currentPage);
		setContent(currentPage);
		if(this.currentPage != null) {
			currentPage.setEntry(this.currentPage.getEntry());
			this.currentPage.hide();
		}
		this.currentPage = currentPage;
		currentPage.show();

		int invisibleArea = getInvisibleArea();
		if (invisibleArea > 0) {
			scrollBar.setParameters(this, 0, invisibleArea, 1);
			scrollBar.setVisible(true);
		} else {
			scrollBar.setValue(0);
			scrollBar.setVisible(false);
		}
	}
}
