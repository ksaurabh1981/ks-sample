package ks.sample.eventbased.parser;

import java.util.Map;

public interface Parser<T> {

	public boolean hasNext();
	public Map<String, String> next();
}
