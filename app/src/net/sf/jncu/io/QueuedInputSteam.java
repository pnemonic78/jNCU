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
package net.sf.jncu.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Queued input stream. Behaves almost like a pipe, where the queue is the
 * source, and the stream itself is the sink.
 * 
 * @author moshew
 */
public class QueuedInputSteam extends InputStream {

	private final Queue<Byte> q;

	/**
	 * Creates a new queued input stream.
	 * 
	 * @param queue
	 *            the source queue.
	 */
	public QueuedInputSteam(Queue<Byte> queue) {
		super();
		this.q = queue;
	}

	@Override
	public int read() throws IOException {
		Byte b;
		if (q instanceof BlockingQueue<?>) {
			BlockingQueue<Byte> bq = (BlockingQueue<Byte>) q;
			try {
				b = bq.take();
			} catch (InterruptedException ie) {
				throw new IOException(ie);
			}
		} else {
			b = q.poll();
			if (b == null) {
				return -1;
			}
		}
		return b.intValue() & 0xFF;
	}

	@Override
	public int available() throws IOException {
		return q.size();
	}

	@Override
	public void close() throws IOException {
		super.close();
		q.clear();
	}
}
