package org.tomvej.fmassoc.plugin.simplepruningfinder;

/**
 * Settings for simple pruning path finder. Specifies length and with limits and
 * whether optional and M:N paths should be searched.
 * 
 * @author Tomáš Vejpustek
 */
public class Settings {
	private final boolean optional, mn;
	private final int length, width;

	/**
	 * Specify all parameters.
	 * 
	 * @param optional
	 *            Search optional paths?
	 * @param mn
	 *            Search M:N paths?
	 * @param length
	 *            Path length limit.
	 * @param width
	 *            Path width limit.
	 */
	public Settings(boolean optional, boolean mn, int length, int width) {
		this.optional = optional;
		this.mn = mn;
		this.length = length;
		this.width = width;
	}

	/**
	 * Search optional paths?
	 */
	public boolean searchOptional() {
		return optional;
	}

	/**
	 * Search M:N paths?
	 */
	public boolean searchMN() {
		return mn;
	}

	/**
	 * Path length limit.
	 */
	public int getLengthLimit() {
		return length;
	}

	/**
	 * Path width limit.
	 */
	public int getWidthLimit() {
		return width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = length;
		result = prime * result + (mn ? 1231 : 1237);
		result = prime * result + (optional ? 1231 : 1237);
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Settings)) {
			return false;
		}
		Settings other = (Settings) obj;
		return length == other.length && mn == other.mn && optional == other.optional && width == other.width;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Simple pruning finder [");
		result.append("width <= ").append(width);
		result.append(", length <= ").append(length);
		if (optional) {
			result.append(", optional");
		}
		if (mn) {
			result.append(", mn");
		}
		return result.append("]").toString();
	}
}
