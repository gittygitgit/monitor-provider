package com.notatracer.sandbox.app.websocket.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notatracer.sandbox.app.websocket.integration.messaging.MessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.MessageParser;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SQFMonitorMessageListener;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonFirmUpdate;
import com.notatracer.sandbox.app.websocket.integration.messaging.sqfmonitor.SeqSqfMonMsg;

@Component
public class DummyDataGeneratingDropConnection implements DropConnection {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private static Map<String, Set<String>> portFirmMap = new HashMap<>();
	
	@Autowired
	private MessageParser<SeqSqfMonMsg> parser;

	private SeqSqfMonFirmUpdate seqSqfMonFirmUpdate = new SeqSqfMonFirmUpdate();

	private Runnable messageGenerator = () -> {
		seqSqfMonFirmUpdate.clear();
		ThreadLocalRandom.current().nextInt(portFirmMap.size());
		String port = portFirmMap.keySet().toArray(new String[0])[ThreadLocalRandom.current().nextInt(portFirmMap.size())];
		Set<String> firmSet = portFirmMap.get(port);
		String firm = firmSet.toArray(new String[0])[ThreadLocalRandom.current().nextInt(firmSet.size())];
		
		setString(seqSqfMonFirmUpdate.firmName, firm);
		setString(seqSqfMonFirmUpdate.portName, port);
		
		seqSqfMonFirmUpdate.latency = ThreadLocalRandom.current().nextLong(100l);
		seqSqfMonFirmUpdate.numBlocks = ThreadLocalRandom.current().nextLong(100l);
		seqSqfMonFirmUpdate.numQuotes = ThreadLocalRandom.current().nextLong(100l);
		seqSqfMonFirmUpdate.numPurges = ThreadLocalRandom.current().nextInt(100);
		seqSqfMonFirmUpdate.numUndPurges = ThreadLocalRandom.current().nextInt(100);
				
		this.onData(seqSqfMonFirmUpdate);
	};

	private MessageListener l;

	private static Set<String> foo(String... firms ) {
		Set<String> fset = new HashSet<>();
		fset.addAll(Arrays.asList(firms));
		return fset;
	}
	
	private void setString(byte[] dest, String src) {
		src = src.trim();
		System.arraycopy(src.getBytes(), 0, dest, 0, src.length());
	}

	static {
		Set<String> firms = new HashSet<>();
		firms.add("009G");
		firms.add("AMDP");
		
		portFirmMap.put("UC001", foo("009G", "AMDP"));
		portFirmMap.put("UC002", foo("CDRG", "BEST", "CNDB"));
	}
	
	
	@Override
	public void connect(MessageListener l) {

		// connect to drop
		
		// schedule generation of mock data
		
//		state.put("009G", new FirmSummary("UC001", "009G", new BigDecimal(".081"), 1_933_739, 871_086, 3, 1_757));
//		state.put("AMDP", new FirmSummary("UC001", "AMDP", new BigDecimal("0"), 1_752, 1_455, 70, 8));
//		state.put("CDRG", new FirmSummary("UC002", "CDRG", new BigDecimal("0"), 62_384, 799, 2, 616));
//		state.put("BEST", new FirmSummary("UC002", "BEST", new BigDecimal("0"), 0, 0, 0, 3_552));
//		return state;
		
		this.connect(l, null);
	}

	@Override
	public void connect(MessageListener l, String url) {

		if (!SQFMonitorMessageListener.class.isAssignableFrom(l.getClass())) {
			throw new IllegalArgumentException("Unsupported listener class [class=" + l.getClass().getSimpleName() + ", required=SQFMonitorMessageListener]");
		}
		this.l = l;
		executor.scheduleAtFixedRate(messageGenerator, 0l, 1l, TimeUnit.SECONDS);
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onData(Object o) {
		
		if (!SeqSqfMonMsg.class.isAssignableFrom(o.getClass())) {
			throw new IllegalArgumentException("Message must extend from SeqSqfMonMsg [actual=" + o.getClass().getSimpleName() + "]");
		}
		
		this.parser.parse((SeqSqfMonMsg)o, this.l);
	}

}
