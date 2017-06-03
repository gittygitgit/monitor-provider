package com.notatracer.sandbox.app.websocket.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.notatracer.sandbox.app.websocket.domain.FirmSummary;

@Component
public class SQFMonitorRepository implements Repository<Map<String, FirmSummary>> {


	private static Set<String> ports = new HashSet<>();
	private static Map<String, Set<String>> firmRegistrations = new HashMap<>();
	
	static {
		ports.add("UC001");
		ports.add("UC002");
		
		Set<String> uc001Firms=new HashSet<>();
		uc001Firms.add("009G");
		uc001Firms.add("AMDP");
		
		Set<String> uc002Firms=new HashSet<>();
		uc002Firms.add("CDRG");
		uc002Firms.add("BEST");
		uc002Firms.add("CNDB");
		
		firmRegistrations.put("UC001", uc001Firms);
		firmRegistrations.put("UC001", uc002Firms);
	}
	
	@Override
	public Map<String, FirmSummary> getCurrentSnapshot() {
		Map<String, FirmSummary> state = new HashMap<>();
		
		state.put("009G", new FirmSummary("UC001", "009G", new BigDecimal(".081"), 1_933_739, 871_086, 3, 1_757));
		state.put("AMDP", new FirmSummary("UC001", "AMDP", new BigDecimal("0"), 1_752, 1_455, 70, 8));
		state.put("CDRG", new FirmSummary("UC002", "CDRG", new BigDecimal("0"), 62_384, 799, 2, 616));
		state.put("BEST", new FirmSummary("UC002", "BEST", new BigDecimal("0"), 0, 0, 0, 3_552));
		return state;
	}
	

}
