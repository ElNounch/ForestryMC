package forestry.api.book;

import net.minecraft.item.ItemStack;

import forestry.api.genetics.IAlleleSpecies;

public interface IEncyclopediaEntry {
	String getName();

	IAlleleSpecies getSpecies();

	ItemStack getStack();

	boolean isDiscovered();
}
