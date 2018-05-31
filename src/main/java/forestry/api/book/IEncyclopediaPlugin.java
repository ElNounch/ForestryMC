package forestry.api.book;

import java.util.Collection;

import forestry.api.genetics.IAlleleSpecies;
import forestry.api.gui.IGuiElement;

public interface IEncyclopediaPlugin<S extends IAlleleSpecies> {
	Collection<IAlleleSpecies> getAllSpecies();

	Collection<IGuiElement> createElements(S species, int leftPageHeight, int rightPageHeight, int pageWidth);
}
