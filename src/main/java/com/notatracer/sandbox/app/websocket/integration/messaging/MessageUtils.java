package com.notatracer.sandbox.app.websocket.integration.messaging;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class MessageUtils {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	
	public static void setString(byte[] dest, String src) {
		src = src.trim();
		System.arraycopy(src.getBytes(), 0, dest, 0, src.length());
	}

	public static void setString(byte[] dest, byte[] src) {
		System.arraycopy(src, 0, dest, 0, src.length);
	}

	public static void setLong(Object o, String fname, long max) {
		try {
			Field field = o.getClass().getField(fname);
			field.setLong(o, ThreadLocalRandom.current().nextLong(max));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void setTime(byte[] dest) {
		String timeFormatted = LocalDateTime.now().format(formatter);			
		System.arraycopy(timeFormatted.getBytes(), 0, dest, 0, timeFormatted.length());
	}
}
