package com.debuggerme.filerandomizer.util;

import java.util.Arrays;

/**
 * @author Jamith Nimantha
 */
public class Randomizer {

    public static int[] generateSingleSet(int numbPerSet, int rangeBegin, int rangeEnd) {
        int [] numbersPerSet = new int [numbPerSet];
        for (int i = 0; i < numbersPerSet.length; i++) {
            numbersPerSet[i] = (int) ((Math.floor(Math.random() * ((rangeEnd + 1) - (rangeBegin)))) + (rangeBegin));
            for (int j = 0; j < i; j++) {
                while (numbersPerSet[i] == numbersPerSet[j]) {
                    numbersPerSet[i] = (int) ((Math.floor(Math.random() * ((rangeEnd + 1) - (rangeBegin)))) + (rangeBegin));
                    j = 0;
                }
            }
        }
        Arrays.sort(numbersPerSet);
        return numbersPerSet;
    }



}
