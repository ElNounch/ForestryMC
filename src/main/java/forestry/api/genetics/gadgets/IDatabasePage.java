package forestry.api.genetics.gadgets;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.genetics.IIndividual;
import forestry.api.gui.IDatabaseElement;
import forestry.api.gui.IGuiElementFactory;

@SideOnly(Side.CLIENT)
public interface IDatabasePage<I extends IIndividual> extends Comparable<IDatabasePage<I>> {

	ItemStack getIconStack();

	String getDisplayName();

	DatabaseMode getMode();

	void createPage(I individual, ItemStack individualStack, IDatabaseElement page, IGuiElementFactory factory, int width);
}
