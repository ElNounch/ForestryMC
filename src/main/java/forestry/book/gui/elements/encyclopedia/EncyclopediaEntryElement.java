package forestry.book.gui.elements.encyclopedia;

import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.ILabelElement;
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
	private final ILabelElement text;
	private boolean selected;

	public EncyclopediaEntryElement(EncyclopediaEntry entry, ElementList<EncyclopediaEntry> elementList) {
		super(GuiEncyclopedia.PAGE_WIDTH, 18);
		this.species = entry;

		item(1, 1, entry.getStack());
		text = label(entry.getName(), -1, 9, GuiElementAlignment.MIDDLE_CENTER, UNSELECTED_STYLE);
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			elementList.setCurrentValue(entry);
		});
		addEventHandler(EventValueChanged.class, event -> {
			if(event.getOrigin() == elementList){
				if(event.getValue() == entry){
					text.setStyle(SELECTED_STYLE);
				}else {
					text.setStyle(UNSELECTED_STYLE);
				}
			}
		});
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
