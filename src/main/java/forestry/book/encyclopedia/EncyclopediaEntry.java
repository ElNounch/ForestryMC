package forestry.book.encyclopedia;

import net.minecraft.item.ItemStack;

import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;

public class EncyclopediaEntry {
	private final IAlleleSpecies species;
	private final String name;
	private final boolean discovered;

	public EncyclopediaEntry(IAlleleSpecies species, IBreedingTracker tracker) {
		this.species = species;
		this.discovered = tracker.isDiscovered(species);
		this.name = discovered ? species.getAlleleName() : EncyclopediaManager.INSTANCE.getRandomName(species);
	}

	public String getName() {
		return name;
	}

	public IAlleleSpecies getSpecies() {
		return species;
	}

	public ItemStack getStack(){
		return species.getRoot().getMemberStack(species, species.getRoot().getIconType());
	}

	public boolean isDiscovered() {
		return discovered;
	}
}
