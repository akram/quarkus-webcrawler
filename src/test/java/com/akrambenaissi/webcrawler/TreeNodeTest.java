package com.akrambenaissi.webcrawler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TreeNodeTest {

	private static final String TWO_SUBLEVEL_SIMPLE = "foobar.com/baz\n  - bar/\n    - xyz\n    - abc\n  - mno\n    - about\n";
	private static final String EMPTY_CHILD = "  - contacts\n";
	private static final String HAS_MORE_LINKS = "    - has\n    - more links\n    - more links\n    - more links\n";

	@Test
	public void testTreeNodeHasMoreLinks() {

		TreeNode<String> root = new TreeNode<>("foobar.com/baz");
		TreeNode<String> ROOT = root;
		TreeNode<String> bar = ROOT.addChild("bar/");
		TreeNode<String> mno = ROOT.addChild("mno");
		TreeNode<String> contacts = ROOT.addChild("contacts");
		bar.addChild("xyz");
		bar.addChild("abc");
		mno.addChild("about");

		contacts.addChild("has");
		contacts.addChild("more links");
		contacts.addChild("more links");
		contacts.addChild("more links");

		String actual = ROOT.toString();
		String expected = TWO_SUBLEVEL_SIMPLE + EMPTY_CHILD + HAS_MORE_LINKS;
		assertEquals(expected, actual);

	}

	@Test
	public void testTreeNodeEmptyChild() {

		TreeNode<String> root = new TreeNode<>("foobar.com/baz");
		TreeNode<String> ROOT = root;
		TreeNode<String> bar = ROOT.addChild("bar/");
		TreeNode<String> mno = ROOT.addChild("mno");
		ROOT.addChild("contacts");
		bar.addChild("xyz");
		bar.addChild("abc");
		mno.addChild("about");

		String actual = ROOT.toString();
		String expected = TWO_SUBLEVEL_SIMPLE + EMPTY_CHILD;
		assertEquals(expected, actual);

	}

	@Test
	public void testTreeNodeToString() {

		TreeNode<String> root = new TreeNode<>("foobar.com/baz");
		TreeNode<String> bar = root.addChild("bar/");
		bar.addChild("xyz");
		bar.addChild("abc");
		TreeNode<String> mno = root.addChild("mno");
		mno.addChild("about");

		String actual = root.toString();
		String expected = TWO_SUBLEVEL_SIMPLE;
		assertEquals(expected, actual);

	}

}
