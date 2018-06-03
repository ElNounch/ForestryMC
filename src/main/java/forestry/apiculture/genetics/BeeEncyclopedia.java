package forestry.apiculture.genetics;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.book.IEncySpeciesPage;
import forestry.api.book.IEncyclopediaCategory;
import forestry.api.genetics.ISpeciesRoot;
import forestry.apiculture.gui.encyclopedia.DatabaseBeeGenome;

public class BeeEncyclopedia implements IEncyclopediaCategory<IAlleleBeeSpecies> {
	@Override
	public ISpeciesRoot getRoot() {
		return BeeManager.beeRoot;
	}

	@Override
	public Collection<IEncySpeciesPage> createPages(int pageWidth, int pageHeight) {
		ImmutableList.Builder<IEncySpeciesPage> pages = new ImmutableList.Builder<>();
		pages.add(new DatabaseBeeGenome());
		return pages.build();
	}
}
