package forestry.book.gui.elements.encyclopedia;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBee;
import forestry.book.encyclopedia.EncyclopediaEntry;
import forestry.book.gui.GuiEncyclopedia;
import forestry.core.gui.elements.ElementList;
import forestry.core.gui.elements.ScrollBarElement;
import forestry.core.gui.elements.ScrollableElement;
import forestry.core.gui.elements.layouts.PaneLayout;

public class EncyclopediaSpeciesList extends PaneLayout {
	/* Textures */
	public static final ResourceLocation TEXTURE = GuiEncyclopedia.TEXTURE;

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
		scrollBar = new ScrollBarElement(width - 10, 4, GuiEncyclopedia.SCROLLBAR_BACKGROUND, false, GuiEncyclopedia.SCROLLBAR_SLIDER);
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
		int invisibleArea = scrollable.getInvisibleArea();
		if (invisibleArea > 0) {
			scrollBar.setParameters(scrollable, 0, invisibleArea, 1);
			scrollBar.setVisible(true);
		} else {
			scrollBar.setValue(0);
			scrollBar.setVisible(false);
		}
	}
}
