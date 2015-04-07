package org.tomvej.fmassoc.swt.wrappers;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**
 * Blocks key events.
 * 
 * @author Tomáš Vejpustek
 */
public class KeyEventBlocker extends KeyAdapter {
	private final Predicate<KeyEvent> filter;

	/**
	 * Specify when events are blocked (predicate returns {@code true}).
	 */
	public KeyEventBlocker(Predicate<KeyEvent> filter) {
		this.filter = Validate.notNull(filter);
	}

	/**
	 * Specify keys which are blocked.
	 */
	public KeyEventBlocker(int... blockedKeys) {
		this(Arrays.asList(ArrayUtils.toObject(blockedKeys)));
	}

	/**
	 * Specify keys which are blocked.
	 */
	public KeyEventBlocker(Collection<Integer> blockedKeys) {
		Validate.notNull(blockedKeys);
		filter = e -> blockedKeys.contains(e.keyCode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (filter.test(e)) {
			e.doit = false;
		}
	}
}
