package forestry.api.genetics.gadgets;

import net.minecraft.client.gui.GuiScreen;

import forestry.api.genetics.IIndividual;
import forestry.api.gui.IDatabaseElement;

public interface IAnalyzerPlugin {
	/**
	 * Creates the first page of the alyzer.
	 *
	 * @param gui A instance of the alyzer gui.
	 * @param element A helper to create the gui elements.
	 * @param individual The individual that is currently in the alyzer slot.
	 */
	void createFirstPage(GuiScreen gui, IDatabaseElement element, IIndividual individual);

	/**
	 * Creates the second page of the alyzer.
	 *
	 * @param gui A instance of the alyzer gui.
	 * @param element A helper to create the gui elements.
	 * @param individual The individual that is currently in the alyzer slot.
	 */
	void createSecondPage(GuiScreen gui, IDatabaseElement element, IIndividual individual);

	/**
	 * Creates the third page of the alyzer. This page is usually used to display the products of the individual.
	 *
	 * @param gui A instance of the alyzer gui.
	 * @param element A helper to create the gui elements.
	 * @param individual The individual that is currently in the alyzer slot.
	 */
	void createThirdPage(GuiScreen gui, IDatabaseElement element, IIndividual individual);
}
