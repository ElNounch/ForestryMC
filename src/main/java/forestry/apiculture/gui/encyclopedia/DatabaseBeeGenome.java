package forestry.apiculture.gui.encyclopedia;

import java.util.function.Function;

import net.minecraft.item.ItemStack;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.gadgets.BlankDatabasePage;
import forestry.api.genetics.gadgets.DatabaseMode;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IDatabaseElement;
import forestry.api.gui.IGuiElementFactory;
import forestry.api.gui.style.ITextStyle;
import forestry.api.gui.style.TextStyleBuilder;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.core.genetics.alleles.AlleleBoolean;
import forestry.core.gui.GuiAlyzer;
import forestry.core.render.ColourProperties;
import forestry.core.utils.StringUtil;
import forestry.core.utils.Translator;

public class DatabaseBeeGenome<I extends IBee> extends BlankDatabasePage<I> {
	private static final ITextStyle BINOMIAL = new TextStyleBuilder().color(()-> ColourProperties.INSTANCE.get("gui.beealyzer.binomial")).build();
	
	public DatabaseBeeGenome(DatabaseMode mode) {
		super(BeeDefinition.MEADOWS.getMemberStack(getType(mode)),  5 - mode.ordinal(), mode);
	}

	private static EnumBeeType getType(DatabaseMode mode){
		switch (mode){
			case BOTH:
				return EnumBeeType.QUEEN;
			case ACTIVE:
				return EnumBeeType.PRINCESS;
			case INACTIVE:
			default:
				return EnumBeeType.DRONE;
		}
	}

	@Override
	public String getDisplayName() {
		return Translator.translateToLocal("for.gui.database.bee");
	}

	@Override
	public void createPage(I bee, ItemStack individualStack, IDatabaseElement page, IGuiElementFactory factory, int width) {
		EnumBeeType type = BeeManager.beeRoot.getType(individualStack);
		if (type == null) {
			return;
		}
		IAlleleSpecies primarySpecies = bee.getGenome().getPrimary();
		IAlleleSpecies secondSpecies = bee.getGenome().getSecondary();
		String yes = Translator.translateToLocal("for.yes");
		String no = Translator.translateToLocal("for.no");

		//page.label(Translator.translateToLocal("for.gui.database.tab." + (active ? "active" : "inactive") + "_species.name"), GuiElementAlignment.TOP_CENTER, GuiElementFactory.DATABASE_TITLE);

		//Species
		{

			{
				String customPrimaryBeeKey = "for.bees.custom.beealyzer." + type.getName() + "." + bee.getGenome().getPrimary().getUnlocalizedName().replace("bees.species.", "");
				String customSecondaryBeeKey = "for.bees.custom.beealyzer." + type.getName() + "." + bee.getGenome().getSecondary().getUnlocalizedName().replace("bees.species.", "");

				page.addSpeciesRow(Translator.translateToLocal("for.gui.species"), GuiAlyzer.checkCustomName(customPrimaryBeeKey), GuiAlyzer.checkCustomName(customSecondaryBeeKey), bee, EnumBeeChromosome.SPECIES);
			}
			page.addAlleleRow(Translator.translateToLocal("for.gui.species"), bee, EnumBeeChromosome.SPECIES);

			page.addRow(Translator.translateToLocal("for.gui.climate"),
				AlleleManager.climateHelper.toDisplay(primarySpecies.getTemperature()),
				AlleleManager.climateHelper.toDisplay(secondSpecies.getTemperature()),
				bee, EnumBeeChromosome.TEMPERATURE_TOLERANCE);
			page.addToleranceRow(bee, EnumBeeChromosome.TEMPERATURE_TOLERANCE);

			page.addRow(Translator.translateToLocal("for.gui.humidity"),
				AlleleManager.climateHelper.toDisplay(primarySpecies.getHumidity()),
				AlleleManager.climateHelper.toDisplay(secondSpecies.getHumidity()),
				bee, EnumBeeChromosome.HUMIDITY_TOLERANCE);
			page.addToleranceRow(bee, EnumBeeChromosome.HUMIDITY_TOLERANCE);
		}

		page.addAlleleRow(Translator.translateToLocal("for.gui.lifespan"), bee, EnumBeeChromosome.LIFESPAN);

		page.addAlleleRow(Translator.translateToLocal("for.gui.speed"), bee, EnumBeeChromosome.SPEED);
		page.addAlleleRow(Translator.translateToLocal("for.gui.pollination"), bee, EnumBeeChromosome.FLOWERING);
		page.addAlleleRow(Translator.translateToLocal("for.gui.flowers"), bee, EnumBeeChromosome.FLOWER_PROVIDER);

		page.addFertilityRow(Translator.translateToLocal("for.gui.fertility"), bee, EnumBeeChromosome.FERTILITY, 0);

		page.addAlleleRow(Translator.translateToLocal("for.gui.area"), bee, EnumBeeChromosome.TERRITORY);
		page.addAlleleRow(Translator.translateToLocal("for.gui.effect"), bee, EnumBeeChromosome.EFFECT);
		{

			String diurnalFirst, diurnalSecond, nocturnalFirst, nocturnalSecond;
			if (bee.getGenome().getNeverSleeps()) {
				nocturnalFirst = diurnalFirst = yes;
			} else {
				nocturnalFirst = bee.getGenome().getPrimary().isNocturnal() ? yes : no;
				diurnalFirst = !bee.getGenome().getPrimary().isNocturnal() ? yes : no;
			}
			if (((AlleleBoolean) bee.getGenome().getInactiveAllele(EnumBeeChromosome.NEVER_SLEEPS)).getValue()) {
				nocturnalSecond = diurnalSecond = yes;
			} else {
				nocturnalSecond = bee.getGenome().getSecondary().isNocturnal() ? yes : no;
				diurnalSecond = !bee.getGenome().getSecondary().isNocturnal() ? yes : no;
			}

			page.addRow(Translator.translateToLocal("for.gui.diurnal"), diurnalFirst, diurnalSecond, false, false);
			page.addRow(Translator.translateToLocal("for.gui.nocturnal"), nocturnalFirst, nocturnalSecond, false, false);
		}

		Function<Boolean, String> flyer = active -> StringUtil.readableBoolean(active ? bee.getGenome().getToleratesRain() : ((AlleleBoolean) bee.getGenome().getInactiveAllele(EnumBeeChromosome.TOLERATES_RAIN)).getValue(), yes, no);
		page.addAlleleRow(Translator.translateToLocal("for.gui.flyer"), (allele, active) -> flyer.apply(active), bee, EnumBeeChromosome.TOLERATES_RAIN);

		Function<Boolean, String> cave = active -> StringUtil.readableBoolean(active ? bee.getGenome().getCaveDwelling() : ((AlleleBoolean) bee.getGenome().getInactiveAllele(EnumBeeChromosome.CAVE_DWELLING)).getValue(), yes, no);
		page.addAlleleRow(Translator.translateToLocal("for.gui.cave"), (allele, active) -> cave.apply(active), bee, EnumBeeChromosome.CAVE_DWELLING);

		String displayText;
		if (type == EnumBeeType.PRINCESS || type == EnumBeeType.QUEEN) {
			String displayTextKey = "for.bees.stock.pristine";
			if (!bee.isNatural()) {
				displayTextKey = "for.bees.stock.ignoble";
			}
			displayText = Translator.translateToLocal(displayTextKey);
			page.label(displayText, GuiElementAlignment.TOP_CENTER, BINOMIAL);
		}
	}
}
