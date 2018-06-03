package forestry.core.gui.elements;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleTolerance;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.gadgets.DatabaseMode;
import forestry.api.genetics.gadgets.IDatabasePage;
import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.IDatabaseElement;
import forestry.api.gui.IElementGroup;
import forestry.api.gui.IElementLayout;
import forestry.api.gui.IGuiElement;
import forestry.api.gui.style.ITextStyle;
import forestry.core.gui.elements.layouts.PaneLayout;
import forestry.core.gui.elements.layouts.VerticalLayout;
import forestry.core.utils.Translator;

public class DatabaseElement extends VerticalLayout implements IDatabaseElement {
	private final DatabaseMode mode;
	private final IDatabasePage page;

	public DatabaseElement(int width, IDatabasePage page) {
		super(0, 0, width);
		this.page = page;
		this.mode = page.getMode();
	}

	@Override
	public DatabaseMode getMode() {
		return mode;
	}

	@Override
	public IDatabasePage getCurrentPage() {
		return page;
	}

	@Override
	public void addFertilityRow(String chromosomeName, IIndividual individual, IChromosomeType chromosome, int texOffset) {
		IAllele allele = individual.getGenome().getActiveAllele(chromosome);
		if(!(allele instanceof IAlleleInteger)){
			return;
		}
		addRow(chromosomeName, GuiElementFactory.INSTANCE.createFertilityInfo((IAlleleInteger) allele, texOffset));
	}

	@Override
	public void addToleranceRow(IIndividual individual, IChromosomeType chromosome) {
		IAllele allele = individual.getGenome().getActiveAllele(chromosome);
		if(!(allele instanceof IAlleleTolerance)){
			return;
		}
		addRow("  " + Translator.translateToLocal("for.gui.tolerance"), GuiElementFactory.INSTANCE.createToleranceInfo((IAlleleTolerance) allele));
	}

	@Override
	public void addMutation(int x, int y, int width, int height, IMutation mutation, IAllele species, IBreedingTracker breedingTracker) {
		IGuiElement element = GuiElementFactory.INSTANCE.createMutation(x, y, width, height, mutation, species, breedingTracker);
		if (element == null) {
			return;
		}
		add(element);
	}

	@Override
	public void addMutationResultant(int x, int y, int width, int height, IMutation mutation, IBreedingTracker breedingTracker) {
		IGuiElement element = GuiElementFactory.INSTANCE.createMutationResultant(x, y, width, height, mutation, breedingTracker);
		if (element == null) {
			return;
		}
		add(element);
	}

	@Override
	public void addRow(String firstText, String secondText, boolean dominant) {
		addRow(firstText, secondText, GuiElementFactory.GUI_STYLE, GuiElementFactory.INSTANCE.getStateStyle(dominant));
	}

	@Override
	public void addRow(String firstText, String secondText, String thirdText, IIndividual individual, IChromosomeType chromosome) {
		addRow(firstText, secondText, thirdText, GuiElementFactory.GUI_STYLE,
			GuiElementFactory.INSTANCE.getStateStyle(individual.getGenome().getActiveAllele(chromosome).isDominant()),
			GuiElementFactory.INSTANCE.getStateStyle(individual.getGenome().getInactiveAllele(chromosome).isDominant()));
	}

	@Override
	public final void addAlleleRow(String chromosomeName, IIndividual individual, IChromosomeType chromosome, boolean active) {
		addAlleleRow(chromosomeName, IAllele::getAlleleName, individual, chromosome, active);
	}

	@Override
	public void addRow(String firstText, String secondText, ITextStyle firstStyle, ITextStyle secondStyle) {
		IElementLayout first = addSplitText(width, firstText, GuiElementAlignment.TOP_LEFT, firstStyle);
		IElementLayout second = addSplitText(width, secondText, GuiElementAlignment.TOP_LEFT, secondStyle);
		addRow(first, second);
	}

	private IElementLayout addSplitText(int width, String text, GuiElementAlignment alignment, ITextStyle style) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		IElementLayout vertical = new VerticalLayout(width);
		for (String splitText : fontRenderer.listFormattedStringToWidth(text, 70)) {
			vertical.label(splitText, alignment, style);
		}
		return vertical;
	}

	private void addRow(String chromosomeName, IGuiElement right) {
		int center = width / 2;
		IGuiElement first = addSplitText(center, chromosomeName, GuiElementAlignment.TOP_LEFT, GuiElementFactory.GUI_STYLE);
		addRow(first, right);
	}

	private void addRow(IGuiElement first, IGuiElement second) {
		int center = width / 2;
		IElementGroup panel = new PaneLayout(width, 0);
		if (first.getHeight() > second.getHeight()) {
			first.setAlign(GuiElementAlignment.MIDDLE_LEFT);
		} else if (second.getHeight() > first.getHeight()) {
			second.setAlign(GuiElementAlignment.MIDDLE_LEFT);
		}
		panel.add(first);
		panel.add(second);
		second.setXPosition(center);
		add(panel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <A extends IAllele> void addAlleleRow(String chromosomeName, Function<A, String> toString, IIndividual individual, IChromosomeType chromosome, boolean active) {
		A allele;
		if (active) {
			allele = (A) individual.getGenome().getActiveAllele(chromosome);
		} else {
			allele = (A) individual.getGenome().getInactiveAllele(chromosome);
		}
		addRow(chromosomeName, toString.apply(allele), allele.isDominant());
	}

}
