package com.akrambenaissi.webcrawler;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import net.codestory.http.WebServer;

@QuarkusTest
public class CrawlTest {

	private static final Object HTML_ONE_LINK = "<html><head></head><body><a href=\"/link1\">first link</a></body></html>";
	private static final Object HTML_TWO_LINKS = "<html><head></head><body><a href=\"/link1\">link 1</a></body><a href=\"/link2\">link 2</a></body></html>";
	private static final Object HTML_NESTED_LINKS = "<html><head></head><body><a href=\"/link1\">link 1</a></body><a href=\"/link2\">link 2</a></body></html>";
	private static final Object HTML_NESTED_LINKS_SUB = "<html><head></head><body><a href=\"link1/link1.1\">link 1</a></body><a href=\"/link2\">link 2</a></body></html>";

	@Test
	public void testDefaultEndpointWithNullUrl() {
		given().when().get("/").then().statusCode(406);
	}

	@Test
	public void testDefaultEndpointWithEmptyUrl() {
		given().when().get("/?url=").then().statusCode(406);
	}

	@Test
	public void testDefaultEndpointWithEmptyURLS() {
		WebServer ws = new WebServer(routes -> routes.get("/", "Hello"));
		int port = ws.startOnRandomPort().port();
		given().when().get("/?url=http://localhost:" + port).then().statusCode(200);
		ws.stop();
	}

	@Test
	public void testDefaultEndpointWithOneLink() {
		WebServer ws = new WebServer(routes -> routes.get("/", HTML_ONE_LINK));
		int port = ws.startOnRandomPort().port();
		given().when().get("/?url=http://localhost:" + port).then().statusCode(200).and().body(containsString("link1"));
		ws.stop();
	}

	@Test
	public void testDefaultEndpointWithTwoLink() {
		WebServer ws = new WebServer(routes -> routes.get("/", HTML_TWO_LINKS));
		int port = ws.startOnRandomPort().port();
		given().when().get("/?url=http://localhost:" + port).then().statusCode(200).and().body(containsString("/link1"))
				.and().body(containsString("/link2"));
		// ws.stop();
	}

	@Test
	public void testDefaultEndpointWithNestedLinksAndDepthAtOne() {
		WebServer ws = new WebServer(routes -> routes.get("/", HTML_NESTED_LINKS).get("/link1", HTML_NESTED_LINKS_SUB));
		int port = ws.startOnRandomPort().port();
		String url = "/?url=http://localhost:" + port + "&depth=1";
		given().when().get(url).then().statusCode(200).and().body(containsString("/link1")).and()
				.body(containsString("/link2")).and();
		// .body(not(containsString("/link1.1")));
		// ws.stop();
	}

	@Test
	public void testDefaultEndpointWithNestedLinksAndDepthAtTwo() {
		WebServer ws = new WebServer(routes -> routes.get("/", HTML_NESTED_LINKS).get("/link1", HTML_NESTED_LINKS_SUB));
		int port = ws.startOnRandomPort().port();
		String url = "/?url=http://localhost:" + port + "&depth=2";
		given().when().get(url).then().statusCode(200).and().body(containsString("/link1")).and()
				.body(containsString("/link2")).and().body(containsString("link1/link1.1"));
		// ws.stop();
	}

}
