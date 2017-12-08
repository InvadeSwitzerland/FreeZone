package com.labconco.freezone;

/**
 * Created by James Holdcroft
 */

public class IPValidator {
    private String ip;
    public IPValidator(String ip){
        this.ip = ip;
    }

    public Boolean validate(){
        //12.43.13.50
        int periodOnePos=0, periodTwoPos=0, periodThreePos=0;
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
        int one = Integer.parseInt(ip.substring(0, periodOnePos));
        int two = Integer.parseInt(ip.substring(periodOnePos + 1, periodTwoPos));
        int three = Integer.parseInt(ip.substring(periodTwoPos + 1, periodThreePos));
        int four = Integer.parseInt(ip.substring(periodThreePos + 1, ip.length()));
        System.out.println(one + "\n" + two + "\n" + three + "\n" + four);
        return !(outOfRange(one) || outOfRange(two) || outOfRange(three) || outOfRange(four));
    }

    private boolean outOfRange(int n){
        if (n < 0 || n > 255){
            return true;
        }
        return false;
    }

}
