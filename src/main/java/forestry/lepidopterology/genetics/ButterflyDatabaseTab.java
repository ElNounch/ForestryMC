package forestry.lepidopterology.genetics;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleTolerance;
import forestry.api.genetics.IDatabaseTab;
import forestry.api.gui.GuiConstants;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IDatabaseElement;
import forestry.api.lepidopterology.EnumButterflyChromosome;
import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IAlleleButterflySpecies;
import forestry.api.lepidopterology.IButterfly;
import forestry.core.genetics.GenericRatings;
import forestry.core.genetics.alleles.AlleleBoolean;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.utils.StringUtil;
import forestry.core.utils.Translator;

@SideOnly(Side.CLIENT)
public class ButterflyDatabaseTab implements IDatabaseTab<IButterfly> {
	private final boolean active;

	ButterflyDatabaseTab(boolean active) {
		this.active = active;
	}

	@Override
	public void createElements(IDatabaseElement container, IButterfly butterfly, ItemStack itemStack) {
		IAlleleButterflySpecies primarySpecies = butterfly.getGenome().getPrimary();

		container.label(Translator.translateToLocal("for.gui.database.tab." + (active ? "active" : "inactive") + "_species.name"), GuiElementAlignment.TOP_CENTER, GuiElementFactory.DATABASE_TITLE);

		container.addAlleleRow(Translator.translateToLocal("for.gui.species"), butterfly, EnumButterflyChromosome.SPECIES, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.size"), butterfly, EnumButterflyChromosome.SIZE, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.lifespan"), butterfly, EnumButterflyChromosome.LIFESPAN, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.speed"), butterfly,EnumButterflyChromosome.SPEED, active);

		container.addAlleleRow(Translator.translateToLocal("for.gui.metabolism"), (IAlleleInteger a)-> GenericRatings.rateMetabolism(a.getValue()), butterfly, EnumButterflyChromosome.METABOLISM, active);

		IAlleleInteger fertility = (IAlleleInteger) (active ? butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.FERTILITY) : butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.FERTILITY));
		container.addFertilityRow(Translator.translateToLocal("for.gui.fertility"), fertility, 8);

		container.addAlleleRow(Translator.translateToLocal("for.gui.flowers"), butterfly, EnumButterflyChromosome.FLOWER_PROVIDER, active);
		container.addAlleleRow(Translator.translateToLocal("for.gui.effect"), butterfly, EnumButterflyChromosome.EFFECT, active);

		IAlleleTolerance tempTolerance = (IAlleleTolerance) (active ? butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.TEMPERATURE_TOLERANCE) : butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.TEMPERATURE_TOLERANCE));

		container.addToleranceRow(Translator.translateToLocal("for.gui.climate"), tempTolerance, primarySpecies, AlleleManager.climateHelper.toDisplay(primarySpecies.getTemperature()));

		IAlleleTolerance humidTolerance = (IAlleleTolerance) (active ? butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.HUMIDITY_TOLERANCE) : butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.HUMIDITY_TOLERANCE));

		container.addToleranceRow(Translator.translateToLocal("for.gui.humidity"), humidTolerance, primarySpecies, AlleleManager.climateHelper.toDisplay(primarySpecies.getHumidity()));

		String yes = Translator.translateToLocal("for.yes");
		String no = Translator.translateToLocal("for.no");

		String diurnal, nocturnal;
		if(active) {
			if (butterfly.getGenome().getNocturnal()) {
				nocturnal = diurnal = yes;
			} else {
				nocturnal = butterfly.getGenome().getPrimary().isNocturnal() ? yes : no;
				diurnal = !butterfly.getGenome().getPrimary().isNocturnal() ? yes : no;
			}
		}else {
			if (((AlleleBoolean) butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.NOCTURNAL)).getValue()) {
				nocturnal = diurnal = yes;
			} else {
				nocturnal = butterfly.getGenome().getSecondary().isNocturnal() ? yes : no;
				diurnal = !butterfly.getGenome().getSecondary().isNocturnal() ? yes : no;
			}
		}

		container.label(Translator.translateToLocal("for.gui.diurnal"), GuiElementAlignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
		container.addRow(Translator.translateToLocal("for.gui.diurnal"), diurnal, false);

		container.addRow(Translator.translateToLocal("for.gui.nocturnal"), nocturnal, false);

		String flyer = StringUtil.readableBoolean(active ? butterfly.getGenome().getTolerantFlyer() : ((AlleleBoolean) butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.TOLERANT_FLYER)).getValue(), yes, no);
		container.addAlleleRow(Translator.translateToLocal("for.gui.flyer"), (a) -> flyer, butterfly, EnumButterflyChromosome.TOLERANT_FLYER, active);

		String fireresist = StringUtil.readableBoolean(active ? butterfly.getGenome().getFireResist() : ((AlleleBoolean) butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.FIRE_RESIST)).getValue(), yes, no);
		container.addAlleleRow(Translator.translateToLocal("for.gui.fireresist"), (a) -> fireresist, butterfly, EnumButterflyChromosome.FIRE_RESIST, active);
	}

	@Override
	public ItemStack getIconStack() {
		return ButterflyDefinition.BlueWing.getMemberStack(active ? EnumFlutterType.BUTTERFLY : EnumFlutterType.CATERPILLAR);
	}
}
