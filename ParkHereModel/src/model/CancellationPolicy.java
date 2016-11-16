package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    
    public static HashMap<String, String> policiesTodb;
    public static List<String> viewPolicies;
    public static HashMap<String, String> dbToPolicy;
    
    public static void initialize(){
    	
    	if (policiesTodb == null){
    		policiesTodb = new HashMap<>();
    		policiesTodb.put(noCharge, NO_CHARGE);
    		policiesTodb.put(tenPercentOfCharge, TEN_PERCENT_OF_CHARGE);
    		policiesTodb.put(fiveDollarCharge, FIVE_DOLLAR_CHARGE);
    		policiesTodb.put(tenDollarCharge, TEN_DOLLAR_CHARGE);
    	}
    	
    	if (dbToPolicy == null){
    		dbToPolicy = new HashMap<>();
    		dbToPolicy.put(NO_CHARGE, noCharge);
    		dbToPolicy.put(TEN_PERCENT_OF_CHARGE, tenPercentOfCharge);
    		dbToPolicy.put(TEN_DOLLAR_CHARGE, tenDollarCharge);
    		dbToPolicy.put(FIVE_DOLLAR_CHARGE, fiveDollarCharge);
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
