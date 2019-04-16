/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 *
 * @author nicho
 */
public class CameraController {

    private final String fileLocation;
    private BufferedImage imageData;
    private final List<Integer> baseLineSpectrum;  //sum of RGB values for media samples are in
    private final List<Point> specPoints;          //cartesian points at which spectrum is measured
    public final static ColorModel LINEAR_RGB = new DirectColorModel(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), 32, //linear RGB color space
            0x00FF0000, 0x0000FF00,
            0x000000FF, 0xFF000000, true,
            DataBuffer.TYPE_INT);
    private final static WritableRaster RASTER = LINEAR_RGB.createCompatibleWritableRaster(500, 500);

    public CameraController() {
        fileLocation = "/home/pi/Chromostat";
        baseLineSpectrum = new ArrayList<>();
        specPoints = new ArrayList<>();
        this.imageData = new BufferedImage(LINEAR_RGB, RASTER, false, null);

    }

    //post: creates a jpg from image taken by camera
    public void takePicture() throws IOException {

        if (this.imageData != null) {
            //resets the image so a new image can be written over it
            this.imageData.flush();
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
        imageCommands.add("" + width);  //the "" in front of the + converts the following int to a string
        imageCommands.add("-h");
        imageCommands.add("" + height);  //the "" in front of the + converts the following int to a string

        ProcessBuilder imageCapture = new ProcessBuilder(imageCommands); //creates process builder with the given imageCommands
        imageCapture.redirectErrorStream(true);

        Process imageCaptureController;

        imageCaptureController = imageCapture.start();
        this.imageData = ImageIO.read(imageCaptureController.getInputStream());
        imageCaptureController.getInputStream().close();

        int picWidth = imageData.getWidth();
        int picHeight = imageData.getHeight();
    }

    //pre: a 32 bit integer 
    //post: returns an array containg the Red, Green, and Blue color values from that pixel    
    public int[] getRGB(int pixel) {

        int[] RGB = new int[4];

        RGB[0] = (pixel >> 16) & 0xff; //gets bits 16-24 (which correspond to the red value)
        RGB[1] = (pixel >> 8) & 0xff; //gets bits 8-16 (which correspond to the green channel)
        RGB[2] = (pixel) & 0xff; //gets bits 0-8 (which correspond to the blue channel)
        RGB[3] = (pixel >> 24) & 0xff; //gets bits 24-32 (which correspond to the alpha channel)

        return RGB;
    }

    //pre: BufferedImage
    //post: returns an integer representing the y value of the pixel with the highest red value     
    public int findSpectrum(BufferedImage spectrumImage) {

        int picHeight = spectrumImage.getHeight();
        int picWidth = spectrumImage.getWidth();

        int[] RGB;
        int maxHeight = 0;
        int maxRed = 0;

        for (int i = 0; i < picHeight; i++) {       //the y coordinate of the image
            for (int j = 0; j < picWidth; j++) {    //the x coordinate of the image

                RGB = getRGB(spectrumImage.getRGB(j, i));  //getRGB needs (x,y) so order is (j,i)

                if (RGB[0] > maxRed) { //runs through entire picture to find highest red value
                    maxRed = RGB[0];   //sets the y value of this pixel as the y value of spectrum
                    maxHeight = i;
                }

            }
        }

        return maxHeight;

    }

    //pre: the y value of the row of pixels to be analyzed and the BufferedImage object to 
    //     analyze
    //post: adds the 32 bit pixel value of every 10th colored pixel to spectrumData
    public void findBaseline(BufferedImage spectrumImage, int searchHeight) {

        int picWidth = spectrumImage.getWidth();
        int[] RGB;

        for (int i = 0; i < picWidth; i += 5) {

            //searches every 10 pixels horizontally at the given y value and converts each pixel
            //to RGB values
            RGB = getRGB(spectrumImage.getRGB(i, searchHeight));

            //tests to see if the pixel at that location is colored (ie. not black)
            if (RGB[0] > 50 || RGB[1] > 50 || RGB[2] > 50) {

                //adds the sum of R,G, and B channels to passed spectrum List
                this.baseLineSpectrum.add(RGB[0] + RGB[1] + RGB[2]);
                this.specPoints.add(new Point(i, searchHeight));

            }
        }
    }

    public List<Integer> getSpectrumData(BufferedImage spectrumImage) {

        int[] RGB;

        List<Integer> spectrumData = new ArrayList<>();

        for (int i = 0; i < this.specPoints.size(); i++) {

            Point tempPoint = this.specPoints.get(i);
            RGB = getRGB(spectrumImage.getRGB(tempPoint.getX(), tempPoint.getY()));

            spectrumData.add(RGB[0] + RGB[1] + RGB[2]);

        }

        return spectrumData;
    }

    //post: takes an image using the camera, finds the spectrum, saves the values of the spectrum
    //      to the baseLine, and prints out the values
    public List<Integer> setBaseline() throws IOException {

        this.baseLineSpectrum.clear();

        //takes a picture and saves it as imageData
        takePicture();

        //saves spectrumData to baseLineSpectrum and records pixel locations
        findBaseline(this.imageData, findSpectrum(this.imageData));

        return this.baseLineSpectrum;

    }

    //post: takes an image using the camera and returns a List<Integer> with the intensity values 
    //      of the spectrum
    public List<Integer> setSampleSpectrum() throws IOException {

        //takes a picture and saves it as bufferedImage
        takePicture();

        List<Integer> spectrumData = getSpectrumData(this.imageData);

        return spectrumData;

    }

}
