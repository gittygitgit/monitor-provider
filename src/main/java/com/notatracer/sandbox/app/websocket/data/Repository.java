package com.notatracer.sandbox.app.websocket.data;

/**
 * 
 * 
 * @author grudkowm
 *
 */
public interface Repository<T> {
	
	T getCurrentSnapshot();

}
