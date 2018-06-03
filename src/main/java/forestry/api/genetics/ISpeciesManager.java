package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.book.IEncyclopediaCategory;

/**
 * @since 5.8
 */
public interface ISpeciesManager {
	@SideOnly(Side.CLIENT)
	void registerDatabasePlugin(String rootUID, IDatabasePlugin plugin);

	@Nullable
	IDatabasePlugin getPlugin(String rootUID);

	@SideOnly(Side.CLIENT)
	void registerAlyzerPlugin(String rootUID, IAlyzerPlugin plugin);

	@Nullable
	IAlyzerPlugin getAlyzerPlugin(String rootUID);

	@SideOnly(Side.CLIENT)
	void registerEncyclopedia(String rootUID, IEncyclopediaCategory category);

	Map<String, IEncyclopediaCategory> getEncyclopediaCategories();

	@SideOnly(Side.CLIENT)
	void registerDisplayHelper(String rootUID, ISpeciesDisplayHelper helper);

	ISpeciesDisplayHelper getDisplayHelper(String rootUID);
}
