package forestry.api.gui;

import forestry.api.gui.style.ITextStyle;
import forestry.api.gui.style.ImmutableTextStyle;
import forestry.api.gui.style.TextStyleBuilder;

public class GuiConstants {
	public static final ITextStyle DEFAULT_STYLE = new ImmutableTextStyle();
	public static final ITextStyle BLACK_STYLE = TextStyleBuilder.create().color(0x000000).build();
	public static final ITextStyle UNICODE_STYLE = TextStyleBuilder.create().unicode(true).build();
	public static final ITextStyle UNDERLINED_STYLE = TextStyleBuilder.create().underlined(true).build();
}
