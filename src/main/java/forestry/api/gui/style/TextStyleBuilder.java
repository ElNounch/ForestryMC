package forestry.api.gui.style;

import java.util.function.IntSupplier;

public final class TextStyleBuilder{

	public static TextStyleBuilder create(){
		return create(0xFFFFFF, false, false, false, false, false, false, false);
	}

	public static TextStyleBuilder create(ITextStyle style) {
		return create(style.getColor(), style.isBold(), style.isItalic(), style.isUnderlined(), style.isStrikethrough(), style.isObfuscated(), style.isShadow(), style.isUnicode());
	}

	public static TextStyleBuilder create(int color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, boolean shadow, boolean unicode){
		return new TextStyleBuilder(()->color, bold, italic, underlined, strikethrough, obfuscated, shadow, unicode);
	}

	public static TextStyleBuilder create(IntSupplier color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, boolean shadow, boolean unicode){
		return new TextStyleBuilder(color, bold, italic, underlined, strikethrough, obfuscated, shadow, unicode);
	}

	private IntSupplier color;
	private boolean bold;
	private boolean italic;
	private boolean underlined;
	private boolean strikethrough;
	private boolean obfuscated;
	private boolean shadow;
	private boolean unicode;

	private TextStyleBuilder(IntSupplier color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, boolean shadow, boolean unicode) {
		this.color = color;
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		this.shadow = shadow;
		this.unicode = unicode;
	}

	public TextStyleBuilder color(int color) {
		this.color = () -> color;
		return this;
	}

	public TextStyleBuilder color(IntSupplier color) {
		this.color = color;
		return this;
	}

	public TextStyleBuilder bold(boolean bold) {
		this.bold = bold;
		return this;
	}

	public TextStyleBuilder obfuscated(boolean obfuscated) {
		this.obfuscated = obfuscated;
		return this;
	}

	public TextStyleBuilder strikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
		return this;
	}

	public TextStyleBuilder underlined(boolean underlined) {
		this.underlined = underlined;
		return this;
	}

	public TextStyleBuilder italic(boolean italic) {
		this.italic = italic;
		return this;
	}

	public TextStyleBuilder shadow(boolean shadow) {
		this.shadow = shadow;
		return this;
	}

	public TextStyleBuilder unicode(boolean unicode) {
		this.unicode = unicode;
		return this;
	}

	public ITextStyle build(){
		return new ImmutableTextStyle(color, bold, italic, underlined, strikethrough, obfuscated, shadow, unicode);
	}
}
