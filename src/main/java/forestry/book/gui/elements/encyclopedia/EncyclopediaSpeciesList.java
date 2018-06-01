package forestry.book.gui.elements.encyclopedia;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBee;
import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.Drawable;
import forestry.core.gui.elements.ElementList;
import forestry.core.gui.elements.GeneticAnalyzer;
import forestry.core.gui.elements.ScrollBarElement;
import forestry.core.gui.elements.ScrollableElement;
import forestry.core.gui.elements.layouts.PaneLayout;
import forestry.core.gui.widgets.IScrollable;

public class EncyclopediaSpeciesList extends PaneLayout implements IScrollable {
	/* Textures */
	public static final ResourceLocation TEXTURE = GuiEncyclopedia.TEXTURE;

	/* Drawables */
	public static final Drawable SCROLLBAR_BACKGROUND = new Drawable(GeneticAnalyzer.TEXTURE, 202, 0, 3, 142);
	public static final Drawable SCROLLBAR_SLIDER = new Drawable(GeneticAnalyzer.TEXTURE, 205, 0, 3, 5);

	/*Attributes - Gui Elements */
	private final ScrollBarElement scrollBar;
	private final ScrollableElement scrollable;
	private final ElementList<EncyclopediaEntry> scrollableContent;

	public EncyclopediaSpeciesList() {
		super(GuiEncyclopedia.PAGE_WIDTH, GuiEncyclopedia.PAGE_HEIGHT);
		//Background Texture
		//drawable(32, 0, new Drawable(TEXTURE, 0, 0, 163, 166));
		//Text Area
		scrollable = new ScrollableElement(4, 0, 145, 150);
		add(scrollable);
		scrollableContent = new ElementList<>(0, 0, 145, EncyclopediaEntryElement::new, null);
		scrollable.add(scrollableContent);
		scrollable.setContent(scrollableContent);
		//Scrollbar
		scrollBar = new ScrollBarElement(width - 10, 4, SCROLLBAR_BACKGROUND, false, SCROLLBAR_SLIDER);
		scrollBar.setVisible(true);
		add(scrollBar);
		List<EncyclopediaEntry> entries = new LinkedList<>();
		for (IBee bee : BeeManager.beeRoot.getIndividualTemplates()) {
			entries.add(new EncyclopediaEntry(bee.getGenome().getPrimary(), BeeManager.beeRoot.getBreedingTracker(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getGameProfile())));
		}
		//speciesList.sort(Comparator.comparingInt(IAlleleSpecies::getComplexity));
		scrollableContent.setOptions(entries);
		/*for(IAlleleSpecies species : speciesList){
			scrollableContent.add(new EncyclopediaEntry(species));
		}*/
		int invisibleElements = scrollable.getInvisibleElementCount();
		if (invisibleElements > 0) {
			scrollBar.setParameters(this, 0, invisibleElements, 1);
			scrollBar.setVisible(true);
		} else {
			scrollBar.setValue(0);
			scrollBar.setVisible(false);
		}
	}

	@Override
	public void onScroll(int value) {
		scrollable.onScroll(value);
	}

	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return isMouseOver();
	}
}
