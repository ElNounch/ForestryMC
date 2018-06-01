package forestry.book.gui.elements.encyclopedia;

import forestry.api.genetics.IAlleleSpecies;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IElementGroup;
import forestry.api.gui.events.GuiEvent;
import forestry.api.gui.style.ITextStyle;
import forestry.api.gui.style.TextStyleBuilder;
import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.elements.ElementList;
import forestry.core.gui.elements.layouts.PaneLayout;
import forestry.core.gui.event.EventValueChanged;

public	class EncyclopediaEntryElement extends PaneLayout {
	public static final ITextStyle UNSELECTED_STYLE = new TextStyleBuilder().color(0xa0a0a0).build();
	//public static final ITextStyle UNDISCOVERED_STYLE = new TextStyleBuilder().color(0xe0a859).build();
	public static final ITextStyle SELECTED_STYLE = new TextStyleBuilder().color(0x4f4f4f).build();

	private final EncyclopediaEntry species;
	private final IElementGroup text;
	private boolean selected;

	public EncyclopediaEntryElement(EncyclopediaEntry entry, ElementList<EncyclopediaEntry> elementList) {
		super(GuiEncyclopedia.PAGE_WIDTH - 40, 18);
		this.species = entry;

		IAlleleSpecies species = entry.getSpecies();
		item(1, 1, entry.getStack());
		text = pane(17, 1, width, height);
		text.label( entry.getName(), GuiElementAlignment.MIDDLE_CENTER, UNSELECTED_STYLE);
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			elementList.setCurrentValue(entry);
		});
		addEventHandler(EventValueChanged.class, event -> {
			if(event.getOrigin() == elementList){
				text.clear();
				if(event.getValue() == entry){
					text.label( entry.getName(), GuiElementAlignment.MIDDLE_CENTER, SELECTED_STYLE);
				}else {
					text.label( entry.getName(), GuiElementAlignment.MIDDLE_CENTER, UNSELECTED_STYLE);
				}
			}
		});
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
