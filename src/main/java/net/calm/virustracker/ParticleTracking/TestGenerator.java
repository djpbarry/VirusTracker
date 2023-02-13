/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.calm.virustracker.ParticleTracking;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.OvalRoi;
import ij.plugin.RoiRotator;
import ij.plugin.filter.GaussianBlur;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import net.calm.iaclasslibrary.IAClasses.Utils;
import net.calm.iaclasslibrary.Particle.IsoGaussian;
import net.calm.iaclasslibrary.Particle.Particle;
import net.calm.trackerlibrary.ParticleTracking.BlinkingFluorophore;
import net.calm.trackerlibrary.ParticleTracking.DecayingFluorophore;
import net.calm.trackerlibrary.ParticleTracking.Fluorophore;
import net.calm.trackerlibrary.ParticleTracking.MotileGaussian;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author barry05
 */
public class TestGenerator {

    DecimalFormat indFormat = new DecimalFormat("000");
    private double noise = 3.0;
    private double randNoise = 0.0;
    private double separation = 0.4;
    private Random rand = new Random();
    private double numAp = 1.4;
    private double lambda = 602.0;
    private double res = 0.133333 * 0.2;
    //    private double sigmaEstPix = 0.305 * lambda / (numAp * res * 1000.0);
    private double sigmaEstPix = 0.133 / res;
    private double sens = 0.02;

    //    public static void main(String args[]) {
//        ByteProcessor template = new ByteProcessor(1000, 250);
//        template.setValue(0.0);
//        template.fill();
//        template.setValue(255.0);
//        template.setFont(new Font("Serif", Font.BOLD, 25));
//        template.drawString("Listening to Dave talk about image analysis is ****ing riveting.", 50, 125);
//        TestGenerator tg = new TestGenerator();
//        tg.generateSwitchingSequenceFromBitMap(template, 1000, 0.002, 5.0, "C:/Users/barry05/Desktop/Test_Data_Sets/Test_Generator_Output");
//    }
//    public static void main(String args[]) {
//        TestGenerator tg = new TestGenerator();
//        tg.generateNuclei(20, 512, 512, 12, 24);
//        System.exit(0);
//    }
    public TestGenerator() {
    }

    public void generateColocalisationGrid() {
        DecimalFormat indFormat = new DecimalFormat("000");
        int width = 256, height = 512;
//        double res = Timelapse_Analysis.getSpatialRes();
        double res = 133.333, yinc = 5000, xinc = 5000, theta = 0.005;//, deltax = 100.0, deltay = 80.0;
        double xc = (width * res) / 2, yc = (height * res) / 2;
        double cnoise = xinc / 100.0, anoise = theta / 100.0;
        FloatProcessor image1 = new FloatProcessor(width, height);
        FloatProcessor image2 = new FloatProcessor(width, height);
        Random r = new Random();
        for (double x = xinc; x < width * res; x += xinc) {
            for (double y = yinc; y < height * res; y += yinc) {
                double x1 = x - cnoise / 2.0 + cnoise * r.nextDouble();
                double y1 = y - cnoise / 2.0 + cnoise * r.nextDouble();
                IsoGaussian g1 = new IsoGaussian(x1, y1, 100.0, 158.0 / res, 158.0 / res, 0.1);
                double x2 = (x - xc) * Math.cos(theta - anoise / 2.0 + anoise * r.nextDouble()) - (y - yc)
                        * Math.sin(theta - anoise / 2.0 + anoise * r.nextDouble()) + xc;
                double y2 = (x - xc) * Math.sin(theta - anoise / 2.0 + anoise * r.nextDouble()) + (y - yc)
                        * Math.cos(theta - anoise / 2.0 + anoise * r.nextDouble()) + yc;
                IsoGaussian g2 = new IsoGaussian(x2, y2, 100.0, 168.0 / res, 168.0 / res, 0.1);
                Utils.draw2DGaussian(image1, g1, 0.0, res, false);
                Utils.draw2DGaussian(image2, g2, 0.0, res, false);
                System.out.println(x1 / res + "," + y1 / res + "," + x2 / res + "," + y2 / res);
            }
        }
        IJ.saveAs(new ImagePlus("", image1), "TIF",
                "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\CalibrationGrid_C0");
        IJ.saveAs(new ImagePlus("", image2), "TIF",
                "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\CalibrationGrid_C1");
    }

    public void generateCalibrationTest(boolean random) {
        DecimalFormat indFormat = new DecimalFormat("000");
        int width = 256, height = 256;
//        double res = 133.333, theta = 0.005;
        double res = 133.333;
        double noise = 2500;
        double noise2 = noise / 2.0;
//        double xc = (width * res) / 2, yc = (height * res) / 2;
        FloatProcessor image1 = new FloatProcessor(width, height);
        FloatProcessor image2 = new FloatProcessor(width, height);
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            double x = width * r.nextDouble() * res;
            double y = height * r.nextDouble() * res;
//            double xt = (x - xc) * Math.cos(theta) - (y - yc) * Math.sin(theta) + xc;
//            double yt = (x - xc) * Math.sin(theta) + (y - yc) * Math.cos(theta) + yc;
            double xt, yt;
            if (random) {
                xt = width * r.nextDouble() * res;
                yt = height * r.nextDouble() * res;
            } else {
                xt = (x - noise2) + noise * r.nextDouble();
                yt = (y - noise2) + noise * r.nextDouble();
            }
//            System.out.println(x / 1000.0 + "," + y / 1000.0 + "," + xt / 1000.0 + "," + yt / 1000.0);
            IsoGaussian g1 = new IsoGaussian(x, y, 100.0, 3 * 158.0 / res, 3 * 158.0 / res, 0.1);
            IsoGaussian g2 = new IsoGaussian(xt, yt, 100.0, 3 * 168.0 / res, 3 * 168.0 / res, 0.1);
            Utils.draw2DGaussian(image1, g1, 0.0, res, false);
            Utils.draw2DGaussian(image2, g2, 0.0, res, false);
        }
        IJ.saveAs(new ImagePlus("", image1), "TIF",
                "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\ColocalTest_" + random + "_" + noise + "_C0");
        IJ.saveAs(new ImagePlus("", image2), "TIF",
                "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\ColocalTest_" + random + "_" + noise + "_C1");
    }

    public void generateMulti(int n, int width, int height, int length, String directory, boolean changeState, double vel, double D, int nChan, boolean persist, String filename, boolean useWeights) {
        ImageStack[] output = new ImageStack[nChan];
        double[][] weights = new double[nChan][n];
        for (int c = 0; c < nChan; c++) {
            output[c] = new ImageStack(width, height);
            Arrays.fill(weights[c], 1.0);
        }
        MotileGaussian[][] particles = new MotileGaussian[nChan][n];
        Random r = new Random();
        for (int j = 0; j < nChan; j++) {
            for (int i = 0; i < n; i++) {
                double x0 = width * res * r.nextDouble();
                double y0 = height * res * r.nextDouble();
                double dist = Math.sqrt(Math.pow(x0 / res - 450.0, 2.0) + Math.pow(y0 / res - 450.0, 2.0));
                double amp = 1.0 + (99.0 * r.nextDouble() * (1.0 - (dist / width)));
                //System.out.println("x: " + x0 + ", y: " + y0 + ", dist: " + dist +  ", amp: " + amp);
                double sig = sigmaEstPix + rand.nextGaussian() * 0.2 * sigmaEstPix;
                if (!changeState) {
                    particles[j][i] = new MotileGaussian(x0, y0,
                            amp, sig, sig, 0.1, sens, persist, changeState, D, vel + vel * r.nextGaussian() / 5.0);
                } else {
                    particles[j][i] = new MotileGaussian(x0, y0,
                            amp, sig, sig, 0.1, sens, r.nextBoolean(), changeState, D, vel);
                }
            }
        }//
        for (int i = 0; i < length; i++) {
            for (int c = 0; c < nChan; c++) {
                FloatProcessor slice = new FloatProcessor(width, height);
                for (int j = 0; j < n; j++) {
                    if (particles[c][j] != null) {
//                    System.out.println(String.format("x:%f, y:%f, m:%f", particles[j].getX()/res, particles[j].getY()/res, particles[j].getMagnitude()));
                        Utils.draw2DGaussian(slice, particles[c][j], 0.0, res, false);
//                    double projectedPos[] = particles[j].projectPosition(false, separation);
//                    IsoGaussian temp = new IsoGaussian(projectedPos[0], projectedPos[1], particles[j].getMagnitude(),
//                            particles[j].getXSigma(), particles[j].getYSigma(), particles[j].getFit());
//                    Utils.draw2DGaussian(c2image, temp, 0.0, res, false, false);
                        particles[c][j].updateVelocity();
                        particles[c][j].updatePosition(weights[c][j]);
                        double x = particles[c][j].getX() / res;
                        double y = particles[c][j].getY() / res;
                        if (x < -2.0 * particles[c][j].getXSigma()
                                || x > width + 2.0 * particles[c][j].getXSigma()
                                || y < -2.0 * particles[c][j].getYSigma()
                                || y > height + 2.0 * particles[c][j].getYSigma()) {
                            if (!changeState) {
                                particles[c][j] = new MotileGaussian(width * res * r.nextDouble(), height * res * r.nextDouble(),
                                        r.nextDouble() * 100.0 + 1.0, sigmaEstPix, sigmaEstPix, 0.1, sens, persist, changeState, D, vel + vel * r.nextGaussian() / 5.0);
                            } else {
                                particles[c][j] = new MotileGaussian(width * res * r.nextDouble(), height * res * r.nextDouble(),
                                        r.nextDouble() * 100.0 + 1.0, sigmaEstPix, sigmaEstPix, 0.1, sens, r.nextBoolean(), changeState, D, vel);
                            }

                        }
//                    System.out.println("X:\t" + particles[j].getX() + "\tY:\t" + particles[j].getY());
                    }
                }
//                slice.noise(randNoise);
                output[c].addSlice(slice);
//            System.out.println("Frame:\t" + i + "\tTotal Count:\t" + totalcount);
            }
            if (useWeights) {
                for (int p = 0; p < n; p++) {
                    for (int c1 = 0; c1 < nChan; c1++) {
                        weights[c1][p] = 0.0;
                        for (int c2 = 0; c2 < nChan; c2++) {
                            if (c1 != c2) {
                                weights[c1][p] += (100.0 - output[c2].getProcessor(i + 1).getInterpolatedValue(particles[c1][p].getX() / res, particles[c1][p].getY() / res)) / (100.0 * (nChan - 1));
                            }
                        }
                    }
                }
            }
        }
        for (int c = 0; c < nChan; c++) {
            IJ.saveAs(new ImagePlus("", output[c]), "TIF", String.format("%s%s%s_C_%s",
                    directory, File.separator, filename, indFormat.format(c)));
        }
    }

    public void generateBrownian(int n, int width, int height, int length, double D, String directory) {
        MotileGaussian particles[] = new MotileGaussian[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            particles[i] = new MotileGaussian(width * res * r.nextDouble(), height * res * r.nextDouble(),
                    r.nextDouble() * 100.0 + 1.0, sigmaEstPix, sigmaEstPix, 0.1, sens, false, false, D, 0.0);
        }
        for (int i = 0; i < length; i++) {
            FloatProcessor c1image = new FloatProcessor(width, height);
            c1image.setColor(100);
            for (int j = 0; j < n; j++) {
                if (particles[j] != null) {
                    Utils.draw2DGaussian(c1image, particles[j], 0.0, res, false);
                    particles[j].updatePosition(0.0);
                    double x = particles[j].getX() / res;
                    double y = particles[j].getY() / res;
                    if (x < -2.0 * particles[j].getXSigma()
                            || x > width + 2.0 * particles[j].getXSigma()
                            || y < -2.0 * particles[j].getYSigma()
                            || y > height + 2.0 * particles[j].getYSigma()) {
                        particles[j] = new MotileGaussian(width * res * r.nextDouble(),
                                height * res * r.nextDouble(), 100.0, sigmaEstPix, sigmaEstPix,
                                0.1, sens, false, false, D, 0.0);
                    }
                }
            }
            IJ.saveAs(new ImagePlus("", c1image.duplicate()), "TIF",
                    directory
                            + File.separator + indFormat.format(i));
        }
    }

    public void twoColocalised(int width, double sep, String directory) {
        MotileGaussian particles[] = new MotileGaussian[2];
        Random r = new Random();
        particles[0] = new MotileGaussian(width / 2 * res, width / 2 * res,
                90.0, sigmaEstPix, sigmaEstPix, 0.1, sens, true, false, 0.1, 0.0);
        particles[1] = new MotileGaussian(particles[0].getX() + sep * r.nextDouble(), particles[0].getY() + sep * r.nextDouble(),
                90.0, sigmaEstPix, sigmaEstPix, 0.1, sens, true, false, 0.1, 0.0);
        int width2 = width;
        double res2 = res;
        while (width2 > 1) {
            for (int j = 0; j < particles.length; j++) {
                if (particles[j] != null) {
                    FloatProcessor c1image = new FloatProcessor(width2, width2);
                    c1image.setColor(100);
                    Utils.draw2DGaussian(c1image, particles[j], 0.0, res2, false);
                    IJ.saveAs(new ImagePlus("", c1image.duplicate()), "TIF",
                            directory + "C" + j + "_" + indFormat.format(res2));
                }
            }
            res2 *= 2;
            width2 /= 2;
        }
    }

    public void generateFluorophoreCircle(int radius, int width, int height, int length,
                                          double finalRes, double thresh, String outputDir) {
        int circum = (int) Math.ceil(2.0 * Math.PI * radius);
        Fluorophore dots[] = new Fluorophore[circum];
        double sigma = 0.305f * 602.0 / 1.4;
        double theta;
        int cx = width / 2;
        int cy = height / 2;
        for (int i = 0; i < circum; i++) {
            theta = i * 2.0 * Math.PI / circum;
            double x = cx + radius * Math.cos(theta);
            double y = cy + radius * Math.sin(theta);
            dots[i] = new Fluorophore(x, y, 255.0, thresh);
//            System.out.println("x: " + x + " y: " + y + " theta: " + theta);
        }
        runGenerator(length, width, height, dots, sigma, finalRes, outputDir);
    }

    //    public void generateFilledFluorophoreCircle(int n, int width, int height,
//            int length, double finalRes) {
//        DecayingFluorophore dots[] = new DecayingFluorophore[n];
//        Random r = new Random();
//        double radius = width
//                / 15.0;
//        double res = 125.0 / radius;
//        double sigma = (0.305f * 602.0 / 1.4)
//                / res;
//        double maxNoise = width / 600.0;
//        int cx = width / 2;
//        int cy =
//                height / 2;
//        GaussianBlur gb = new GaussianBlur();
//        for (int i = 0; i < n;
//                i++) {
//            double theta = 2.0 * Math.PI * r.nextDouble();
//            double nradius =
//                    radius * (1.0 - Math.pow(r.nextDouble(), 2.0));
//            double x = cx + nradius
//                    * Math.cos(theta);
//            double y = cy + nradius * Math.sin(theta);
//            dots[i] = new DecayingFluorophore(x, y, 255.0, 0.05);
//        }
//        for (int i = 0; i < length;
//                i++) {
//            FloatProcessor image = new FloatProcessor(width, height);
//            image.setValue(0.0);
//            image.fill();
//            for (int j = 0; j < n; j++) {
//                dots[j].updateMag();
//                int x = (int) Math.round(dots[j].getX());
//                int y =
//                        (int) Math.round(dots[j].getY());
//                double mag = dots[j].getCurrentMag()
//                        + image.getPixelValue(x, y);
//                image.putPixelValue(x, y, mag);
//            }
//            IJ.saveAs(new ImagePlus("", image), "TIF", "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test      Sequences\\Simulation\\Original_" + indFormat.format(i));
//            gb.blurGaussian(image, sigma, sigma, 0.001);
//            image.setInterpolationMethod(ImageProcessor.BICUBIC);
//            IJ.saveAs(new ImagePlus("", image.resize((int) Math.round(width * res / finalRes))),
//                    "TIF", "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test      Sequences\\Simulation\\BlurredAndScaled_" + indFormat.format(i));
//        }
//    }
    public void generateFilledFluorophoreCircle(int n, int width, int height, int length, double finalRes) {
        DecayingFluorophore dots[] = new DecayingFluorophore[n];
        Random r = new Random();
        double radius = 150.0;
        double radius2 = radius * radius;
        double res = 1.0;
        double sigma = (0.305f * 602.0 / 1.4) / res;
        double scope = 2 * radius;
        int xc = width / 2;
        int yc = height / 2;
        GaussianBlur gb = new GaussianBlur();
        for (int i = 0; i < n; ) {
            double x = xc - radius + r.nextDouble() * scope;
            double y = yc - radius + r.nextDouble() * scope;
            if (Math.pow(x - xc, 2.0) + Math.pow(y - yc, 2.0) <= radius2) {
                dots[i] = new DecayingFluorophore(x, y, 255.0, 0.05);
                i++;
            }
        }
        for (int i = 0; i < length; i++) {
            FloatProcessor image = new FloatProcessor(width, height);
            image.setValue(0.0);
            image.fill();
            for (int j = 0; j < n; j++) {
                dots[j].updateMag();
                int x = (int) Math.round(dots[j].getX());
                int y = (int) Math.round(dots[j].getY());
                double mag = dots[j].getCurrentMag() + image.getPixelValue(x, y);
                image.putPixelValue(x, y, mag);
            }
            IJ.saveAs(new ImagePlus("", image), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test Sequences\\Simulation\\Original_"
                            + indFormat.format(i));
            gb.blurGaussian(image, sigma, sigma, 0.001);
            image.setInterpolationMethod(ImageProcessor.BICUBIC);
            IJ.saveAs(new ImagePlus("", image.resize((int) Math.round(width * res / finalRes))), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test Sequences\\Simulation\\BlurredAndScaled_"
                            + indFormat.format(i));
        }
    }

    public void generateFilledFluorophoreSquare(int dw, int dh, int width, int height,
                                                int length, double finalRes, String outputDir, double thresh) {
        Fluorophore dots[] = new Fluorophore[dw * dh];
        double sigma = 0.305f * lambda / numAp;
        int x0 = (width - dw) / 2;
        int y0 = (height - dh) / 2;
        for (int i = x0; i < x0 + dw; i++) {
            for (int j = y0; j < y0 + dh; j++) {
                dots[(j - y0) * dw + (i - x0)] = new Fluorophore(i, j, 255.0, thresh);
            }
        }
        runGenerator(length, width, height, dots, sigma, finalRes, outputDir);
    }

    public void generateFilament(int n, int m, int width, int height, int length, double finalRes) {
        DecayingFluorophore dots[] = new DecayingFluorophore[n];
        Random r = new Random();
        double radius = 250.0;
        double res = 1.0;
        double sigma = (0.305f * 602.0 / 1.4) / res;
        GaussianBlur gb = new GaussianBlur();
        double yc = height / 2.0 - radius;
        double yincs[] = new double[m];
        for (int k = 0; k < m; k++) {
            yincs[k] = k * 2.0 * radius / (m - 1);
        }
        for (int i = 0; i < n; i++) {
            double x = r.nextDouble() * width + noise * rand.nextGaussian();
            double y = yc + yincs[r.nextInt(m)] + noise * rand.nextGaussian();
            dots[i] = new DecayingFluorophore(x, y, 0.0, 0.0);
        }
        for (int i = 0; i < length; i++) {
            FloatProcessor image = new FloatProcessor(width, height);
            image.setValue(0.0);
            image.fill();
            for (int j = 0; j < n; j++) {
                dots[j].updateXMag(i);
                int x = (int) Math.round(dots[j].getX());
                int y = (int) Math.round(dots[j].getY());
                double mag = dots[j].getCurrentMag() + image.getPixelValue(x, y);
                image.putPixelValue(x, y, mag);
            }
            IJ.saveAs(new ImagePlus("", image), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test Sequences\\Simulation\\Original_"
                            + indFormat.format(i));
            gb.blurGaussian(image, sigma, sigma, 0.001);
            image.setInterpolationMethod(ImageProcessor.BICUBIC);
            IJ.saveAs(new ImagePlus("", image.resize((int) Math.round(width * res / finalRes))), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test Data Sets\\Tracking Test Sequences\\Simulation\\BlurredAndScaled_"
                            + indFormat.format(i));
        }
    }

    public void generateDeterministicGrid(int width, int height, int length, int sep, int border) {
        int m = (int) Math.round((width - 2.0 * border) / sep) + 1;
        int n = (int) Math.round((height - 2.0 * border) / sep) + 1;
        int size = m * n;
        DecayingFluorophore dots[] = new DecayingFluorophore[size];
        GaussianBlur gb = new GaussianBlur();
        double initialSig = 0.305 * lambda / numAp;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                dots[i + j * n] = new DecayingFluorophore(border + i * sep, border + j * sep, 255.0, 0.05);
            }
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < size; j++) {
                FloatProcessor image = new FloatProcessor(width, height);
                image.setValue(0.0);
                image.fill();
                dots[j].updateMag(dots[j].getInitialMag() * (1.0 - (double) i / length));
                for (int k = 0; k < size; k++) {
                    int x = (int) Math.round(dots[k].getX());
                    int y = (int) Math.round(dots[k].getY());
                    double mag = dots[k].getCurrentMag() + image.getPixelValue(x, y);
                    image.putPixelValue(x, y, mag);
//                    System.out.println("x: " + x + " y: " + y + " mag: " + mag);
                }
                ImageProcessor imageCopy = image.duplicate();
                IJ.saveAs(new ImagePlus("", imageCopy), "TIF",
                        "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\Original_"
                                + indFormat.format(i * size + j));
                gb.blurGaussian(imageCopy, initialSig, initialSig, 0.001);
                imageCopy.setInterpolationMethod(ImageProcessor.BICUBIC);
                IJ.saveAs(new ImagePlus("", imageCopy.resize((int) Math.round(width / (res * 1000.0)))), "TIF",
                        "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\BlurredAndScaled_"
                                + indFormat.format(i * size + j));
            }
        }
    }

    public void generateStochasticGrid(int width, int height, int length, int sep, int border) {
        int m = (int) Math.round((width - 2.0 * border) / sep) + 1;
        int n = (int) Math.round((height - 2.0 * border) / sep) + 1;
        int size = m * n;
        DecayingFluorophore dots[] = new DecayingFluorophore[size];
        GaussianBlur gb = new GaussianBlur();
        double initialSig = 0.305 * lambda / numAp;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                dots[i + j * n] = new DecayingFluorophore(border + i * sep, border + j * sep, 255.0, 0.05);
            }
        }
        for (int i = 0; i < length; i++) {
            FloatProcessor image = new FloatProcessor(width, height);
            image.setValue(0.0);
            image.fill();
            for (int j = 0; j < size; j++) {
                dots[j].updateMag();
                int x = (int) Math.round(dots[j].getX());
                int y = (int) Math.round(dots[j].getY());
                double mag = dots[j].getCurrentMag() + image.getPixelValue(x, y);
                image.putPixelValue(x, y, mag);
//                    System.out.println("x: " + x + " y: " + y + " mag: " + mag);
            }
            IJ.saveAs(new ImagePlus("", image), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\Original_"
                            + indFormat.format(i));
            gb.blurGaussian(image, initialSig, initialSig, 0.001);
            image.setInterpolationMethod(ImageProcessor.BICUBIC);
            IJ.saveAs(new ImagePlus("", image.resize((int) Math.round(width / (res * 1000.0)))), "TIF",
                    "C:\\Users\\barry05\\Desktop\\Test_Data_Sets\\Tracking_Test_Sequences\\Simulation\\BlurredAndScaled_"
                            + indFormat.format(i));
        }
    }

    public void generateSwitchingSequenceFromBitMap(ByteProcessor template, int length,
                                                    double onProb, double finalRes, String outputDir) {
        int width = template.getWidth();
        int height = template.getHeight();
        int nFluors = template.getStatistics().histogram[255];
        BlinkingFluorophore dots[] = new BlinkingFluorophore[nFluors];
        double initialSig = (0.305 * lambda / numAp) / finalRes;
        for (int j = 0, count = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (template.getPixel(i, j) > 0) {
                    dots[count] = new BlinkingFluorophore(i, j, 0.0, onProb);
                    count++;
                }
            }
        }
        runGenerator(length, width, height, dots, initialSig, finalRes, outputDir);
    }

    void runGenerator(int length, int width, int height, Fluorophore dots[],
                      double sigma, double finalRes, String outputDir) {
        FloatProcessor origImage = new FloatProcessor(width, height);
        origImage.setValue(0.0);
        origImage.fill();
        for (int i = 0; i < length; i++) {
            FloatProcessor blurImage = new FloatProcessor(width, height);
            blurImage.setValue(0.0);
            blurImage.fill();
            int n = dots.length;
            GaussianBlur gb = new GaussianBlur();
            for (int j = 0; j < n; j++) {
                int x = (int) Math.round(dots[j].getX());
                int y = (int) Math.round(dots[j].getY());
                double mag = dots[j].getCurrentMag() + blurImage.getPixelValue(x, y);
                blurImage.putPixelValue(x, y, mag);
                mag = dots[j].getCurrentMag();
                if (mag > 0.0) {
                    origImage.putPixelValue(x, y, mag);
                }
                dots[j].updateMag();
            }
            IJ.saveAs(new ImagePlus("", origImage), "TIF", outputDir + "\\Original_"
                    + indFormat.format(i));
            gb.blurGaussian(blurImage, sigma, sigma, 0.001);
            blurImage.setInterpolationMethod(ImageProcessor.BICUBIC);
            IJ.saveAs(new ImagePlus("", blurImage.resize((int) Math.round(width / finalRes))),
                    "TIF", outputDir + "\\BlurredAndScaled_" + indFormat.format(i));
        }
    }

    public double[][] generateNuclei(int n, int imageWidth, int imageHeight, int nWidth, int nHeight, String dir) {
        Random r = new Random();
        ShortProcessor c1image = new ShortProcessor(imageWidth, imageHeight);
        c1image.setColor(100.0);
        double[][] centres = new double[n][2];
        for (int i = 0; i < n; i++) {
            int x = r.nextInt(imageWidth);
            int y = r.nextInt(imageHeight);
            double width = nWidth + r.nextDouble() * nWidth;
            double height = nHeight + r.nextDouble() * nHeight;
            OvalRoi nucleus = new OvalRoi(x, y, width, height);
            c1image.fill(RoiRotator.rotate(nucleus, r.nextDouble() * 360.0));
            centres[i] = new double[]{x + width / 2.0, y + height / 2.0};
        }
        c1image.noise(10.0);
        IJ.saveAs(new ImagePlus("", c1image), "TIF", dir + "/nuclei.tif");
        return centres;
    }

    public ArrayList<Particle> generateMulti(int maxNPerCell, double maxDist, int width, int height, double[][] centres, String dir) {
        Random r = new Random();
        int nCentres = centres.length;
        ShortProcessor c1image = new ShortProcessor(width, height);
        c1image.setColor(100);
        ArrayList<Particle> output = new ArrayList();
        for (int n = 0; n < nCentres; n++) {
            int N = r.nextInt(maxNPerCell);
            for (int j = 0; j < N; j++) {
                double radius = Math.abs(r.nextGaussian() * maxDist);
                double theta = 2.0 * Math.PI * r.nextDouble();
                double px = centres[n][0] + radius * Math.cos(theta);
                double py = centres[n][1] + radius * Math.sin(theta);
                IsoGaussian particle = new IsoGaussian(px, py, 100.0 + 50.0 * r.nextGaussian(), 1.0);
                output.add(particle);
                Utils.draw2DGaussian(c1image, particle, 0.0, 1.0, false);
            }
        }
        c1image.noise(10.0);
        IJ.saveAs(new ImagePlus("", c1image.duplicate()), "TIF", dir + "/foci.tif");
        return output;
    }

    public void generateColocalisedParticles(int width, int height, ArrayList<Particle> particles, double colocFactor, String dir) {
        Random r = new Random();
        int N = particles.size();
        ShortProcessor c2image = new ShortProcessor(width, height);
        c2image.setColor(100);
        for (int n = 0; n < N; n++) {
            Particle p = particles.get(n);
            IsoGaussian p2 = new IsoGaussian(p.getX() + r.nextGaussian() * colocFactor,
                    p.getY() + r.nextGaussian() * colocFactor, 100.0 + 50.0 * r.nextGaussian(), 1.0);
            p.setColocalisedParticle(p2);
            Utils.draw2DGaussian(c2image, p2, 0.0, 1.0, false);
        }
        c2image.noise(10.0);
        IJ.saveAs(new ImagePlus("", c2image.duplicate()), "TIF", dir + "/coloc_foci.tif");
    }
}
