/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

import static chromostat.ConcentrationCalculator.calculateConcentration;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nicho
 */
public class Chromostat {

    final static GpioController GPIO = GpioFactory.getInstance(); // set up GPIO controller 

    public static void main(String[] args) throws IOException, UnsupportedBusNumberException, InterruptedException {

        List<Integer> L1 = new ArrayList<>();
        L1.add(1);
        L1.add(2);

        List<Integer> L2 = new ArrayList<>();
        L2.add(3);
        L2.add(4);

        List<Integer> L3 = new ArrayList<>();
        L3.add(4);
        L3.add(6);

        final GpioPinDigitalOutput pin = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);

        Scanner userInput = new Scanner(System.in);

//        printCommands();
        
        calculateConcentration();

//        String command = userInput.next();
//        CameraController camera = new CameraController();
//
//        while (true) {
//
//            if (command.equalsIgnoreCase("l")) {
//
//                pin.toggle();
//
//            } else if (command.equalsIgnoreCase("b")) {
//
//                camera.setBaseline();
//
//            } else if (command.equalsIgnoreCase("s")) {
//
//                camera.setSampleSpectrum();
//                
//
//            }
//            Thread.sleep(300);
//            printCommands();
//            command = userInput.next();
//
//        }

    }

    public static void printCommands() {
        System.out.println("Press l to toggle light");
        System.out.println("Press b to capture a spectrum");
        System.out.println("Press s to capture sample spectrum");
    }
}
