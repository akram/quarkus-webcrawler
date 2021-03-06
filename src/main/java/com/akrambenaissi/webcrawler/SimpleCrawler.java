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

	private static final String SRC_ATTRIBUTE = "src";
	private static final String FRAME_QUERY = "frame";
	private static final String HREF_ATTRIBUTE = "href";
	private static final String A_HREF_QUERY = "a[href]";
	private static final int MAX_DEPTH = 256;
	private static java.util.logging.Logger LOGGER = Logger.getLogger(SimpleCrawler.class.getName());
	private Set<String> vistedUrls;
	private Set<String> links = new HashSet<String>();
	private TreeNode<String> tree;
	String baseUri = null;
	private Map<String, String> cookies;
	private int maxDepth;

	public SimpleCrawler(String url, int maxDepth) throws MalformedURLException {
		this.baseUri = extractBaseUri(url);
		this.maxDepth = maxDepth;
		this.tree = new TreeNode<String>(url);
		this.vistedUrls = new HashSet<String>();

	}

	@Override
	public TreeNode<String> crawl(String url) {
		return this.crawl(url, 0, new HashSet<String>());
	}

	/**
	 * @param url
	 * @param currentDepth
	 * @return
	 */
	private TreeNode<String> crawl(String url, int currentDepth, Set<String> visited) {
		LOGGER.info("Crawling went to depth: " + currentDepth + " [" + url + "]");
		this.vistedUrls.addAll(visited);
		if (!vistedUrls.contains(url) && currentDepth < this.maxDepth && currentDepth < MAX_DEPTH) {
			try {
				vistedUrls.add(url);
				Connection connection = Jsoup.connect(url);
				if (this.cookies != null) {
					connection.cookies(cookies);
				} else {
					this.cookies = connection.response().cookies();
				}
				Document document = connection.get();
				crawl(currentDepth, document, A_HREF_QUERY, HREF_ATTRIBUTE, this.vistedUrls);
				crawl(currentDepth, document, FRAME_QUERY, SRC_ATTRIBUTE, this.vistedUrls);
				currentDepth++;
			} catch (IOException e) {
				// LOGGER.info("For '" + URL + "': " + e.getCause().getMessage());
			}
		} else {
			LOGGER.info("URL [" + url + "] already visited ");
		}
		return tree;
	}

	private void crawl(int currentDepth, Document document, String cssQuery, String attributeKey, Set<String> visited)
			throws MalformedURLException {
		this.vistedUrls.addAll(visited);
		Elements linksOnPage = document.select(cssQuery);
		for (Element page : linksOnPage) {
			String absoluteLink = page.absUrl(attributeKey);
			String rootBaseUri = extractBaseUri(this.baseUri);
			String linkBaseUri = extractBaseUri(absoluteLink);
			if (rootBaseUri.equals(linkBaseUri)) { // if on the same domain
				String link = page.attr(attributeKey);
				links.add(link);
				tree.addChild(link);
				crawl(absoluteLink, currentDepth + 1, visited);
			}
		}
	}

	public TreeNode<String> getTree() {
		return tree;
	}

	private String extractBaseUri(String url) throws MalformedURLException {
		URL URL = new URL(url);
		return URL.getProtocol() + "://" + URL.getHost() + ":" + URL.getPort();
	}

}
