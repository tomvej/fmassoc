package org.tomvej.fmassoc.plugin.simplepruningfinder;

import java.util.ArrayList;
import java.util.List;

import org.tomvej.fmassoc.plugin.prioritydfpathfinder.IteratedPriorityDFFinderProvider;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;

/**
 * Path finder provider. Specifies pruning iteration.
 * 
 * @author Tomáš Vejpustek
 */
public class SimplePathFinderProvider extends IteratedPriorityDFFinderProvider {

	/**
	 * Specify settings.
	 */
	public SimplePathFinderProvider(Settings settings) {
		super(generatePruning(settings));
	}

	private static List<Pruning> generatePruning(Settings settings) {
		List<Pruning> result = new ArrayList<>();
		for (int width = 0; width <= settings.getWidthLimit(); width++) {
			for (int length = 3; length < settings.getLengthLimit(); length += 3) {
				addPruningsForLength(result, width, length, settings.searchOptional(), settings.searchMN());
			}
			addPruningsForLength(result, width, settings.getLengthLimit(), settings.searchOptional(), settings.searchMN());
		}
		return result;
	}

	private static void addPruningsForLength(List<Pruning> result, int width, int length, boolean optional, boolean nm) {
		result.add(new SimplePruning(new Settings(false, false, length, width)));
		if (optional) {
			result.add(new SimplePruning(new Settings(true, false, length, width)));
		}
		if (nm) {
			result.add(new SimplePruning(new Settings(false, true, length, width)));
			if (optional) {
				result.add(new SimplePruning(new Settings(true, true, length, width)));
			}
		}
	}

}
