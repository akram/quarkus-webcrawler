package com.akrambenaissi.webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleCrawler implements Crawler {

	private static final int MAX_DEPTH = 256;
	private static java.util.logging.Logger LOGGER = Logger.getLogger(SimpleCrawler.class.getName());
	private Set<String> vistedUrls = new HashSet<String>();
	private Set<String> links = new HashSet<String>();
	String baseUri = null;
	private Map<String, String> cookies;
	private int maxDepth;

	public SimpleCrawler(String url, int maxDepth) throws MalformedURLException {
		this.baseUri = extractBaseUri(url);
		this.maxDepth = maxDepth;

	}

	@Override
	public Set<String> crawl(String url) {
		return this.crawl(url, 0);
	}

	private Set<String> crawl(String url, int currentDepth) {
		LOGGER.info("Crawling went to depth: " + currentDepth + " [" + url + "]");
		if ((!vistedUrls.contains(url) && (currentDepth < this.maxDepth) && (currentDepth < MAX_DEPTH))) {
			try {
				vistedUrls.add(url);
				Connection connection = Jsoup.connect(url);
				if (this.cookies != null) {
					connection.cookies(cookies);
				} else {
					this.cookies = connection.response().cookies();
				}

				Document document = connection.get();
				Elements linksOnPage = document.select("a[href]");
				for (Element page : linksOnPage) {
					String link = page.attr("abs:href");
					if (extractBaseUri(this.baseUri).equals(extractBaseUri(link))) {
						links.add(page.attr("href"));
						crawl(link, currentDepth + 1);
					}
				}
				currentDepth++;
			} catch (IOException e) {
				// LOGGER.info("For '" + URL + "': " + e.getCause().getMessage());
			}
		} else {
			LOGGER.info("URL [" + url + "] already visited ");
		}

		return links;
	}

	private String extractBaseUri(String url) throws MalformedURLException {
		URL URL = new URL(url);
		return URL.getProtocol() + "://" + URL.getHost() + ":" + URL.getPort();
	}

}
