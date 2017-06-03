package com.notatracer.sandbox.app.websocket.web.websocket.interceptor;

import org.apache.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * Detects messages flowing over message channel.
 * @author grudkowm
 *
 */
public class InboundChannelInterceptor extends ChannelInterceptorAdapter {

	private Logger LOGGER = Logger.getLogger(InboundChannelInterceptor.class);
	
	@Override
	public Message<?> preSend(Message<?> arg0, MessageChannel arg1) {
		LOGGER.info("InboundChannelInterceptor::preSend");
		MessageHeaders headers = arg0.getHeaders();
		headers.forEach((k, v) -> {
			LOGGER.info(String.format("Header [name=%s, value=%s]", k, v));
		});
		return arg0;
	}
	
	

	@Override
	public void postSend(Message<?> arg0, MessageChannel arg1, boolean arg2) {
		LOGGER.info("InboundChannelInterceptor::postSend");

	}

	@Override
	public void afterSendCompletion(Message<?> arg0, MessageChannel arg1, boolean arg2, Exception arg3) {
		LOGGER.info("InboundChannelInterceptor::afterSendCompletion");
	}

	@Override
	public boolean preReceive(MessageChannel arg0) {
		LOGGER.info("InboundChannelInterceptor::preReceive");
		return false;
	}

	@Override
	public Message<?> postReceive(Message<?> arg0, MessageChannel arg1) {
		LOGGER.info("InboundChannelInterceptor::postReceive");
		return null;
	}

	
	@Override
	public void afterReceiveCompletion(Message<?> arg0, MessageChannel arg1, Exception arg2) {
		LOGGER.info("InboundChannelInterceptor::afterReceiveCompletion");
	}



}
