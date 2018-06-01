package forestry.book.gui.elements.encyclopedia.pages;

import javax.annotation.Nullable;

import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.elements.layouts.VerticalLayout;

public abstract class EncyclopediaPage extends VerticalLayout {
	@Nullable
	private EncyclopediaEntry entry;

	protected EncyclopediaPage() {
		super(GuiEncyclopedia.PAGE_WIDTH);
	}

	public void setEntry(@Nullable EncyclopediaEntry entry) {
		this.entry = entry;
		updateContent(entry);
	}

	public abstract void updateContent(@Nullable EncyclopediaEntry entry);

	@Nullable
	public EncyclopediaEntry getEntry() {
		return entry;
	}
}
