package lecho.lib.hellocharts.formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.util.Utils;

public class SimpleAxisValueFormatter implements AxisValueFormatter {
	protected static final int DEFAULT_DIGITS_NUMBER = 0;

	private int decimalDigitsNumber = DEFAULT_DIGITS_NUMBER;
	private char[] appendedText = new char[0];
	private char[] prependedText = new char[0];
	private char decimalSeparator = '.';

	public SimpleAxisValueFormatter() {
		NumberFormat numberFormat = NumberFormat.getInstance();
		if (numberFormat instanceof DecimalFormat) {
			decimalSeparator = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
		}
	}

	public int getDecimalDigitsNumber() {
		return decimalDigitsNumber;
	}

	/**
	 * Sets number of digits after comma, used only for manual axes, this value will not be used for auto-generated axes.
	 */
	public SimpleAxisValueFormatter setDecimalDigitsNumber(int decimalDigitsNumber) {
		this.decimalDigitsNumber = decimalDigitsNumber;
		return this;
	}

	public char[] getAppendedText() {
		return appendedText;
	}

	public SimpleAxisValueFormatter setAppendedText(char[] appendedText) {
		if (null != appendedText) {
			this.appendedText = appendedText;
		}
		return this;
	}

	public char[] getPrependedText() {
		return prependedText;
	}

	public SimpleAxisValueFormatter setPrependedText(char[] prependedText) {
		if (null != prependedText) {
			this.prependedText = prependedText;
		}
		return this;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	public SimpleAxisValueFormatter setDecimalSeparator(char decimalSeparator) {
		char nullChar = '\0';
		if (nullChar != decimalSeparator) {
			this.decimalSeparator = decimalSeparator;
		}
		return this;
	}

	@Override
	public int formatValueForManualAxis(char[] formattedValue, AxisValue axisValue) {
		final char[] label = axisValue.getLabel();
		if (null != label) {
			// If custom label is not null use label's characters as formatted value.
			// Copy label into formatted value array.
			System.arraycopy(label, 0, formattedValue, formattedValue.length - label.length, label.length);
			return label.length;
		}

		//When there is no custom label
		final int charsNumber = formatFloatValue(formattedValue, axisValue.getValue(), decimalDigitsNumber);
		appendText(formattedValue);
		prependText(formattedValue, charsNumber);
		return charsNumber + prependedText.length + appendedText.length;
	}

	@Override
	public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int decimalDigits) {
		final int charsNumber = formatFloatValue(formattedValue, value, decimalDigits);
		appendText(formattedValue);
		prependText(formattedValue, charsNumber);
		return charsNumber + prependedText.length + appendedText.length;
	}

	protected int formatFloatValue(char[] formattedValue, float value, int decimalDigits) {
		return Utils.formatFloat(formattedValue, value, formattedValue.length - appendedText.length, decimalDigits, decimalSeparator);
	}

	protected void appendText(char[] formattedValue){
		if (appendedText.length > 0) {
			System.arraycopy(appendedText, 0, formattedValue, formattedValue.length - appendedText.length,
					appendedText.length);
		}
	}

	protected void prependText(char[] formattedValue, int charsNumber){
		if (prependedText.length > 0) {
			System.arraycopy(prependedText, 0, formattedValue, formattedValue.length - charsNumber - appendedText.length
					- prependedText.length, prependedText.length);
		}
	}
}
