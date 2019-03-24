package com.akrambenaissi.webcrawler;

public interface Crawler {

	public TreeNode<String> crawl(String url);

	public TreeNode<String> getTree();
}
