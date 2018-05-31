package forestry.book.gui.elements.encyclopedia;

import forestry.api.genetics.IAlleleSpecies;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IElementGroup;
import forestry.api.gui.events.GuiEvent;
import forestry.api.gui.style.ITextStyle;
import forestry.api.gui.style.TextStyleBuilder;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.elements.layouts.PaneLayout;

public	class EncyclopediaEntry extends PaneLayout {
	public static final ITextStyle UNSELECTED_STYLE = TextStyleBuilder.create().color(0xa0a0a0).build();
	public static final ITextStyle SELECTED_STYLE = TextStyleBuilder.create().color(0x4f4f4f).build();

	private final IAlleleSpecies species;
	private final IElementGroup text;
	private boolean selected;

	public EncyclopediaEntry(IAlleleSpecies species) {
		super(GuiEncyclopedia.PAGE_WIDTH - 40, 18);
		this.species = species;

		item(1, 1, species.getRoot().getMemberStack(species, species.getRoot().getIconType()));
		text = panel(17, 1, width, height);
		text.label( species.getAlleleName(), GuiElementAlignment.MIDDLE_CENTER, UNSELECTED_STYLE);
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			text.clear();
			if(selected){
				selected = false;
				text.label( species.getAlleleName(), GuiElementAlignment.MIDDLE_CENTER, UNSELECTED_STYLE);
				return;
			}
			selected = true;
			text.label( species.getAlleleName(), GuiElementAlignment.MIDDLE_CENTER, SELECTED_STYLE);
		});
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
