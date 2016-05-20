package com.centit.support.algorithm;

import java.util.UUID;

public class UuidOpt {

    public static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    
 /*   public static String uuidTo32String2(UUID uuid){
        long leastSigBits =uuid.getLeastSignificantBits() ;
        long mostSigBits = uuid.getMostSignificantBits();
        return (digits(mostSigBits >> 32, 8) + 
                digits(mostSigBits >> 16, 4) + 
                digits(mostSigBits, 4) + 
                digits(leastSigBits >> 48, 4) + 
                digits(leastSigBits, 12));
    }*/
    
    public static String uuidTo32String(UUID uuid){
        long leastSigBits =uuid.getLeastSignificantBits() ;
        long mostSigBits = uuid.getMostSignificantBits();
        return Long.toHexString(mostSigBits)+ Long.toHexString(leastSigBits);
    }
    
    public static String getUuidAsString(){
    	UUID uuid =  UUID.randomUUID();
        long leastSigBits =uuid.getLeastSignificantBits() ;
        long mostSigBits = uuid.getMostSignificantBits();
        return Long.toHexString(mostSigBits)+ Long.toHexString(leastSigBits);
    }
    


}
