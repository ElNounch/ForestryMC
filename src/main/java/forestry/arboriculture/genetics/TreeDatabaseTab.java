package forestry.arboriculture.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.arboriculture.EnumFruitFamily;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.IAlleleFruit;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITree;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IDatabaseTab;
import forestry.api.genetics.IFruitFamily;
import forestry.api.gui.GuiConstants;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IDatabaseElement;
import forestry.api.gui.style.ITextStyle;
import forestry.arboriculture.genetics.alleles.AlleleFruits;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.utils.Translator;

@SideOnly(Side.CLIENT)
public class TreeDatabaseTab implements IDatabaseTab<ITree> {
	private final boolean active;

	TreeDatabaseTab(boolean active) {
		this.active = active;
	}

	@Override
	public void createElements(IDatabaseElement container, ITree tree, ItemStack itemStack) {
		IAlleleTreeSpecies primarySpecies = tree.getGenome().getPrimary();
		IAlleleTreeSpecies species = active ? primarySpecies : tree.getGenome().getSecondary();
		ITextStyle speciesStyle = GuiElementFactory.INSTANCE.getStateStyle(species.isDominant());

		container.label(Translator.translateToLocal("for.gui.database.tab." + (active ? "active" : "inactive") + "_species.name"), GuiElementAlignment.TOP_CENTER, GuiElementFactory.DATABASE_TITLE);

		container.addAlleleRow(Translator.translateToLocal("for.gui.species"), tree, EnumTreeChromosome.SPECIES, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.saplings"), tree, EnumTreeChromosome.FERTILITY, active);
		container.addAlleleRow(Translator.translateToLocal("for.gui.maturity"), tree, EnumTreeChromosome.MATURATION, active);
		container.addAlleleRow(Translator.translateToLocal("for.gui.height"), tree, EnumTreeChromosome.HEIGHT, active);

		IAlleleInteger girth = (IAlleleInteger) (active ? tree.getGenome().getActiveAllele(EnumTreeChromosome.GIRTH) : tree.getGenome().getInactiveAllele(EnumTreeChromosome.GIRTH));
		container.addRow(Translator.translateToLocal("for.gui.girth"), String.format("%sx%s", girth.getValue(), girth.getValue()), girth.isDominant());

		container.addAlleleRow(Translator.translateToLocal("for.gui.yield"), tree, EnumTreeChromosome.YIELD, active);
		container.addAlleleRow(Translator.translateToLocal("for.gui.sappiness"), tree, EnumTreeChromosome.SAPPINESS, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.effect"), tree, EnumTreeChromosome.EFFECT, active);

		container.addRow(Translator.translateToLocal("for.gui.native"), Translator.translateToLocal("for.gui." + tree.getGenome().getPrimary().getPlantType().toString().toLowerCase(Locale.ENGLISH)), species.isDominant());

		container.label(Translator.translateToLocal("for.gui.supports"), GuiElementAlignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
		List<IFruitFamily> families = new ArrayList<>(tree.getGenome().getPrimary().getSuitableFruit());

		for (IFruitFamily fruitFamily : families) {
			container.label(fruitFamily.getName(), GuiElementAlignment.TOP_CENTER, speciesStyle);
		}

		IAlleleFruit fruit = (IAlleleFruit) (active ? tree.getGenome().getActiveAllele(EnumTreeChromosome.FRUITS) : tree.getGenome().getInactiveAllele(EnumTreeChromosome.FRUITS));
		ITextStyle textStyle = GuiElementFactory.INSTANCE.getStateStyle(tree.getGenome().getActiveAllele(EnumTreeChromosome.FRUITS).isDominant());

		container.label(Translator.translateToLocal("for.gui.fruits"), GuiElementAlignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
		String strike = "";
		if (!species.getSuitableFruit().contains(fruit.getProvider().getFamily()) && fruit != AlleleFruits.fruitNone) {
			strike = TextFormatting.STRIKETHROUGH.toString();
		}
		container.label(strike + fruit.getProvider().getDescription(), GuiElementAlignment.TOP_CENTER, textStyle);

		IFruitFamily family = fruit.getProvider().getFamily();

		if (family != null && !family.getUID().equals(EnumFruitFamily.NONE.getUID())) {
			container.label(Translator.translateToLocal("for.gui.family"), GuiElementAlignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
			container.label(family.getName(), GuiElementAlignment.TOP_CENTER, textStyle);
		}

	}

	@Override
	public ItemStack getIconStack() {
		return TreeDefinition.Cherry.getMemberStack(active ? EnumGermlingType.SAPLING : EnumGermlingType.POLLEN);
	}
}
