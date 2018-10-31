package ks.sample.eventbased.parser;

/**
 * This enumeration contains all the transition (= set of characters) readable
 * during the parsing of a output file.
 * 
 * @author Kumar Saurabh
 *
 */
public enum EventBasedParserTransitions {
	/**
	 * Represents any readable characters except colon, comma, quote and EOL.
	 */
	Char(0),
	/**
	 * Represents the ":" character.
	 */
	Colon(1),
	/**
	 * Represents a comma.
	 */
	Comma(2),
	/**
	 * Represents an End of line (1.e no more character).
	 */
	EOL(3),
	/**
	 * Represents a quote.
	 */
	Quote(4),
	/**
	 * Represents any space characters, i.e matching the reexp '\s'.
	 */
	Spaces(5),
	/**
	 * Represents a End of bloc (= a bloc means data set).
	 */
	UnixFF(6);

	/**
	 * Contains the position of the transition inside the matrix list (x-axis)
	 */
	private int position;

	private EventBasedParserTransitions(int pos) {
		this.position = pos;
	}

	/**
	 * Returns the x-axis position of the element into the matrix.
	 * 
	 * @return The position of the element into the matrix
	 */
	public int getColumn() {
		return this.position;
	}

	/**
	 * A transition represent a group of characters. This method will return the
	 * group into which the specified character is.
	 * 
	 * @param c The character for which we need the related transition.
	 * 
	 * @return The transition associates to the specified character.
	 */
	public static EventBasedParserTransitions getTransition(char c) {
		EventBasedParserTransitions res;

		switch (c) {
		case ':':
			res = EventBasedParserTransitions.Colon;
			break;

		case ',':
			res = EventBasedParserTransitions.Comma;
			break;

		case '\n':
			res = EventBasedParserTransitions.EOL;
			break;

		case '\r':
			res = EventBasedParserTransitions.EOL;
			break;

		case '\u000C':
			res = EventBasedParserTransitions.UnixFF;
			break;

		case '"':
			res = EventBasedParserTransitions.Quote;
			break;

		case ' ':
			res = EventBasedParserTransitions.Spaces;
			break;

		case '\t':
			res = EventBasedParserTransitions.Spaces;
			break;
		default:
			res = EventBasedParserTransitions.Char;
			break;
		}

		return res;

	}
}
