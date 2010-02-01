package net.sf.jncu.protocol;

import java.util.zip.Checksum;

/**
 * Frame Check Sequence.
 */
public class FrameCheckSequence implements Checksum {

	/** 16 bit value. */
	private int fcsWord;

	/** Creates a new FCS. */
	public FrameCheckSequence() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.zip.Checksum#getValue()
	 */
	@Override
	public long getValue() {
		return fcsWord;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.zip.Checksum#reset()
	 */
	@Override
	public void reset() {
		fcsWord = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.zip.Checksum#update(byte[], int, int)
	 */
	@Override
	public void update(byte[] b, int off, int len) {
		for (int i = off; i < len; i++) {
			update(b[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.zip.Checksum#update(int)
	 */
	@Override
	public void update(int b) {
		int pow = 1;
		for (int i = 0; i < 8; i++) {
			if ((((fcsWord & 0xFF) & 0x01) == 0x01) ^ ((b & pow) == pow)) {
				fcsWord = (fcsWord >>> 1) ^ 0xa001;
			} else {
				fcsWord >>>= 1;
			}
			pow <<= 1;
		}
	}
}