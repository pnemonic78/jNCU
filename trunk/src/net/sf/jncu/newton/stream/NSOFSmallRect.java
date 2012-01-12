/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Small Rectangle.
 * 
 * @author Moshe
 */
public class NSOFSmallRect extends NSOFObject implements Precedent {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("smallRect");

	private int top;
	private int left;
	private int bottom;
	private int right;

	/**
	 * Constructs a new small rectangle.
	 */
	public NSOFSmallRect() {
		this(0, 0, 0, 0);
	}

	/**
	 * Constructs a new small rectangle.
	 * 
	 * @param top
	 *            the top co-ordinate.
	 * @param left
	 *            the left co-ordinate.
	 * @param bottom
	 *            the bottom co-ordinate.
	 * @param right
	 *            the right co-ordinate.
	 */
	public NSOFSmallRect(int top, int left, int bottom, int right) {
		super();
		setNSClass(NS_CLASS);
		setTop(top);
		setLeft(left);
		setBottom(bottom);
		setRight(right);
	}

	/**
	 * Constructs a new small rectangle.
	 * 
	 * @param top
	 *            the top co-ordinate.
	 * @param left
	 *            the left co-ordinate.
	 * @param bottom
	 *            the bottom co-ordinate.
	 * @param right
	 *            the right co-ordinate.
	 */
	public NSOFSmallRect(byte top, byte left, byte bottom, byte right) {
		this();
		setTop(top);
		setLeft(left);
		setBottom(bottom);
		setRight(right);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Top value (byte)
		int top = in.read();
		if (top == -1) {
			throw new EOFException();
		}
		setTop(top);

		// Left value (byte)
		int left = in.read();
		if (left == -1) {
			throw new EOFException();
		}
		setLeft(left);

		// Bottom value (byte)
		int bottom = in.read();
		if (bottom == -1) {
			throw new EOFException();
		}
		setBottom(bottom);

		// Right value (byte)
		int right = in.read();
		if (right == -1) {
			throw new EOFException();
		}
		setRight(right);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(SMALL_RECT);
		// Top value (byte)
		out.write(getTop());
		// Left value (byte)
		out.write(getLeft());
		// Bottom value (byte)
		out.write(getBottom());
		// Right value (byte)
		out.write(getRight());
	}

	/**
	 * Get the bottom.
	 * 
	 * @return the bottom
	 */
	public int getBottom() {
		return bottom;
	}

	/**
	 * Set the bottom.
	 * 
	 * @param bottom
	 *            the bottom.
	 */
	public void setBottom(int bottom) {
		this.bottom = bottom & 0xFF;
	}

	/**
	 * Set the bottom.
	 * 
	 * @param bottom
	 *            the bottom.
	 */
	public void setBottom(byte bottom) {
		setBottom((int) bottom);
	}

	/**
	 * Get the left.
	 * 
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Set the left.
	 * 
	 * @param left
	 *            the left.
	 */
	public void setLeft(int left) {
		this.left = left & 0xFF;
	}

	/**
	 * Set the left.
	 * 
	 * @param left
	 *            the left.
	 */
	public void setLeft(byte left) {
		setLeft((int) left);
	}

	/**
	 * Get the right.
	 * 
	 * @return the right
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Set the right.
	 * 
	 * @param right
	 *            the right.
	 */
	public void setRight(int right) {
		this.right = right & 0xFF;
	}

	/**
	 * Set the right.
	 * 
	 * @param right
	 *            the right.
	 */
	public void setRight(byte right) {
		setRight((int) right);
	}

	/**
	 * Get the top.
	 * 
	 * @return the top
	 */
	public int getTop() {
		return top;
	}

	/**
	 * Set the top.
	 * 
	 * @param top
	 *            the top.
	 */
	public void setTop(int top) {
		this.top = top & 0xFF;
	}

	/**
	 * Set the top.
	 * 
	 * @param top
	 *            the top.
	 */
	public void setTop(byte top) {
		setTop((int) top);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (top << 24) | (left << 16) | (bottom << 8) | (right << 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFSmallRect) {
			NSOFSmallRect that = (NSOFSmallRect) obj;
			return (this.getBottom() == that.getBottom()) && (this.getLeft() == that.getLeft()) && (this.getRight() == that.getRight())
					&& (this.getTop() == that.getTop());
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{left: " + left + ", top: " + top + ", right: " + right + ", bottom: " + bottom + "}";
	}

}
