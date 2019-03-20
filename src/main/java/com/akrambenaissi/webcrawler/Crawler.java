package com.akrambenaissi.webcrawler;

import java.util.Set;

public interface Crawler {

	public Set<String> crawl(String url);
}
