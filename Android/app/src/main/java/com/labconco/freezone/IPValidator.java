package com.labconco.freezone;

import android.util.Log;

/**
 * Created by James Holdcroft
 * This takes an ip address in as a string and divides each section into an int which it then checks whether or not it's between 0 and 255
 */

public class IPValidator {
    private String ip;
    public IPValidator(String ip){
        this.ip = ip;
    }

    public Boolean validate(){
        //12.43.13.50
        int periodOnePos=0, periodTwoPos=0, periodThreePos=0;
        int one = -1, two = -1, three = -1, four = -1; //keep them out of range so a false positive is not thrown
        for (int i = 0; i < ip.length(); i++){
            if ((ip.charAt(i) + "").equals(".")) {
                if (periodOnePos == 0) {
                    //get first
                    periodOnePos = i;
                } else {
                    if (periodTwoPos == 0) {
                        //get second
                        periodTwoPos = i;
                    } else {
                        //get third
                        periodThreePos = i;
                    }
                }
            }
        }
        try {
            one = Integer.parseInt(ip.substring(0, periodOnePos));
            two = Integer.parseInt(ip.substring(periodOnePos + 1, periodTwoPos));
            three = Integer.parseInt(ip.substring(periodTwoPos + 1, periodThreePos));
            four = Integer.parseInt(ip.substring(periodThreePos + 1, ip.length()));
        } catch (Exception e) { //I do not know what exception I'm searching for I just know there may be one
            Log.d("Debug", "IPValidator parse ran into an exception");
        }
        return !(outOfRange(one) || outOfRange(two) || outOfRange(three) || outOfRange(four));
    }

    private boolean outOfRange(int n){
        if (n < 0 || n > 255){
            return true;
        }
        return false;
    }

}
