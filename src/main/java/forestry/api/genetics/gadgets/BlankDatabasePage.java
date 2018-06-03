package forestry.api.genetics.gadgets;

import net.minecraft.item.ItemStack;

import forestry.api.genetics.IIndividual;

public abstract class BlankDatabasePage<I extends IIndividual> implements IDatabasePage<I> {
	private final ItemStack iconStack;
	private final int priority;
	private final DatabaseMode mode;

	public BlankDatabasePage(ItemStack iconStack, int priority, DatabaseMode mode) {
		this.iconStack = iconStack;
		this.priority = priority;
		this.mode = mode;
	}

	@Override
	public DatabaseMode getMode() {
		return mode;
	}

	@Override
	public ItemStack getIconStack() {
		return iconStack;
	}

	@Override
	public int compareTo(IDatabasePage<I> o) {
		return priority;
	}
}
