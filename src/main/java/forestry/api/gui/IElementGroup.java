package forestry.api.gui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.gui.style.ITextStyle;

@SideOnly(Side.CLIENT)
public interface IElementGroup extends IGuiElement {
	/**
	 * Adds a element to this layout.
	 */
	<E extends IGuiElement> E add(E element);

	/**
	 * Removes a element from this layout.
	 */
	<E extends IGuiElement> E remove(E element);

	default IElementGroup add(IGuiElement... elements) {
		for (IGuiElement element : elements) {
			add(element);
		}
		return this;
	}

	default IElementGroup remove(IGuiElement... elements) {
		for (IGuiElement element : elements) {
			remove(element);
		}
		return this;
	}

	default IElementGroup add(Collection<IGuiElement> elements) {
		elements.forEach(this::add);
		return this;
	}

	default IElementGroup remove(Collection<IGuiElement> elements) {
		elements.forEach(this::remove);
		return this;
	}

	void clear();

	@Nullable
	IGuiElement getLastElement();

	/**
	 * @return All elements that this layout contains.
	 */
	List<IGuiElement> getElements();

	IGuiElement item(int xPos, int yPos, ItemStack itemStack);

	default IGuiElement item(ItemStack itemStack) {
		return item(0, 0, itemStack);
	}

	/**
	 * Adds a text element with the default color,the align {@link GuiElementAlignment#TOP_LEFT} and the height 12.
	 */
	IGuiElement label(String text);

	IGuiElement label(String text, ITextStyle style);

	IGuiElement label(String text, GuiElementAlignment align);

	IGuiElement label(String text, GuiElementAlignment align, ITextStyle textStyle);

	IGuiElement label(String text, int width, int height, GuiElementAlignment align, ITextStyle textStyle);

	IGuiElement label(String text, int x, int y, int width, int height, GuiElementAlignment align, ITextStyle textStyle);

	default IElementLayout vertical(int width) {
		return vertical(0, 0, width);
	}

	IElementLayout vertical(int xPos, int yPos, int width);

	IElementLayout horizontal(int xPos, int yPos, int height);

	default IElementLayout horizontal(int height) {
		return horizontal(0, 0, height);
	}

	IElementGroup panel(int xPos, int yPos, int width, int height);

	default IElementGroup panel(int width, int height) {
		return panel(0, 0, width, height);
	}

	IElementLayoutHelper layoutHelper(IElementLayoutHelper.LayoutFactory layoutFactory, int width, int height);
}
