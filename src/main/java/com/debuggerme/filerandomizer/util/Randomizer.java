package com.debuggerme.filerandomizer.util;

import com.debuggerme.filerandomizer.controller.MainController;

import java.util.Arrays;

/**
 * @author Jamith Nimantha
 */
public class Randomizer {
    private final MainController mainController;

    public Randomizer(MainController mainController) {
        this.mainController = mainController;
    }

    public int[] generateSingleSet(int numbPerSet, int rangeBegin, int rangeEnd) {
        int [] numbersPerSet = new int [numbPerSet];
//        int rangeBegin = 1;
//        int rangeEnd = 20;
        boolean unique = true;
        for (int i = 0; i < numbersPerSet.length; i++) {
            numbersPerSet[i] = (int) ((Math.floor(Math.random() * ((rangeEnd + 1) - (rangeBegin)))) + (rangeBegin));
//            System.out.println(numbersPerSet[i]);
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
        System.out.println(Arrays.toString(numbersPerSet));
        return numbersPerSet;
    }

//    public static void main(String[] args) {
//        int numberOfSets = 5;
//        for (int i = 0; i < numberOfSets; i++) {
//            generateSingleSet(3,1,20);
//        }
//    }


}
