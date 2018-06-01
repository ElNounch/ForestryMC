package forestry.book.encyclopedia;

import java.util.Random;

import forestry.api.genetics.IAlleleSpecies;

public class EncyclopediaManager {
	public static final EncyclopediaManager INSTANCE = new EncyclopediaManager();
	private final String[] NAME_PREFIXES = {
		"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu",
		"Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"
	};
	private final String[] NAME_SUFFIX = {
		""
	};

	private EncyclopediaManager() {
	}

	public String getRandomName(IAlleleSpecies species){
		return NAME_PREFIXES[new Random().nextInt(NAME_PREFIXES.length)];
	}
}
