package com.akrambenaissi.webcrawler;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerThread implements Runnable {
	private static java.util.logging.Logger LOGGER = Logger.getLogger(CrawlerThread.class.getName());

	private Set<String> visited = new ConcurrentSkipListSet<String>();
	private static Set<String> unvisited = new ConcurrentSkipListSet<String>();

	private String url;
	private Set<String> links;

	public CrawlerThread(String url, Set<String> links) {
		this.url = url;
		this.links = links;
	}

	@Override
	public void run() {
		visitPage(url);
	}

	private void visitPage(String url) {
		synchronized (this) {
			visited.add(url);
			unvisited.remove(url);
		}
		if (!url.contains("javascript") && !url.contains("#")) {
			try {
				Connection connection = Jsoup.connect(url);
				Document doc = connection.timeout(0).get();
				Elements linkTags = doc.select("a[href]");
				for (Element e : linkTags) {
					String link = e.attr("abs:href");
					if (!link.contains("#") && !link.contains("javascript") && !link.equals(url)) {
						if (link.startsWith("http") || link.startsWith("www")) {
							if (link.contains(url)) {
								links.add(link);
								unvisited.add(link);
							}
						} else if (link.startsWith("/")) {
							links.add(link);
							unvisited.add(link);
						}
					}
				}
				LOGGER.info("Links found in [" + url + "] : " + links);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public static Set<String> getUnvisited() {
		return unvisited;
	}

}