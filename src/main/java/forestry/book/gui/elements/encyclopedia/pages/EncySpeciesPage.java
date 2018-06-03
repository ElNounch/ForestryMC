package forestry.book.gui.elements.encyclopedia.pages;

import javax.annotation.Nullable;

import forestry.api.book.IEncyclopediaEntry;
import forestry.api.genetics.gadgets.IDatabasePage;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.elements.layouts.VerticalLayout;

public abstract class EncySpeciesPage extends VerticalLayout implements IDatabasePage {
	@Nullable
	private IEncyclopediaEntry entry;

	protected EncySpeciesPage() {
		super(GuiEncyclopedia.PAGE_WIDTH - 10);
	}

	public void setEntry(@Nullable IEncyclopediaEntry entry) {
		this.entry = entry;
		updateContent(entry);
	}

	public abstract void updateContent(@Nullable IEncyclopediaEntry entry);

	@Nullable
	public IEncyclopediaEntry getEntry() {
		return entry;
	}
}
