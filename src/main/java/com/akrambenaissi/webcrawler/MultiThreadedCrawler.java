package com.akrambenaissi.webcrawler;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedCrawler implements Crawler {

	private TreeNode<String> tree = new TreeNode<String>(null);

	@Override
	public TreeNode<String> crawl(String url) {
		if (tree.data == null) {
			tree.data = url;
		}
		TreeNode<String> links = new TreeNode<>(null);
		CrawlerThread worker = new CrawlerThread(url, tree);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		executorService.execute(worker);
		Set<String> unvisited = CrawlerThread.getUnvisited();
		for (String unvisitedLink : unvisited) {
			Runnable crawlerThread = new CrawlerThread(unvisitedLink, links);
			executorService.execute(crawlerThread);
		}
		executorService.shutdown();
		return tree;
	}

	@Override
	public TreeNode<String> getTree() {
		// TODO Auto-generated method stub
		return null;
	}

}
