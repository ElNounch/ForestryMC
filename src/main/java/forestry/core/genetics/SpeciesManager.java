package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.book.IEncyclopediaCategory;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.IDatabasePlugin;
import forestry.api.genetics.ISpeciesDisplayHelper;
import forestry.api.genetics.ISpeciesManager;
import forestry.api.genetics.ISpeciesRoot;
import forestry.api.genetics.ISpeciesType;

public class SpeciesManager implements ISpeciesManager {
	public static final SpeciesManager INSTANCE = new SpeciesManager();

	private final Map<String, IDatabasePlugin> databaseByUID = new HashMap<>();
	private final Map<String, IAlyzerPlugin> alyzerByUID = new HashMap<>();
	private final Map<String, IEncyclopediaCategory> encyclopediaByUID = new HashMap<>();
	private final Map<String, ISpeciesDisplayHelper> displayHelperByUID = new HashMap<>();

	private SpeciesManager() {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerDatabasePlugin(String rootUID, IDatabasePlugin plugin) {
		databaseByUID.put(rootUID, plugin);
	}

	@Nullable
	public IDatabasePlugin getPlugin(String rootUID){
		return databaseByUID.get(rootUID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerAlyzerPlugin(String rootUID, IAlyzerPlugin plugin) {
		alyzerByUID.put(rootUID, plugin);
	}

	@Nullable
	public IAlyzerPlugin getAlyzerPlugin(String rootUID){
		return alyzerByUID.get(rootUID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerEncyclopedia(String rootUID, IEncyclopediaCategory category) {
		encyclopediaByUID.put(rootUID, category);
	}

	@Nullable
	public IEncyclopediaCategory getEncyclopediaCategory(String rootUID){
		return encyclopediaByUID.get(rootUID);
	}

	public Map<String, IEncyclopediaCategory> getEncyclopediaCategories() {
		return encyclopediaByUID;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerDisplayHelper(String rootUID, ISpeciesDisplayHelper helper) {
		displayHelperByUID.put(rootUID, helper);
	}

	public ISpeciesDisplayHelper getDisplayHelper(String rootUID){
		ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(rootUID);
		if(root == null){
			return FakeSpeciesDisplayHelper.INSTANCE;
		}
		return displayHelperByUID.computeIfAbsent(rootUID, key -> new SpeciesDisplayHelper(root));
	}

	private static class FakeSpeciesDisplayHelper implements ISpeciesDisplayHelper{

		public static final FakeSpeciesDisplayHelper INSTANCE = new FakeSpeciesDisplayHelper();

		private FakeSpeciesDisplayHelper() {
		}

		@Override
		public ItemStack getDisplayStack(IAlleleSpecies species, ISpeciesType type) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack getDisplayStack(IAlleleSpecies species) {
			return ItemStack.EMPTY;
		}
	}
}
