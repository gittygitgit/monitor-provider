package com.notatracer.sandbox.app.websocket.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageUtils;
import com.notatracer.sandbox.app.websocket.integration.messaging.Message;
import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.MessageParser;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SQFMonitorMessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonMsg;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonPortActivity;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonPortConnectionEvent;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonPortMaint;

@Component
public class DummyDataGeneratingDropConnection implements DropConnection {

	private static final long INTERVAL_MSGGEN_MILLI = 250l;

	private static Logger LOGGER = Logger.getLogger(DummyDataGeneratingDropConnection.class);

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private static Map<String, Set<String>> portFirmMap = new HashMap<>();
	
	@Autowired
	private MessageParser<SeqSqfMonMsg> parser;

 	private SeqSqfMonPortActivity seqSqfMonPortActivity = new SeqSqfMonPortActivity();
	
	private SeqSqfMonPortConnectionEvent seqSqfMonPortConnectionEvent = new SeqSqfMonPortConnectionEvent();
	
	private SeqSqfMonPortMaint seqSqfMonPortMaint = new SeqSqfMonPortMaint();
	
	private Map<String, SeqSqfMonPortMaint> portMap = new HashMap();
	
	private MessageListener l;

	private ScheduledFuture<?> generatorHandle;

	private Runnable messageGenerator = () -> {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Generating message.");
		}
		
		// Randomly generate port connection event
		double ratio = ThreadLocalRandom.current().nextDouble();
		if (ratio > .9d) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Generating message [type=%s]", (char)Message.MsgType.PORT_CONNECTION_EVENT.getVal()));
			}
			
			seqSqfMonPortConnectionEvent.clear();
			 
			SeqSqfMonPortMaint port = portMap.values().toArray(new SeqSqfMonPortMaint[0])[ThreadLocalRandom.current().nextInt(portMap.size())];
			MessageUtils.setString(seqSqfMonPortConnectionEvent.groupName, port.groupName);
			MessageUtils.setString(seqSqfMonPortConnectionEvent.ringName, port.ringName);
			MessageUtils.setString(seqSqfMonPortConnectionEvent.portName, port.name);
			seqSqfMonPortConnectionEvent.event = Message.ConnectionEvent.CONNECT.getVal();
			MessageUtils.setTime(seqSqfMonPortConnectionEvent.time);
			
			this.onData(seqSqfMonPortConnectionEvent);
			
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Generating message [type=%s]", (char)Message.MsgType.PORT_ACTIVITY.getVal()));
			}

			seqSqfMonPortActivity.clear();
			
			ThreadLocalRandom.current().nextInt(portMap.size());
			String port = portMap.keySet().toArray(new String[0])[ThreadLocalRandom.current().nextInt(portMap.size())];
			SeqSqfMonPortMaint portMaint = portMap.get(port);
			
			MessageUtils.setTime(seqSqfMonPortActivity.last);
			MessageUtils.setString(seqSqfMonPortActivity.groupName, portMaint.groupName);
			MessageUtils.setString(seqSqfMonPortActivity.name, portMaint.name);
			MessageUtils.setString(seqSqfMonPortActivity.ringName, portMaint.ringName);
			
			MessageUtils.setLong(seqSqfMonPortActivity, "limitCurrent", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "limit1Min", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "limit5Min", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "blocks", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "numBlocks", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "numQuotes", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "numPurges", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "numUndPurges", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "ql1Min", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "qlCurrent", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "rateCurrent", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "rate1Min", 1000);
			MessageUtils.setLong(seqSqfMonPortActivity, "rate5Min", 1000);
			
			this.onData(seqSqfMonPortActivity);			
		}
	};

	@Override
	public void connect(MessageListener l) {
		this.connect(l, null);
	}

	@Override
	public void connect(MessageListener l, String url) {

		LOGGER.info("Connecting.");
		
		if (!SQFMonitorMessageListener.class.isAssignableFrom(l.getClass())) {
			throw new IllegalArgumentException("Unsupported listener class [class=" + l.getClass().getSimpleName() + ", required=SQFMonitorMessageListener]");
		}
		this.l = l;
		
		spinPorts();
		generateEventStream();
	}

	private void generateEventStream() {
		LOGGER.info("Generating event stream.");
		generatorHandle = executor.scheduleAtFixedRate(messageGenerator, 0l, INTERVAL_MSGGEN_MILLI, TimeUnit.MILLISECONDS);
	}

	private void spinPorts() {
		seqSqfMonPortMaint.clear();

		
		seqSqfMonPortMaint.id = 1l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "WOL2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRWOL1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRWOL1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		
		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		
		seqSqfMonPortMaint.id = 2l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "WOL2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRWOL2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRWOL2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 3l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "WOL2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRWOL3");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRWOL3", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 4l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "WOL2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRWOL4");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRWOL4", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 5l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "GSM1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRGSM1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRGSM1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 6l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "GSM1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRGSM2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRGSM2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 7l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SIG1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSIG1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSIG1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 8l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SIG1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSIG2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSIG2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 9l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SIG2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSG21");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSG21", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 10l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SIG2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSG22");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSG22", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 11l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 12l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 13l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI3");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI3", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 14l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI4");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI4", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 15l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI5");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI5", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 16l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "THI7");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRTHI6");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRTHI6", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);
		

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 17l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "ABC1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRABC1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRABC1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 18l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "ABC1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRABC2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRABC2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 19l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "ABC2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRAC21");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRAC21", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 20l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "ABC2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRAC22");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRAC22", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 21l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 22l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 23l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC3");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC3", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 24l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC4");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC4", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 25l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC5");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC5", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 26l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "SFC6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRSFC6");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRSFC6", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);


		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 27l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 28l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 29l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD3");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD3", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 30l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD4");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD4", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 31l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD5");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD5", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 32l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "BHD6");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRBHD6");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRBHD6", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);



		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 33l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMTG1");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMTG1", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 34l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG1");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMTG2");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMTG2", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 35l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMG21");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMG21", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 36l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG2");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMG22");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMG22", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 37l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG3");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMG31");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMG31", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);

		seqSqfMonPortMaint = new SeqSqfMonPortMaint();
		seqSqfMonPortMaint.id = 38l;
		MessageUtils.setString(seqSqfMonPortMaint.groupName, "MTG3");
		MessageUtils.setString(seqSqfMonPortMaint.name, "CRMG32");
		MessageUtils.setString(seqSqfMonPortMaint.ringName, "POEM");
		portMap.put("CRMG32", seqSqfMonPortMaint);
		this.onData(seqSqfMonPortMaint);
	}

	@Override
	public void disconnect() {
		LOGGER.info("Disconnecting.");

		generatorHandle.cancel(true);
		
		this.l = null;
	}

	@Override
	public void onData(Object o) {
		
		if (!SeqSqfMonMsg.class.isAssignableFrom(o.getClass())) {
			throw new IllegalArgumentException("Message must extend from SeqSqfMonMsg [actual=" + o.getClass().getSimpleName() + "]");
		}
		
		this.parser.parse((SeqSqfMonMsg)o, this.l);
	}

}
