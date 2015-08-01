package org.tomvej.fmassoc.model.path;

/**
 * Defines {@link #equals(Object)} as equality of comprising associations (in
 * sequence). {@link #hashCode()} is defined accordingly.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public abstract class AbstractPath implements Path {

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Path)) {
			return false;
		}
		return getAssociations().equals(((Path) obj).getAssociations());
	}

	@Override
	public int hashCode() {
		return getAssociations().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() +
				"[" + getSource().getName() + " -> " + getDestination().getName() + "; "
				+ getLength() + "]";
	}
}
