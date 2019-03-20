package com.akrambenaissi.webcrawler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedCrawler implements Crawler {

	@Override
	public Set<String> crawl(String url) {
		Set<String> links = new HashSet<>();
		CrawlerThread worker = new CrawlerThread(url, links);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		executorService.execute(worker);
		Set<String> unvisited = CrawlerThread.getUnvisited();
		for (String unvisitedLink : unvisited) {
			Runnable crawlerThread = new CrawlerThread(unvisitedLink, links);
			executorService.execute(crawlerThread);
		}
		executorService.shutdown();
		return links;
	}

}
