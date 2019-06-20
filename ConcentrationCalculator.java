package chromostat;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConcentrationCalculator {

    private static DecimalFormat df2 = new DecimalFormat("#.###");
    private GpioPinDigitalOutput pinOne;
    private GpioPinDigitalOutput pinTwo;
    private List<Integer> baseline;
    private List<Integer> sampleOne;
    private List<Integer> sampleTwo;

    public ConcentrationCalculator(GpioPinDigitalOutput pinOne, GpioPinDigitalOutput pinTwo) {
        this.pinOne = pinOne;
        this.pinTwo = pinTwo;
    }

    public void removeFalseValues(List<Integer> baseline, List<Integer> sampleOne,
            List<Integer> sampleTwo, List<Integer> mixedSample) {

        if (baseline.size() != sampleOne.size() || sampleOne.size() != sampleTwo.size()
                || sampleTwo.size() != mixedSample.size()) {

            throw new IllegalArgumentException();

        }

        int minimum = 8;
        int oldSize = baseline.size();

        for (int i = oldSize - 1; i >= 0; i--) {

            if (sampleOne.get(i) < minimum || sampleTwo.get(i) < minimum
                    || baseline.get(i) < minimum) {

                sampleOne.remove(i);
                sampleTwo.remove(i);
                baseline.remove(i);
                mixedSample.remove(i);

            }

        }

    }

    //pre: ArrayList of optical density values from the mixed sample of interest. A List of Lists
    //     that contain the molar absortivity values of the two pure sampels. The length of the 
    //     matrix that needs to be used in octave. 
    //post: 
    public void computeConcentration(List<Double> opticalDensity,
            List<List<Double>> eValues) throws IOException {

        int matrixLength = opticalDensity.size();

        List<String> octaveCommands = new ArrayList<>();
        octaveCommands.add("octave");
        octaveCommands.add("MolarAbsorbCalc.m");

        String values = "" + (double) matrixLength + " ";

        values += opticalDensity.stream()
                .map(Object::toString).
                collect(Collectors.joining(" "));

        for (int i = 0; i < eValues.size(); i++) {

            List<Double> currentList = eValues.get(i);

            values += " " + currentList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));

        }

        octaveCommands.add(values);

        ProcessBuilder octaveManager = new ProcessBuilder(octaveCommands);

        Process startOctave = octaveManager.start();

        Scanner inputReader = new Scanner(new InputStreamReader(startOctave.getInputStream()));

        String waste = "";
        List<Double> concentrations = new ArrayList<>();

        while (inputReader.hasNextLine()) {

            System.out.println(inputReader.nextLine());

        }

    }

    //pre: a scanner that takes user input
    //post: requires user to press enter to continue program execution 
    private void waitForEnter(Scanner s) {

        String input = s.nextLine();

        while (input.length() != 0) {

            input = s.nextLine();

        }

    }

    public List<Double> calculateMolA(List<Integer> baseline, List<Integer> sample,
            double pathlength, double concentration) {

        if (baseline.size() != sample.size()) {
            throw new IllegalArgumentException();
        }

        List<Double> molA = new ArrayList<>();

        System.out.println();

        for (int i = 0; i < baseline.size(); i++) {

            if (baseline.get(i) == 0) {
                baseline.set(i, 1);

            }
            if (sample.get(i) == 0) {
                sample.set(i, 1);
            }

            //note: cast array values to double prior to division to ensure that result is a double
            double currentOD = Math.log10((double) baseline.get(i) / (double) sample.get(i))
                    / (pathlength * concentration);
            molA.add(currentOD);

        }

        System.out.println();
        return molA;

    }

    //pre: a List of intensity values of a sample at a known concentraiton (baseline) and a list of
    //     intensity of a sample at an unknown concentration (sample) (lists must be the same length
    //     throws IllegalArgumentException otherwise)
    //post: returns a List of transmittance values calculated from the baseline and sample Lists
    public List<Double> calculateOD(List<Integer> baseline, List<Integer> sample,
            double pathLength) {

        return calculateMolA(baseline, sample, pathLength, 1);

    }
}
