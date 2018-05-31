package forestry.core.gui.elements;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

import forestry.api.gui.GuiElementAlignment;
import forestry.api.gui.ITextElement;
import forestry.api.gui.style.ITextStyle;

public class Label extends GuiElement implements ITextElement {
	/* Constants */
	public static final FontRenderer FONT_RENDERER = Minecraft.getMinecraft().fontRenderer;

	/* Attributes - Final */
	protected final String text;
	protected final String rawText;
	protected final ITextStyle style;

	public Label(String text, GuiElementAlignment align, ITextStyle style) {
		this(0, 0, -1, FONT_RENDERER.FONT_HEIGHT, text, align, style);
	}

	public Label(int xPos, int yPos, int width, int height, String text, GuiElementAlignment align, ITextStyle style) {
		super(xPos, yPos, width, height);
		this.style = style;
		this.rawText = text;
		this.text = getFormattedString(text);
		setAlign(align);
		if (width < 0) {
			boolean uni = FONT_RENDERER.getUnicodeFlag();
			FONT_RENDERER.setUnicodeFlag(style.isUnicode());
			setWidth(FONT_RENDERER.getStringWidth(this.text));
			FONT_RENDERER.setUnicodeFlag(uni);
		}
	}

	@Override
	public Collection<String> getText() {
		return Collections.singletonList(text);
	}

	@Override
	public Map<ITextStyle, String> getRawText() {
		return ImmutableMap.of(style, rawText);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		boolean unicode = FONT_RENDERER.getUnicodeFlag();
		FONT_RENDERER.setUnicodeFlag(style.isUnicode());
		FONT_RENDERER.drawString(text, 0, 0, style.getColor());
		FONT_RENDERER.setUnicodeFlag(unicode);
	}

	private String getFormattedString(String text) {
		StringBuilder modifiers = new StringBuilder();
		if (style.isBold()) {
			modifiers.append(TextFormatting.BOLD);
		}
		if (style.isItalic()) {
			modifiers.append(TextFormatting.ITALIC);
		}
		if (style.isUnderlined()) {
			modifiers.append(TextFormatting.UNDERLINE);
		}
		if (style.isStrikethrough()) {
			modifiers.append(TextFormatting.STRIKETHROUGH);
		}
		if (style.isObfuscated()) {
			modifiers.append(TextFormatting.OBFUSCATED);
		}
		modifiers.append(text);
		return modifiers.toString();
	}
}
