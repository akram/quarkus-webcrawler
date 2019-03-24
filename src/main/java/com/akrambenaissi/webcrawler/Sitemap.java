package com.akrambenaissi.webcrawler;

import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
public class Sitemap {
	private static java.util.logging.Logger LOGGER = Logger.getLogger(Sitemap.class.getName());
	String baseUri = null;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response get(@QueryParam("url") String url, @QueryParam("depth") @DefaultValue("1") Integer maxDepth)
			throws InterruptedException, MalformedURLException {
		LOGGER.info("Asking sitemap for : [" + url + "]" + "Depth: " + maxDepth);
		if (url == null || "".equals(url)) {
			String message = "URL parameter: \"url\" cannot be null or empty";
			LOGGER.severe(message);
			return Response.serverError().status(Status.NOT_ACCEPTABLE).build();
		}

//		Crawler crawler = new SimpleCrawler(url, maxDepth);
		Crawler crawler = new MultiThreadedCrawler();
		TreeNode<String> result = crawler.crawl(url);
		System.out.println("RESULT AS TREE: \n " + crawler.getTree() + "\n/ END OF RESULT AS TREE");
		return Response.ok(crawler.getTree()).build();

	}

}