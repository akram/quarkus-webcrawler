package com.akrambenaissi.webcrawler;

import java.util.LinkedHashSet;
import java.util.Set;

public class TreeNode<T> {

	T data;
	TreeNode<T> parent;
	Set<TreeNode<T>> children;
	private Set<T> wholeFamily;
	private int depth;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedHashSet<TreeNode<T>>();
		if (parent != null) {
			this.wholeFamily = parent.wholeFamily;
		} else {
			this.wholeFamily = new LinkedHashSet<>();
		}
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
		this.wholeFamily.add(child);
		this.depth = parent != null ? parent.depth + 1 : 0;
		return childNode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (parent == null) {
			sb.append(data).append("\n");
		}
		if (children != null && !children.isEmpty()) {
			for (TreeNode<T> child : children) {
				sb.append(child.toString(depth)).append("");
				sb.append(child.toString());
			}
		}
		return sb.toString();

	}

	private String toString(int offset) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < offset + 1; i++) {
			sb.append("  ");
		}
		sb.append("- ").append(data).append("\n");
		return sb.toString();
	}

	public int getDepth() {
		return depth;
	}

	public boolean contains(T data) {
		return wholeFamily.contains(data);
	}

}