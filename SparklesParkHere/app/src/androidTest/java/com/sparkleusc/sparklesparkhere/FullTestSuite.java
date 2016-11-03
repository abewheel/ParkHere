package com.sparkleusc.sparklesparkhere;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
/**
 * Created by emmalautz on 10/25/16.
 */

public class FullTestSuite {

    public static Test suit(){
        return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();

    }

    public FullTestSuite(){
        super();
    }
}
