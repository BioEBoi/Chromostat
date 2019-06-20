/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nicho
 */
public class Chromostat {

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    final static GpioController GPIO = GpioFactory.getInstance(); // set up GPIO controller 

    public static void main(String[] args) throws IOException, UnsupportedBusNumberException, InterruptedException {

        GpioPinDigitalOutput pin1 = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
        GpioPinDigitalOutput pin2 = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_16, PinState.LOW);

        Scanner userInput = new Scanner(System.in);

        ConcentrationCalculator controller = new ConcentrationCalculator(pin1, pin2);
        CameraController cameraController = new CameraController(pin1, pin2);

        measureSample(userInput, cameraController, controller);

    }

    public static void printCommands() {
        System.out.println("Press l to toggle light");
        System.out.println("Press b to capture a spectrum");
        System.out.println("Press s to capture sample spectrum");
    }

    public static void measureSample(Scanner console, CameraController camera,
            ConcentrationCalculator conc) throws IOException {

        Double pathLength = 1.778;

        List<List<Double>> molarAbsorbVals = new ArrayList<>();
        List<List<Integer>> intensityVals = new ArrayList<>();

        System.out.println("For reference, this system is set up to work with 1.778 cm diameter test "
                + "tubes. "
                + "If these aren't\n"
                + "the tubes you're using, please change the path length \n"
                + "variable to reflect this");
        System.out.println();

        System.out.println("Please scan your baseline (hit enter to scan)");
        System.out.println();
        waitForEnter(console);

        List<Integer> baseline = camera.setBaseline();

        for (int i = 0; i < 2; i++) {

            System.out.println("Please scan your sample (hit enter to scan)");
            System.out.println();
            waitForEnter(console);

            intensityVals.add(camera.setSampleSpectrum());

        }

        System.out.println("Press enter to scan mixed sample");
        System.out.println();
        waitForEnter(console);

        List<Integer> mixedSample = camera.setSampleSpectrum();

        conc.removeFalseValues(baseline, intensityVals.get(0), intensityVals.get(1), mixedSample);

        System.out.println();

        System.out.println("baseline intensity is: ");
        System.out.println(baseline);
        System.out.println();

        System.out.println("sample one intensity is: ");
        System.out.println(intensityVals.get(0));
        System.out.println();

        System.out.println("sample two intensity it:");
        System.out.println(intensityVals.get(1));
        System.out.println();

        System.out.println("mixed intensity is:");
        System.out.println(mixedSample);
        System.out.println();

        for (int i = 0; i < intensityVals.size(); i++) {

            molarAbsorbVals.add(conc.calculateMolA(baseline, intensityVals.get(i), pathLength, 1));

        }

        conc.computeConcentration(conc.calculateOD(baseline, mixedSample, pathLength), molarAbsorbVals);

    }

    //pre: a scanner that takes user input
    //post: requires user to press enter to continue program execution 
    private static void waitForEnter(Scanner s) {

        String input = s.nextLine();

        while (input.length() != 0) {

            input = s.nextLine();

        }

    }
}
