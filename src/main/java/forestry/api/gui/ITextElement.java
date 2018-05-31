package forestry.api.gui;

import java.util.Collection;
import java.util.Map;

import forestry.api.gui.style.ITextStyle;

public interface ITextElement extends IGuiElement {

	/**
	 * @return The text this element displays.
	 */
	Collection<String> getText();

	/**
	 * @return The raw text this element displays without any formations and their style.
	 */
	Map<ITextStyle, String> getRawText();
}
