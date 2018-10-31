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
		/* States			Char			Colon			Comma			EOL				Quote			Spaces		UnixFFf  */
		
					 // 	THIS PART ALLOWS TO PARSE THE FIRST HEADER LINE OF GIVEN FORMAT : "BasicsScript" :
		/* Init_main */		{H_IA,			H_IA,			H_IA,			H_IA,			H_IA,			H_IA,		EErr},
		/* H_IA		 */		{H_IA,			H_IA,			H_IA,			B_IA,			H_IA,			H_IA,		EErr},
		
					 //		THIS PART JUST IGNORE BLANK LINE BEETWEEN "basincScript" and a Blocks or between two Blocks 
		/* B_IA		 */		{L_IA,			EErr,			EErr,			B_IA,			EErr,			B_IA,		EErr},
		
		/* L_IA		*/ 		{}
	};
}
