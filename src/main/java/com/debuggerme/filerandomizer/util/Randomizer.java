package com.debuggerme.filerandomizer.util;

import com.debuggerme.filerandomizer.controller.MainController;

import java.util.Arrays;

/**
 * @author Jamith Nimantha
 */
public class Randomizer {

    public Randomizer(MainController mainController) {
    }

    public int[] generateSingleSet(int numbPerSet, int rangeBegin, int rangeEnd) {
        int [] numbersPerSet = new int [numbPerSet];
        boolean unique = true;
        for (int i = 0; i < numbersPerSet.length; i++) {
            numbersPerSet[i] = (int) ((Math.floor(Math.random() * ((rangeEnd + 1) - (rangeBegin)))) + (rangeBegin));
            if (unique) {
                for (int j = 0; j < i; j++) {
                    while (numbersPerSet[i] == numbersPerSet[j]){
                        numbersPerSet[i] = (int) ((Math.floor(Math.random() * ((rangeEnd + 1) - (rangeBegin)))) + (rangeBegin));
                        j = 0;
                    }
                }
            }
        }
        Arrays.sort(numbersPerSet);
        return numbersPerSet;
    }



}
