package ks.sample.eventbased.parser;

/**
 * This enumeration contains all the states usable during the parsing of a input
 * file.
 * 
 * @author Kumar Saurabh
 *
 */
public enum EventBasedParserStates {
	/**
	 * Initial state - The state into which the parser begin.
	 */
	Init_main(0),
	/**
	 * Header part:
	 * Ignore All - all characters read into this states are ignored until a EOL has bean read.
	 */
	H_IA(1),
	/**
	 * Blank part :
	 * Ignore All - ignore all spaces and EOL characters until the beginning of a bloc.
	 */
	B_IA(2),
	/**
	 * Label part :
	 * Ignore All - ignore all character until a Scenario of Position values are not for
	 */
	L_IA(3),
	/**
	 * Label part :
	 * Generate Scenario String and Add to the List - Convert the buffer into a String and add this String into the result list.
	 */
	L_GSSAL(4),
	/**
	 * Label part :
	 * Ignore Space - Ignore all spaces and returns characters until the next character.
	 */
	L_IS(5),
	/**
	 * Label part :
	 * Add Into Buffer - Place the current character into the buffer.
	 */
	L_AIB(6),
	/**
	 * Label part :
	 * Generate Position String and Add to the list - Convert the buffer into a String and add this String  into the result list.
	 */
	L_GPSAL(7),
	/**
	 * CSV - key part :
	 * The Initial state of the CSV parsing part.
	 */
	CK_Start(8),
	/**
	 * CSV - key part :
	 * Add Into Buffer - Places the current transition into the buffer.
	 */
	CK_AIB(9),
	/**
	 * CSV - key part :
	 * Add Content Into Quotes - Add the current transition into the buffer.
	 */
	CK_ACIQ(10),
	/**
	 * CSV - key part :
	 * Generate String and Add to the List - Convert the buffer into a String and add this String to the key list.
	 */
	CK_GSAL(11),
	/**
	 * CSV - key part :
	 * Ending Quotes - A quote has been read, so just ignore it.
	 */
	CK_EQ(12),
	/**
	 * CSV - key part :
	 * Opening Quotes - A quote has been read, so just ignore it.
	 */
	CK_OQ(13),
	/**
	 * CSV - values part :
	 * Start the CSV values - Converts the buffer into a String and add this String to the keys list.
	 */
	CV_Start(14),
	/**
	 * CSV - values part :
	 * Add Into Buffer - Places the current transition into the buffer.
	 */
	CV_AIB(15),
	/**
	 * CSV - values part :
	 * Add Content Into Quotes - Add the current transition into the buffer.
	 */
	CV_ACIQ(16),
	/**
	 * CSV - values part :
	 * Generate String and Add to the Map - Converts the buffer into a String and adds this String to the result map.
	 */
	CV_GSAM(17),
	/**
	 * CSV - values part :
	 * Ending Quotes - A quote has been read, so just ignore it.
	 */
	CV_EQ(18),
	/**
	 * CSV - values part :
	 * Opening Quotes - A quote has been read, so just ignore it.
	 */
	CV_OQ(19),
	/**
	 * CSV part :
	 * End Of Block - The Unix character FF has been read and mark the current block as terminated. There is nothing more contents for this block, so content
	 * is add to the result list and this list is returned then.
	 */
	EOB(20),
	/**
	 * End in Error - and unexpected character has been read and the process stopped by throwing an exception.
	 */
	EErr(21),
	/**
	 * Final state - There is nothing to do, just go outside of the treatment.
	 */
	End(-1);

	/**
	 * The position of the state into the state matrix (y-axis)
	 */
	private int position;

	private EventBasedParserStates(int pos) {
		this.position = pos;
	}

	/**
	 * Returns the y-axis position of the element into the matrix.
	 * 
	 * @return The position of the element into the matrix
	 */
	public int getRow() {
		return this.position;
	}

}
