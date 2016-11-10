package model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by emmalautz on 10/23/16.
 */

public class CancellationPolicy {

    public static final String NO_CHARGE = "NO_CHARGE";
    public static final String TEN_PERCENT_OF_CHARGE = "TEN_PERCENT_OF_CHARGE";
    public static final String FIVE_DOLLAR_CHARGE = "FIVE_DOLLAR_CHARGE";
    public static final String TEN_DOLLAR_CHARGE = "TEN_DOLLAR_CHARGE";
    
    public static final String noCharge = "No charge";
    public static final String tenPercentOfCharge = "Ten percent of charge";
    public static final String fiveDollarCharge = "Five dollar charge";
    public static final String tenDollarCharge = "Ten dollar charge";
    
    public static HashMap<String, String> policies;
    public static List<String> viewPolicies;
    
    public static void initialize(){
    	
    	if (policies == null){
    		policies = new HashMap<>();
    		policies.put(noCharge, NO_CHARGE);
    		policies.put(tenPercentOfCharge, TEN_PERCENT_OF_CHARGE);
    		policies.put(fiveDollarCharge, FIVE_DOLLAR_CHARGE);
    		policies.put(tenDollarCharge, TEN_DOLLAR_CHARGE);
    	}
    	
    	if (viewPolicies == null){
    		viewPolicies = new ArrayList<>();
    		viewPolicies.add(noCharge);
    		viewPolicies.add(tenPercentOfCharge);
    		viewPolicies.add(fiveDollarCharge);
    		viewPolicies.add(tenDollarCharge);
    	}
    }
}
