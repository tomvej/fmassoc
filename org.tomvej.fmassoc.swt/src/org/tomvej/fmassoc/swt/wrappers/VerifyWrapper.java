package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Wrapper of verify listener. Verifies wrt new text.
 * 
 * @author Tomáš Vejpustek
 */
public class VerifyWrapper implements VerifyListener {
	private final Predicate<String> checker;

	/**
	 * Specify verifier of the new text.
	 */
	public VerifyWrapper(Predicate<String> checker) {
		this.checker = Validate.notNull(checker);
	}

	@Override
	public void verifyText(VerifyEvent e) {
		String oldText = ((Text) e.getSource()).getText();
		if (!checker.test(oldText.substring(0, e.start) + e.text + oldText.substring(e.end))) {
			e.doit = false;
		}
	}
}
