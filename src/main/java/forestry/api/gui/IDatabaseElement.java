package forestry.api.gui;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

import forestry.api.genetics.IAllele;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.gadgets.DatabaseMode;
import forestry.api.genetics.gadgets.IDatabasePage;
import forestry.api.gui.style.ITextStyle;

public interface IDatabaseElement extends IElementGroup {

	/**
	 * Adds the chromosomeName and the name of the active/not active allele, of the chromosome, with {@link #label}.
	 */
	void addAlleleRow(String chromosomeName, IIndividual individual, IChromosomeType chromosome);

	/**
	 * Adds the chromosomeName and the result of toString with {@link #label}.
	 */
	<A extends IAllele> void addAlleleRow(String chromosomeName, BiFunction<A, Boolean, String> toString, IIndividual individual, IChromosomeType chromosome);

	void addFertilityRow(String chromosomeName, IIndividual individual, IChromosomeType chromosome, int texOffset);

	void addToleranceRow(IIndividual individual, IChromosomeType chromosome);

	void addMutation(int x, int y, int width, int height, IMutation mutation, IAllele species, IBreedingTracker breedingTracker);

	void addMutationResultant(int x, int y, int width, int height, IMutation mutation, IBreedingTracker breedingTracker);

	void addRow(String firstText, String secondText, String thirdText, ITextStyle firstStyle, ITextStyle secondStyle, ITextStyle thirdStyle);

	void addRow(String leftText, String rightText, boolean dominant);

	void addRow(String firstText, String secondText, String thirdText, boolean secondDominant, boolean thirdDominant);

	void addRow(String firstText, String secondText, String thirdText, IIndividual individual, IChromosomeType chromosome);

	void addSpeciesRow(String firstText, @Nullable String secondText, @Nullable String thirdText, IIndividual individual, IChromosomeType chromosome);

	DatabaseMode getMode();

	IDatabasePage getCurrentPage();
}
