/**
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 * 
 */
public class NSOFSmallRect extends NSOFObject {

	private int top;

	private int left;

	private int bottom;

	private int right;

	/**
	 * Constructs a new object.
	 * 
	 */
	public NSOFSmallRect() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setTop(0);
		setLeft(0);
		setBottom(0);
		setRight(0);

		// Top value (int)
		int top = in.read();
		if (top == -1) {
			throw new EOFException();
		}
		setTop(top);
		// Left value (int)
		int left = in.read();
		if (left == -1) {
			throw new EOFException();
		}
		setLeft(left);
		// Bottom value (int)
		int bottom = in.read();
		if (bottom == -1) {
			throw new EOFException();
		}
		setBottom(bottom);
		// Right value (int)
		int right = in.read();
		if (right == -1) {
			throw new EOFException();
		}
		setRight(right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (top << 24) | (left << 16) | (bottom << 8) | (right << 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{left: " + left + ", top: " + top + ", right: " + right
				+ ", bottom: " + bottom + "}";
	}
}
