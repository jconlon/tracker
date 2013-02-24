package com.verticon.tracker.trigger.view.views;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newLinkedList;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.verticon.tracker.trigger.view.TriggerViewPlugin;
import com.verticon.tracker.trigger.view.preferences.PreferenceConstants;

public class LotWizard extends Wizard {

	protected LotPageOneID one;
	protected LotPageTwoDate two;
	protected LotPageThreeSuffix three;
	final Text lotText;
	private final List<String> currentValues;
	private String lotSeparator;



	public LotWizard(Text lotText) {
		super();
		setNeedsProgressMonitor(true);
		this.lotText = lotText;
		currentValues = init(lotText.getText());
	}

	private List<String> init(String values) {
		List<String> result;

		if (!isNullOrEmpty(values)) {
			IPreferenceStore store = TriggerViewPlugin.getDefault()
					.getPreferenceStore();
			lotSeparator = store
					.getString(PreferenceConstants.P_LOT_SEPARATOR_STRING);
			if (isNullOrEmpty(lotSeparator)) {
				result = newLinkedList();
				result.add(values);
			}
			Iterable<String> elements = Splitter.on(lotSeparator).trimResults()
					.split(values);

			result = ImmutableList.copyOf(newLinkedList(elements));

		} else {
			result = Collections.emptyList();
		}
		return result;

	}

	@Override
	public void addPages() {
		one = new LotPageOneID();
		two = new LotPageTwoDate();
		three = new LotPageThreeSuffix();
		addPage(one);
		addPage(two);
		addPage(three);
	}

	@Override
	public boolean performFinish() {

		StringBuilder builder = new StringBuilder();
		builder.append(one.getText1());
		if (!isNullOrEmpty(two.getText1())) {
			builder.append(lotSeparator).append(two.getText1());
		}
		if (!isNullOrEmpty(three.getText1())) {
			builder.append(lotSeparator).append(three.getText1());
		}

		lotText.setText(builder.toString());
		return true;
	}

	/**
	 * @return the currentValues
	 */
	List<String> getCurrentValues() {
		return currentValues;
	}

}
