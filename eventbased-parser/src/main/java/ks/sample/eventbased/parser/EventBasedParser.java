package ks.sample.eventbased.parser;

import static ks.sample.eventbased.parser.EventBasedParserStates.Init_main;
import static ks.sample.eventbased.parser.EventBasedParserStates.B_IA;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_ACIQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_AIB;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_EQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_GSAL;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_OQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CK_Start;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_ACIQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_AIB;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_EQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_GSAM;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_OQ;
import static ks.sample.eventbased.parser.EventBasedParserStates.CV_Start;
import static ks.sample.eventbased.parser.EventBasedParserStates.EErr;
import static ks.sample.eventbased.parser.EventBasedParserStates.EOB;
import static ks.sample.eventbased.parser.EventBasedParserStates.End;
import static ks.sample.eventbased.parser.EventBasedParserStates.H_IA;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_AIB;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_GPSAL;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_IA;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_IS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class EventBasedParser implements Parser<Map<String, String>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(EventBasedParser.class);
	
	private static final EventBasedParserStates[][] STATES_MATRIX = new EventBasedParserStates[][] {
		/* States			Char		Colon		Comma		EOL			Quote		Spaces		UnixFFf  */
		
					 // 	THIS PART ALLOWS TO PARSE THE FIRST HEADER LINE OF GIVEN FORMAT : "BasicsScript" :
		/* Init_main */		{H_IA,		H_IA,		H_IA,		H_IA,		H_IA,		H_IA,		EErr},
		/* H_IA		 */		{H_IA,		H_IA,		H_IA,		B_IA,		H_IA,		H_IA,		EErr},
		
					 //		THIS PART JUST IGNORE BLANK LINE BEETWEEN "basincScript" and a Blocks or between two Blocks 
		/* B_IA		 */		{L_IA,		EErr,		EErr,		B_IA,		EErr,		B_IA,		EErr},
		
					//		THIS PART ALLOWS TO GET THE VALUE OF THE TWO LABELS "Scenario" and "Position":
		/* L_IA		*/ 		{L_IA,		L_IS,		L_IA,		L_IA,		L_IA,		L_IA,		EErr},
		/* L_GSSAL	*/ 		{L_IA,		L_IS,		L_IA,		L_IA,		L_IA,		L_IA,		EErr},
		/* L_IS		*/ 		{L_AIB,		EErr,		EErr,		EErr,		EErr,		L_IS,		EErr},
		/* L_AIB	*/ 		{L_AIB,		L_AIB,		L_GPSAL,	L_GPSAL,	L_AIB,		L_AIB,		EErr},
		/* L_GPSAL	*/ 		{EErr,		EErr,		EErr,		CK_Start,	EErr,		EErr,		EErr},
		
					//		THIS PART READS AND STORES ALL THE KEYS OF THE CSV PART OF THE GIVEN FORMAT:
		/* CK_Start	*/ 		{CK_AIB,		CK_AIB,		CK_GSAL,		CV_Start,	CK_OQ,		CK_AIB,		EErr},
		/* CK_AIB	*/ 		{CK_AIB,		CK_AIB,		CK_GSAL,		CV_Start,	EErr,		CK_AIB,		EErr},
		/* CK_ACIQ	*/ 		{CK_ACIQ,		CK_ACIQ,	CK_ACIQ,		EErr,		CK_EQ,		CK_ACIQ,	EErr},
		/* CK_GSAL	*/ 		{CK_AIB,		CK_AIB,		CK_GSAL,		CV_Start,	CK_OQ,		CK_AIB,		EErr},
		/* CK_EQ	*/ 		{EErr,			EErr,		CK_GSAL,		CV_Start,	EErr,		EErr,		EErr},
		/* CK_OQ	*/ 		{CK_ACIQ,		CK_ACIQ,	CK_ACIQ,		EErr,		CK_EQ,		CK_ACIQ,	EErr},
		
					//		THIS PART ASSOCIATES THE KEYS PREVIOUSLY READ TO THE VALUE OF THE SECOND CSV ROW OF GIVEN FORMAT:
		/* CV_Start	*/ 		{CV_AIB,		CV_AIB,		CV_GSAM,		CV_GSAM,	CV_OQ,		CV_AIB,		EErr},
		/* CV_AIB	*/ 		{CV_AIB,		CV_AIB,		CV_GSAM,		CV_GSAM,	EErr,		CV_AIB,		EErr},
		/* CV_ACIQ	*/ 		{CV_ACIQ,		CV_ACIQ,	CV_ACIQ,		EErr,		CV_EQ,		CV_ACIQ,	EErr},
		/* CV_GSAL	*/ 		{CV_AIB,		CK_AIB,		CV_GSAM,		CV_GSAM,	CV_OQ,		CV_AIB,		EOB},
		/* CV_EQ	*/ 		{EErr,			EErr,		CV_GSAM,		CV_GSAM,	EErr,		EErr,		EErr},
		/* CV_OQ	*/ 		{CV_ACIQ,		CV_ACIQ,	CV_ACIQ,		EErr,		CV_EQ,		CV_ACIQ,	EErr},
		
					//		THIS PART CONTAIN BOTH ERROR STATES AND FINAL STATES (THIS AUTOMATE IS COMPLETE, SO ALL TRANSITION ARE MANAGED) :
		/* EOB		*/		{EErr,		EErr,		EErr,		B_IA,		End,		B_IA,		EErr},
		/* EErr		*/		{End,		End,		End,		End,		End,		End,		End}
	};
	
	/**
	 * The number of characters that this buffer is able to handle in memory.
	 */
	private static final int BUFFER_SIZE = 8 * 1000 * 1000;
	
	/**
	 * To check 
	 */
	private static final int MIN_BLOCK_SIZE = 20;
	
	/**
	 * The reader used for file reading.
	 */
	private InputStreamReader reader;
	
	/**
	 * The buffer containing all read characters directly from file.
	 */
	private char[] buffer;
	
	/**
	 * The number of character placed into the buffer during execution of a file reading.
	 */
	private int nbCharsIntoBuffer;
	
	/**
	 * The position of the character currently read into the buffer.
	 */
	private int positionIntoBuffer;
	
	/**
	 * This array contains the {@link String} contained into on block. This is the same
	 * array which is reused each time the next method is called.
	 */
	private final LinkedList<String> keyList;
	
	/**
	 * Contains the all the value of one block.
	 */
	private final HashMap<String, String> resultMap;
	
	/**
	 * The number of characters contains into the read file.
	 */
	private long algoFileSize;
	
	/**
	 * The total number of characters already read into this files.
	 */
	private long totalNbReadChars;
	
	/**
	 * The current state into which is this parser.
	 */
	private EventBasedParserStates currentState;
	
	public EventBasedParser(String filePath, int defaultMapCapacity) throws FileNotFoundException {
		//Initialization of the file reader
		if(filePath == null) {
			throw new IllegalArgumentException("Impossible to read null file path");
		}
		
		File f = new File(filePath);
		
		if (f.exists() && f.isFile()) {
			this.algoFileSize = f.length();
		}
		System.out.println("Loading file { "+filePath+" }");
		this.reader = new InputStreamReader(new FileInputStream(filePath), Charset.forName("ISO-8859-1"));
		this.buffer = new char[EventBasedParser.BUFFER_SIZE];
		
		//Initialization of the parser
		this.keyList = new LinkedList<>();
		this.resultMap = new HashMap<>(defaultMapCapacity, 1.0f);
		this.currentState = Init_main;
	}
	
	/**
	 * Add the content of the specified {@link StringBuilder} into the keys list then call the clear method.
	 * @param buffer
	 */
	private void addBufferIntoTheListThenCleatIt(StringBuilder buffer) {
		this.keyList.addLast(buffer.toString());
		this.clearLocalBuffer(buffer);
	}

	private void addBufferIntoTheMapThenCleatIt(String key, StringBuilder buffer) {
		this.resultMap.put(key, buffer.toString());
		this.clearLocalBuffer(buffer);
	}
	
	public void close() {
		try {
			if (this.reader != null) {
				this.reader.close();
			}
		} catch (IOException ex) {
			this.reader = null;
			LOGGER.error("Unable to close correctly the stream to the given file to read",ex);
		}
	}
	
	private void clearLocalBuffer(StringBuilder buffer) {
		buffer.delete(0, buffer.length());		
	}
	
	private EventBasedParserStates getNextState(EventBasedParserTransitions transition) {
		return EventBasedParser.STATES_MATRIX[this.currentState.getRow()][transition.getColumn()];
	}
	
	private void handleError(String errorMessage) {
		LOGGER.error(errorMessage);
		throw new IllegalStateException(errorMessage);
	}
	
	@Override
	public boolean hasNext() {
	return this.reader !=null && (this.algoFileSize - this.totalNbReadChars > MIN_BLOCK_SIZE);
	}
	
	@Override
	public Map<String, String> next(){
		// Reset the map to return:
		this.resultMap.clear();
		
		//Definition of working variables :
		char currentChar;
		StringBuilder builder = new StringBuilder(100);
		
		while(!EOB.equals(this.currentState)) {
			this.populateBuffer();
			currentChar = this.buffer[this.positionIntoBuffer++];
			System.out.print(currentChar);
			this.currentState = this.getNextState(EventBasedParserTransitions.getTransition(currentChar));
			this.process(builder, currentChar, this.currentState);
			
			this.totalNbReadChars++;
		}
	
	
	//After each processed block (EOB state), the current parser state becomes the
	//B_IA state (waiting state for blank between block).
		this.currentState = B_IA;
		
		return this.resultMap;
	}
	
	private void populateBuffer() {
		if (this.nbCharsIntoBuffer <= this.positionIntoBuffer) {
			try {
				this.positionIntoBuffer = 0;
				this.nbCharsIntoBuffer = this.reader.read(this.buffer, 0, BUFFER_SIZE);
			}catch(IOException ex) {
				this.nbCharsIntoBuffer = -1;
			}
		}
		
	}
	
	private void process(StringBuilder buffer, char currentChar, EventBasedParserStates state) {
		switch(state) {
		//All the 5 following statements get char and append it into the buffer
		case L_AIB:
		case CK_AIB:
		case CK_ACIQ:
		case CV_AIB:
		case CV_ACIQ:
			buffer.append(currentChar);
			break;
		case CK_GSAL:
		case CV_Start:
			this.addBufferIntoTheListThenCleatIt(buffer);
			break;
		case L_GSSAL:
			this.addBufferIntoTheMapThenCleatIt("Scenario", buffer);
			break;
		case L_GPSAL:
			this.addBufferIntoTheMapThenCleatIt("Position", buffer);
			break;
		case CV_GSAM:
			this.addBufferIntoTheMapThenCleatIt(this.keyList.poll(), buffer);
			break;
		case EErr:
			this.handleError("Parsing error. An unexpected character has been read from the current state. Check Alog input file.");
			break;
			
			default:
				//Nothing to do for all the states.
				break;
		}
	}
	
}