package com.akrambenaissi.webcrawler;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

	T data;
	TreeNode<T> parent;
	List<TreeNode<T>> children;
	private int depth;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
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

	public String toString(int offset) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < offset + 1; i++) {
			sb.append("  ");
		}
		sb.append("- ").append(data).append("\n");
		return sb.toString();
	}

	public String toString2() {
		StringBuilder sb = new StringBuilder();
//		sb.append(data).append("\n");
		if (this.parent == null) {
			sb.append(data).append("\n");
		}
		if (children != null && !children.isEmpty()) {
			StringBuilder offset = new StringBuilder();
			for (int i = 0; i < this.depth + 1; i++) {
				sb.append(" ");
			}
			sb.append(offset).append("- ").append(data);
			for (int i = 0; i < this.depth; i++) {
				sb.append(" ");
			}
			sb.append("- ").append(data).append("\n");
			for (TreeNode<T> child : children) {
				sb.append(child.toString()).append("\n");
			}
			sb.append(offset).append("- ").append(data);
		}

		return sb.toString();

	}

	public int getDepth() {
		return depth;
	}

}