package com.notatracer.sandbox.app.websocket.integration.emulation;

/**
 * Generates a sequence of message instances.
 * </p>
 * Will be configured as follows;
 * <li>Message types</li>
 * <li>Message ordering</li>
 * <li>Message rate</li>
 * <li>Message dependencies</li>
 * </p>
 * TODO: Define a good way to describe the stream
 * @author grudkowm
 *
 */
public interface StreamGenerator {
	
	
	public void start();

}
