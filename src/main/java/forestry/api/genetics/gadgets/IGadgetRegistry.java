package forestry.api.genetics.gadgets;

import java.util.Collection;

public interface IGadgetRegistry {
	void addEncyclopediaPage(String rootUID, IDatabasePage page);

	Collection<IDatabasePage> getEncyclopediaPage(String rootUID);

	void addAnalyzerPage(String rootUID, IDatabasePage page);

	Collection<IDatabasePage> getAnalyzerPagePage(String rootUID);

	void registerAnalyzerPlugin(String rootUID, IAnalyzerPlugin plugin);

	IAnalyzerPlugin getAnalyzerPlugin(String rootUID);
}
