package forestry.book.encyclopedia;

import net.minecraft.item.ItemStack;

import forestry.api.book.IEncyclopediaEntry;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesDisplayHelper;
import forestry.core.genetics.SpeciesManager;

public class EncyclopediaEntry implements IEncyclopediaEntry {
	private final IAlleleSpecies species;
	private final ItemStack displayStack;
	private final String name;
	private final boolean discovered;

	public EncyclopediaEntry(IAlleleSpecies species, IBreedingTracker tracker) {
		this.species = species;
		this.discovered = tracker.isDiscovered(species);
		this.name = /*discovered ?*/ species.getAlleleName() /*: EncyclopediaManager.INSTANCE.getRandomName(species)*/;
		ISpeciesDisplayHelper displayHelper = SpeciesManager.INSTANCE.getDisplayHelper(species.getRoot().getUID());
		this.displayStack = displayHelper.getDisplayStack(species);
	}

	public String getName() {
		return name;
	}

	public IAlleleSpecies getSpecies() {
		return species;
	}

	public ItemStack getStack(){
		return displayStack;
	}

	public boolean isDiscovered() {
		return discovered;
	}
}
