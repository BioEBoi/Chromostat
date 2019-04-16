/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author nicho
 */
public class ConcentrationCalculator {

    public static void calculateConcentration() throws IOException, InterruptedException {

        Scanner userInput = new Scanner(System.in);
        CameraController cameraController = new CameraController();

        //set baseline of media
        System.out.println("Hit enter to set media BaseLine");
        userInput.next();
        List<Integer> baseLine = cameraController.setBaseline();
        System.out.println(baseLine);

        //measure spectrum of sample 1
        System.out.println("Hit enter to set sample one baseline");
        userInput.next();
        List<Integer> sampleOne = cameraController.setSampleSpectrum();
        System.out.println(sampleOne);

        //measure spectrum of sample 2
        System.out.println("Hit enter to set sample two baseline");
        userInput.next();
        List<Integer> sampleTwo = cameraController.setSampleSpectrum();
        System.out.println(sampleTwo);

        //measure spectrum of mixed data
        System.out.println("Hit enter to set mixed sample");
        userInput.next();
        List<Integer> data = cameraController.setSampleSpectrum();
        System.out.println(data);
        System.out.println();

        //measure absorbance  of sample 1
        System.out.println("First absorbance values");
        System.out.println();
        List<Double> absorbanceOne = computeAbsorbance(baseLine, sampleOne);
        System.out.println(absorbanceOne);
        System.out.println();

        //measure spectrum of sample 2
        System.out.println("Second absorbance values");
        System.out.println();
        List<Double> absorbanceTwo = computeAbsorbance(baseLine, sampleTwo);
        System.out.println(absorbanceTwo);
        System.out.println();

        //measure absorbance of mixed data
        System.out.println("combined sample values");
        System.out.println();
        List<Double> sampleData = computeAbsorbance(baseLine, data);
        System.out.println(sampleData);
        System.out.println();

        computeConcentration(absorbanceOne, absorbanceTwo, sampleData);

    }

    //pre: a List of intensity values of a sample at a known concentraiton (baseline) and a list of
    //     intensity of a sample at an unknown concentration (sample) (lists must be the same length
    //     throws IllegalArgumentException otherwise)
    //post: returns a List of transmittance values calculated from the baseline and sample Lists
    public static List<Double> computeAbsorbance(List<Integer> baseline, List<Integer> sample) {

        if (baseline.size() != sample.size()) {
            throw new IllegalArgumentException();
        }

        List<Double> absorbanceValues = new ArrayList<>();

        for (int i = 0; i < sample.size(); i++) {

            if (sample.get(i) <= 0) {
                sample.set(i, 1);
            }

            absorbanceValues.add(Math.log10((baseline.get(i)) / (sample.get(i))));

        }

        return absorbanceValues;

    }

    //pre: three transmittance curves of the same length (throws illegalArgumentException otherwise)
    //post: computes the least square fit of the first two transmittance curves to the third 
    //      trasnmittance curve and prints out the least square coefficents of the two curves
    public static void computeConcentration(List<Double> absorbanceOne, List<Double> absorbanceTwo, List<Double> mixedAbsorbance) throws IOException {

        if (absorbanceOne.size() != absorbanceTwo.size()
                || absorbanceTwo.size() != mixedAbsorbance.size()) {

            throw new IllegalArgumentException();

        }

        List<String> octaveCommands = new ArrayList<>();

        octaveCommands.add("octave");
        octaveCommands.add("specConcentrationCalc.m"); //replace with the octave file name

        //converts the transmittance List to a String so it can be passed to octave
        String tString1 = absorbanceOne.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
//
//        octaveCommands.add(tString1);
        //converts the transmittance2 List to a String so it can be passed to octave
        String tString2 = absorbanceTwo.stream().map(Object::toString)
                .collect(Collectors.joining(" "));

//        octaveCommands.add(tString2);
        //converts the mixedTransmittance List to a String so it can be passed to octave
        String tString3 = mixedAbsorbance.stream().map(Object::toString)
                .collect(Collectors.joining(" "));

        String sumOfString = tString1 + " " + tString2 + " " + tString3;

        octaveCommands.add(tString1 + " " + tString2 + " " + tString3);

        ProcessBuilder octaveManager = new ProcessBuilder(octaveCommands);

        Process startOctave = octaveManager.start();

        Scanner inputReader = new Scanner(new InputStreamReader(startOctave.getInputStream()));

        int[] concentration = new int[2];
        int i = 0;
        String waste;

//        while (inputReader.hasNext()) {
//
//            if (inputReader.hasNextInt()) {
//
//                concentration[i] = inputReader.nextInt();
//                //there are only two concentration values, so i will never go above 1
//                i++;
//
//            } else {
//                waste = inputReader.next();
//            }
//
//        }
        while (inputReader.hasNextLine()) {

            System.out.println(inputReader.nextLine());

        }

//        System.out.println(Arrays.toString(concentration));
    }

}
