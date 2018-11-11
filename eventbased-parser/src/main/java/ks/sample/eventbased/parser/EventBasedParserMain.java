package ks.sample.eventbased.parser;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

public class EventBasedParserMain {

	public static void main(String[] args) {
		String filePath = "input.csv";
		URL url = EventBasedParserMain.class.getClassLoader().getResource(filePath);
		
		System.out.println(url.getPath());
		try {
			EventBasedParser parser = new EventBasedParser(url.getPath(), 20);
			while (parser.hasNext()) {
				Map<String, String> map = parser.next();
				System.out.println(map);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
