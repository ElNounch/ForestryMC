package forestry.api.book;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import forestry.api.gui.IElementGroup;

public interface IEncySpeciesPage extends IElementGroup {
	void setEntry(@Nullable IEncyclopediaEntry entry);

	IEncyclopediaEntry getEntry();

	ItemStack getButtonIcon();
}
