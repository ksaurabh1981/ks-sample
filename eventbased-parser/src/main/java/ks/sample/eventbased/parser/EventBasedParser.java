package ks.sample.eventbased.parser;

import static ks.sample.eventbased.parser.EventBasedParserStates.H_IA;
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
import static ks.sample.eventbased.parser.EventBasedParserStates.B_IA;
import static ks.sample.eventbased.parser.EventBasedParserStates.Init_main;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_AIB;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_GPSAL;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_GSSAL;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_IA;
import static ks.sample.eventbased.parser.EventBasedParserStates.L_IS;

import java.util.Map;

public class EventBasedParser implements Parser<Map<String, String>>{

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
}
