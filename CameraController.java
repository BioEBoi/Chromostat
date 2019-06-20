/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import static com.pi4j.io.gpio.PinState.LOW;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 *
 * @author nicho
 */
public class CameraController {

    private BufferedImage imageData;
    private final List<Point> specPoints;          //cartesian points at which spectrum is measured
    private Raster colorData;
    private GpioPinDigitalOutput lightingPinOne;
    private GpioPinDigitalOutput lightingPinTwo;

    public CameraController(GpioPinDigitalOutput pinOne, GpioPinDigitalOutput pinTwo) {

        specPoints = new ArrayList<>();
        imageData = new BufferedImage(500, 500, TYPE_BYTE_GRAY);
        colorData = imageData.getRaster();
        lightingPinOne = pinOne;
        lightingPinTwo = pinTwo;

    }

    //post: creates a jpg from image taken by camera
    public void takePicture() throws IOException {

        if (imageData != null) {
            //resets the image so a new image can be written over it
            imageData = null;
        }

        int quality = 100;
        int height = 500;
        int width = 500;

        List imageCommands = new ArrayList<>();

        imageCommands.add("raspistill");
        imageCommands.add("-o");
        imageCommands.add("-v");
        imageCommands.add("-q");
        imageCommands.add("" + quality);
        imageCommands.add("-w");
        imageCommands.add("" + width);
        imageCommands.add("-h");
        imageCommands.add("" + height);

        //creates process builder with the given imageCommands
        ProcessBuilder imageCapture = new ProcessBuilder(imageCommands);
        imageCapture.redirectErrorStream(true);

        Process imageCaptureController = imageCapture.start();

        imageData = ImageIO.read(imageCaptureController.getInputStream());
        imageCaptureController.getInputStream().close();

    }

    //pre: a 32 bit integer 
    //post: returns an array containg the Red, Green, and Blue color values from that pixel    
    private int[] getRGB(int pixel) {

        int[] RGB = new int[4];

        RGB[0] = (pixel >> 16) & 0xff; //gets bits 16-24 (which correspond to the red value)
        RGB[1] = (pixel >> 8) & 0xff; //gets bits 8-16 (which correspond to the green channel)
        RGB[2] = (pixel) & 0xff; //gets bits 0-8 (which correspond to the blue channel)
        RGB[3] = (pixel >> 24) & 0xff; //gets bits 24-32 (which correspond to the alpha channel)

        return RGB;
    }

    private List<Integer> getSpectrumData(BufferedImage spectrumImage) {

        List<Integer> spectrumData = new ArrayList<>();

        for (int i = 0; i < specPoints.size(); i++) {

            Object pixel = colorData.getDataElements(0, 0, null);

            int grayValue = ((byte[]) pixel)[0] & 0xff;

            spectrumData.add(grayValue);

        }

        return spectrumData;

    }

    //post: takes an image using the camera, finds the spectrum, saves the values of the spectrum
    //      to the baseLine, and prints out the values
    public List<Integer> setBaseline() throws IOException {

        //takes a picture and saves it as imageData
        controlLights(lightingPinOne, LOW);
        controlLights(lightingPinTwo, LOW);
        takePicture();

        //saves spectrumData to baseLineSpectrum and records pixel locations
        return findBaseline();

    }

    //post: takes an image using the camera and returns a List<Integer> with the intensity values 
    //      of the spectrum
    public List<Integer> setSampleSpectrum() throws IOException {

        if (specPoints.isEmpty()) {
            System.out.println("Need to set baseline prior to taking sample spectrum");
            System.out.println();
            throw new IllegalStateException();
        }

        controlLights(lightingPinOne, LOW);
        controlLights(lightingPinTwo, LOW);
        //takes a picture and saves it as bufferedImage
        takePicture();

        List<Integer> spectrumData = getSpectrumData(imageData);

        return spectrumData;

    }

    //pre: a digital GPIO output pin and the PinState to set it to
    //post: sets the GPIO pin to the passed state
    private void controlLights(GpioPinDigitalOutput pin, PinState state) {

        pin.setState(state);

    }

    private void findBaselineHelper(BufferedImage spectrumImage, int height,
            Map<Integer, Integer> heightAndValues) {

        if (height < spectrumImage.getHeight()) {

            boolean saturated = false;

            int maxRGB = 0;

            for (int i = 0; i < spectrumImage.getWidth() / 2; i++) {

                int[] RGB = getRGB(imageData.getRGB(i, height));

                if (RGB[1] > maxRGB) {
                    maxRGB = RGB[1];
                }

                if (RGB[0] >= 255 || RGB[1] >= 255 || RGB[2] >= 255) {
                    saturated = true;

                }

            }

            if (saturated) {
                heightAndValues.put(height, 0);
            } else {
                heightAndValues.put(height, maxRGB);
            }

            findBaselineHelper(spectrumImage, height + 1, heightAndValues);

        }

    }

    private List<Integer> findBaseline() {

        List<Integer> intensityValues = new ArrayList<>();

        for (int i = 0; i < imageData.getWidth() / 2; i++) {

            Object pixel = colorData.getDataElements(i, imageData.getHeight() / 2, null);

            int grayValue = ((byte[]) pixel)[0] & 0xff;

            intensityValues.add(grayValue);
            specPoints.add(new Point(i, imageData.getHeight() / 2));

        }

        return intensityValues;

    }

}
