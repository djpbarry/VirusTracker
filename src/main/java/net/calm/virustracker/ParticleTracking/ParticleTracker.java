package net.calm.virustracker.ParticleTracking;

import fiji.plugin.trackmate.tracking.jaqaman.LAPUtils;
import net.calm.adapt.Adapt.Analyse_Movie;
import net.calm.goshtasbycalibration.Multi_Goshtasby;
import net.calm.iaclasslibrary.Cell.CellData;
import net.calm.iaclasslibrary.Extrema.MultiThreadedMaximaFinder;
import net.calm.iaclasslibrary.IAClasses.ProgressDialog;
import net.calm.iaclasslibrary.IAClasses.Region;
import net.calm.iaclasslibrary.IAClasses.Utils;
import net.calm.iaclasslibrary.IO.DataWriter;
import net.calm.iaclasslibrary.IO.PropertyWriter;
import net.calm.iaclasslibrary.Math.Optimisation.IsoGaussianFitter;
import net.calm.iaclasslibrary.Particle.*;
import net.calm.iaclasslibrary.Segmentation.RegionGrower;
import net.calm.iaclasslibrary.Trajectory.DiffusionAnalyser;
import net.calm.iaclasslibrary.UtilClasses.GenUtils;
import net.calm.iaclasslibrary.UtilClasses.GenVariables;
import net.calm.iaclasslibrary.UtilClasses.Utilities;
import net.calm.trackerlibrary.ParticleTracking.ParticleTrajectory;
import net.calm.trackerlibrary.ParticleTracking.TrackMateTracker;
import net.calm.trackerlibrary.ParticleTracking.UserVariables;
import net.calm.virustracker.Detection.Blob_Detector;
import net.calm.virustracker.ui.ResultsPreviewInterface;
import net.calm.virustracker.ui.VirusTrackerUI;
import net.calm.virustracker.ParticleAnalysis.BeadCalibration;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.tracking.TrackerKeys;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.plugin.Straightener;
import ij.plugin.TextReader;
import ij.plugin.filter.GaussianBlur;
import ij.process.AutoThresholder;
import ij.process.Blitter;
import ij.process.ByteProcessor;
import ij.process.FloatBlitter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.StackStatistics;
import ij.process.TypeConverter;
import ij.text.TextWindow;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.swing.JFileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Timelapse_Analysis seeks to identify individual particles in a stack of
 * images and plot their trajectories. Particles are first identified in
 * individual colour bands at different time-points by searching for local
 * maxima above a certain threshold, fitting IsoGaussian curves about these
 * maxima, associating detected particles between images and constructing
 * trajectories.
 *
 * @author David J Barry
 * @version 2.0, FEB 2011
 */
public class ParticleTracker {

//    protected double[] SIGMAS;
    public final int GOSHTASBY_M = 2, GOSHTASBY_N = 4;
//    public static final int VERSION = 5;
    private final double NORM_VAL = 0.99999;
    protected ArrayList<ParticleTrajectory> trajectories; //Trajectories of the detected particles
    protected long startTime;
    protected DecimalFormat numFormat = new DecimalFormat("0.000");
    protected DecimalFormat intFormat = new DecimalFormat("000");
    protected String title, ext;
    protected final String C_0 = String.format(".%s_C0_temp", title), C_1 = "C1";
    private final double TRACK_WIDTH = 5.0;
    public final float TRACK_EXT = 5.0f;
    public final float TRACK_OFFSET = 5.0f;
    protected static File c0Dir, c1Dir,
            calDir = new File("C:\\Users\\barry05\\Desktop\\2016.03.10_PP1\\Cal");
    protected final String delimiter = GenUtils.getDelimiter();
    protected ImagePlus[] inputs;
    protected final String labels[] = {"Channel 1", "Channel 2"};
    protected static boolean gpuEnabled = false;
    private final double PART_RAD = 2.0;
    private static File calFile;
    protected double normFactor;
    private final double NOISE = 0.0001;
    protected Properties props;
    private String inputName;
    protected final String PARTICLE_RESULTS_HEADINGS = String.format("Particle\tFrame\tTime (s)\tX (%cm)\tY (%cm)\tC1 Intensity\tC2 Intensity", IJ.micronSymbol, IJ.micronSymbol);
    private final String RESULTS_SUMMARY_HEADINGS = "Particle\tDuration (s)\tDisplacement (" + IJ.micronSymbol
            + "m)\tVelocity (" + IJ.micronSymbol + "m/s)\tDirectionality\tDiffusion Coefficient ("
            + IJ.micronSymbol + "m^2/s)";

    public ParticleTracker() {
    }

    public ParticleTracker(String title, ImagePlus[] inputs, Properties props) {
        this.title = title;
        this.inputs = inputs;
        this.props = props;
    }

    public ParticleTracker(ImageStack[] stacks) {
        this.inputs = new ImagePlus[]{new ImagePlus("C1", stacks[0]), new ImagePlus("C1", stacks[1])};
    }

    public ParticleTracker(ImagePlus imp, String ext) {
//        this.imp = imp;
//        this.stacks = imp.getImageStack();
        this.ext = ext;
    }

    public void run(String arg) {
        if (inputs == null) {
            inputs = new ImagePlus[2];
            if (IJ.getInstance() != null) {
                getActiveImages(true);
            } else {
                inputs[0] = IJ.openImage();
                inputs[1] = IJ.openImage();
            }
        }
//        File inputDir = null;
        inputName = inputs[0].getTitle();
//        readParamsFromImage();
//        inputDir = buildStacks(true);
//        if (inputDir == null) {
//            return;
//        }
        analyse(new File(IJ.getDirectory("current")));
//        cleanUp();
        IJ.log(String.format("%s - done", title));
        IJ.showStatus(String.format("%s - done", title));
    }

    protected File buildStacks(boolean sameSize) {
        String dirName = prepareInputs(sameSize);
        File inputDir;
        if (dirName != null) {
            inputDir = new File(dirName);
        } else {
            return null;
        }
        c0Dir = new File(inputDir.getAbsolutePath() + delimiter + C_0);
        inputs[0] = Utils.buildStack(c0Dir);
        this.ext = inputs[0].getTitle();
        c1Dir = new File(inputDir.getAbsolutePath() + delimiter + C_1);
        if (c1Dir.exists()) {
            inputs[1] = Utils.buildStack(c1Dir);
        }
        return inputDir;
    }

    protected void cleanUp() {
        try {
            FileUtils.deleteDirectory(c0Dir);
            FileUtils.deleteDirectory(c1Dir);
        } catch (Exception e) {
            IJ.error(e.toString());
        }
    }

    protected String prepareInputs(boolean sameSize) {
        ImagePlus cytoImp = inputs[0].duplicate();
        if (cytoImp == null) {
            return null;
        }
        ImagePlus sigImp = null;
        if (inputs[1] != null) {
            sigImp = inputs[1].duplicate();
        } else {
            inputs[1] = null;
        }
        ImageStack cytoStack = cytoImp.getImageStack();
        int cytoSize = cytoStack.getSize();
        ImageStack sigStack = null;
        if (sigImp != null) {
            sigStack = sigImp.getImageStack();
            if (sameSize && sigStack.getSize() != cytoSize) {
                IJ.error("Stacks must contain same number of slices.");
                return null;
            }
        }

        return normaliseStacks(cytoStack, sigStack);
    }

    protected String normaliseStacks(ImageStack cytoStack, ImageStack sigStack) {
        ImagePlus cytoImp = new ImagePlus("", cytoStack);
        int cytoSize = cytoStack.getSize();
        StackStatistics cytoStats = new StackStatistics(cytoImp);
        double stackMin = cytoStats.min;
        for (int i = 1; i <= cytoSize; i++) {
            cytoStack.getProcessor(i).subtract(stackMin);
        }
        StackStatistics cytoStats2 = new StackStatistics(cytoImp, 256, 0.0, 0.0);
        int histogram[] = cytoStats2.histogram16;
        if (histogram == null) {
            histogram = cytoStats2.histogram;
            normFactor = 1.0 / cytoStats2.binSize;
        }
        double sum = 0.0;
        int nPixels = cytoStats2.pixelCount;
        int thresh = 0;
        while (sum < nPixels * NORM_VAL && thresh < histogram.length) {
            sum += histogram[thresh++];
        }
        normFactor = 100.0 / thresh;
        cytoImp = GenUtils.convertStack(cytoImp, 32);
        cytoStack = cytoImp.getImageStack();
        for (int i = 1; i <= cytoSize; i++) {
            cytoStack.getProcessor(i).multiply(normFactor);
        }
        String parent = IJ.getDirectory("current");
        File seriesFolder = GenUtils.createDirectory(parent + FilenameUtils.getBaseName(cytoImp.getTitle()), false);
        File c0Folder = GenUtils.createDirectory(seriesFolder.getAbsolutePath() + delimiter + C_0, true);
        Utils.saveStackAsSeries(cytoStack, c0Folder.getAbsolutePath() + delimiter,
                "TIF", intFormat);
        if (sigStack != null) {
            File c1Folder = GenUtils.createDirectory(seriesFolder.getAbsolutePath() + delimiter + C_1, true);
            Utils.saveStackAsSeries(sigStack, c1Folder.getAbsolutePath() + delimiter,
                    "TIF", intFormat);
        }
        return seriesFolder.getAbsolutePath();
    }

    /**
     * Analyses the {@link ImageStack} specified by <code>stack</code>.
     */
    public void analyse(File inputDir) {
        ImageStack[] stacks = getAllStacks();
        File outputDir = null;
        try {
            outputDir = Utilities.getFolder(inputDir, "Specify directory for output files...", true);
        } catch (Exception e) {
            GenUtils.logError(e, "Failed to create output directory.");
        }
        String parentDir = GenUtils.openResultsDirectory(outputDir + delimiter + title + "-" + inputName);
        String sigc0Dir = GenUtils.openResultsDirectory(parentDir + delimiter + "C0");
        String sigc1Dir = GenUtils.openResultsDirectory(parentDir + delimiter + "C1");
        String msdDir = GenUtils.openResultsDirectory(parentDir + delimiter + "MSD_Data");
        String trajDir = GenUtils.openResultsDirectory(parentDir + delimiter + "Trajectory_Data");
        ParticleTrajectory.resetMSDPlot();
        if (!(stacks[1] == null) && UserVariables.isUseCals()) {
            JFileChooser fileChooser = new JFileChooser(calDir);
            fileChooser.setDialogTitle("Specify file containing bead calibration data");
            fileChooser.showOpenDialog(null);
            calFile = fileChooser.getSelectedFile();
            calDir = calFile.getParentFile();
            if (!(new Multi_Goshtasby()).run(calFile, BeadCalibration.HEADER_SIZE)) {
                UserVariables.setUseCals(false);
                System.out.println("Calculation of calibration coefficients failed - proceeding without calibration.");
            }
        }
        if (stacks != null) {
            UserVariables.setFitC2(false);
            IJ.register(this.getClass());
            startTime = System.currentTimeMillis();
            trajectories = new ArrayList();
            updateTrajs(findParticles(), UserVariables.getSpatialRes());
            TextWindow results = new TextWindow(title + " Results", PARTICLE_RESULTS_HEADINGS,
                    new String(), 1000, 500);
            TextWindow resultSummary = new TextWindow(title + " Results Summary",
                    RESULTS_SUMMARY_HEADINGS,
                    new String(), 1200, 500);
            int n = trajectories.size();
            IJ.log("Filtering trajectories...");
            for (int i = 0; i < n; i++) {
                ParticleTrajectory traj = (ParticleTrajectory) trajectories.get(i);
                if (!(traj.getDisplacement(traj.getEnd(), traj.getSize()) > UserVariables.getMinTrajDist()
                        && traj.getNumberOfFrames() / UserVariables.getTimeRes() > UserVariables.getMinTrajLength()
                        && checkTrajColocalisation(traj, UserVariables.getColocalThresh(), UserVariables.isColocal()))) {
                    trajectories.remove(i);
                    i--;
                    n--;
                }
            }
//            if (UserVariables.isPrevRes()) {
//                ArrayList<Integer> includeList = previewResults();
//                if (includeList != null) {
//                    ArrayList<ParticleTrajectory> temps = new ArrayList();
//                    for (Integer e : includeList) {
//                        temps.add(trajectories.get(e));
//                    }
//                    trajectories = new ArrayList();
//                    trajectories.addAll(temps);
//                }
//            }
            n = trajectories.size();
            DiffusionAnalyser da = new DiffusionAnalyser(true);
            da.resetMSDPlot();
            double[][] msdData = null;
            IJ.log("Analysing trajectories...");
            for (int i = 0; i < n; i++) {
                IJ.log(String.format("Analysing trajectory %d of %d", (i + 1), n));
                IJ.showStatus(String.format("Analysing trajectory %d of %d", (i + 1), n));
                boolean remove = false;
                ParticleTrajectory traj = (ParticleTrajectory) trajectories.get(i);
                if (traj != null) {
                    msdData = da.calcMSD(-1, i + 1, traj.getInterpolatedPoints(), UserVariables.getMinMSDPoints(), UserVariables.getTimeRes());
                    try {
                        DataWriter.saveValues(DataWriter.transposeValues(msdData),
                                new File(String.format("%s%s%s", msdDir, File.separator, String.format("MSDPlotData_Particle_%d.csv", (i + 1)))),
                                new String[]{"Time (s)", "Mean", "SD", "N"}, null, false);
                    } catch (Exception e) {
                    }
                    traj.setDiffCoeff(da.getDiffCoeff());
                    printData(i, resultSummary, i + 1);
                    traj.printTrajectory(i + 1, results, numFormat, title, inputs);
                    if (stacks[1] != null && UserVariables.isExtractsigs()) {
                        ImageStack signals[] = extractTrajSignalValues(traj,
                                (int) Math.round(UserVariables.getTrackLength() / UserVariables.getSpatialRes()),
                                (int) Math.round(TRACK_WIDTH / UserVariables.getSpatialRes()),
                                TRACK_EXT / ((float) UserVariables.getSpatialRes()), stacks[0].getWidth(), stacks[0].getHeight(), i + 1);
                        if (signals[0].getSize() > 0) {
                            for (int j = 1; j <= signals[0].getSize(); j++) {
                                IJ.saveAs((new ImagePlus("", signals[0].getProcessor(j))),
                                        "TIF", sigc0Dir + delimiter + "C0-" + String.valueOf(i + 1)
                                        + "-" + signals[0].getSliceLabel(j));
                                IJ.saveAs((new ImagePlus("", signals[1].getProcessor(j))),
                                        "TIF", sigc1Dir + delimiter + "C1-" + String.valueOf(i + 1)
                                        + "-" + signals[1].getSliceLabel(j));
                            }
                        } else {
                            remove = true;
                        }
                    }
                } else {
                    remove = true;
                }
                if (remove) {
                    trajectories.remove(i);
                    i--;
                    n--;
                }
            }
            ParticleTrajectory.drawGlobalMSDPlot();
            IJ.log("Generating outputs...");
            if (trajectories.size() > 0) {
                inputs[0].setOverlay(mapTrajectories(trajectories, UserVariables.getSpatialRes(), true, 0, trajectories.size() - 1, 1, calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getSigEstRed()), stacks[0].getSize()));
                inputs[0].show();
                inputs[0].draw();
//                RoiManager roim = RoiManager.getRoiManager();
//                roim.setOverlay(inputs[0].getOverlay());
//                if (!roim.runCommand("Save", String.format("%s%s%s", parentDir, File.separator, "trajectories.zip"))) {
//                    GenUtils.logError(null, "Error saving trajectory overlay.");
//                }
                resultSummary.append("\nAnalysis Time (s): " + numFormat.format((System.currentTimeMillis() - startTime) / 1000.0));
                results.setVisible(true);
                resultSummary.setVisible(true);
//                IJ.saveString(results.getTextPanel().getText(), parentDir + "/results.txt");
                Plot msdPlot = DiffusionAnalyser.getMsdPlot();
                try {
                    DataWriter.saveTextWindow(resultSummary, new File(String.format("%s%s%s", parentDir, File.separator, "result_summary.csv")), RESULTS_SUMMARY_HEADINGS);
                    printTrajectories(trajectories, trajDir, stacks[0].size());
                    if (msdPlot != null) {
                        IJ.saveAs(msdPlot.makeHighResolution("", 10.0f, true, false), "PNG", msdDir + "/MSD_Plot");
                    }
                } catch (IOException e) {
                    GenUtils.logError(e, "Error analysing trajectories.");
                }
            } else {
                IJ.log("No Particle Trajectories Constructed.");
            }
        }
        try {
            PropertyWriter.saveProperties(props, parentDir, title, true);
        } catch (IOException e) {
            GenUtils.logError(e, "Failed to create properties file.");
        }
    }

    protected ParticleArray findParticles() {
        ImageStack[] stacks = getAllStacks();
        return findParticles(0, stacks[0].getSize() - 1, UserVariables.getCurveFitTol(), stacks[0], stacks[1]);
    }

    /**
     * Median filter and IsoGaussian filter the image specified by
     * <code>processor</code>.
     *
     * @param processor the image to be pre-processed.
     */
    protected ImageProcessor preProcess(ImageProcessor processor, double sigma) {
        if (processor == null) {
            return null;
        }
        ImageProcessor fp = (new TypeConverter(processor, false)).convertToFloat(null);
        fp.noise(NOISE);
        if (UserVariables.isPreProcess()) {
            (new GaussianBlur()).blurGaussian(fp, sigma, sigma, 0.1);
        }
        return fp;
    }

    public ParticleArray findParticles(int startSlice, int endSlice, double c1FitTol, ImageStack channel1, ImageStack channel2) {
        return findParticles(startSlice, endSlice, c1FitTol,
                channel1, channel2, false, UserVariables.isFitC2(), true);
    }

    public ParticleArray findParticles(int startSlice, int endSlice, double c1FitTol, ImageStack channel1, ImageStack channel2, boolean floatingSigma, boolean fitC2, boolean verbose) {
        if (verbose) {
            IJ.log("Finding particles...");
        }
        if (channel1 == null) {
            return null;
        }
        int i, noOfImages = channel1.getSize(),
                arraySize = endSlice - startSlice + 1;
        int searchRad;
        if (UserVariables.getDetectionMode() == UserVariables.GAUSS) {
            searchRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getSigEstRed());
        } else {
            searchRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getBlobSize());
        }
        ParticleArray particles = new ParticleArray(arraySize);
        for (i = startSlice; i < noOfImages && i <= endSlice; i++) {
            particles.addDetection(i, null);
            IJ.freeMemory();
            if (verbose) {
                IJ.log(String.format("Searching slice %d of %d", (i - startSlice + 1), arraySize));
                IJ.showStatus(String.format("Searching slice %d of %d", (i - startSlice + 1), arraySize));
            }
            ImageProcessor ip1 = channel1.getProcessor(i + 1).convertToFloat();
            ImageProcessor ip2 = (channel2 != null) ? channel2.getProcessor(i + 1).convertToFloat() : null;
            FloatProcessor ip2Proc = ip2 != null ? ip2.convertToFloatProcessor() : null;
            if (UserVariables.isPreProcess()) {
                ip2Proc = ip2 != null ? (FloatProcessor) preProcess(ip2.duplicate(), UserVariables.getFilterRadius()) : null;
            }
            if (UserVariables.getDetectionMode() == UserVariables.BLOBS) {
                detectHessianBlobs(i, particles, fitC2, ip1, ip2);
            } else {
                ByteProcessor thisC2Max = null;
                if (ip2Proc != null) {
                    thisC2Max = Utils.findLocalMaxima(searchRad, searchRad, UserVariables.FOREGROUND, ip2Proc, getThreshold(ip2Proc, UserVariables.getC2ThreshMethod()), false, 0);
                }
                FloatProcessor ip1Proc = ip1.convertToFloatProcessor();
                if (UserVariables.isPreProcess()) {
                    ip1Proc = (FloatProcessor) preProcess(ip1.duplicate(), UserVariables.getFilterRadius());
                }
                ByteProcessor thisC1Max = Utils.findLocalMaxima(searchRad, searchRad, UserVariables.FOREGROUND, ip1Proc, getThreshold(ip1Proc, UserVariables.getC1ThreshMethod()), false, 0);
                if (UserVariables.getDetectionMode() == UserVariables.GAUSS) {
                    detectGaussians(i, particles,
                            floatingSigma, fitC2, c1FitTol, thisC1Max, ip1Proc, ip2Proc, thisC2Max, searchRad);
                } else {
                    storeMaximaAsParticles(i, particles, thisC1Max, ip1Proc, ip2Proc, searchRad, thisC2Max);
                }
            }
        }
        return particles;
    }

    public void detectBlobs(int i, ParticleArray particles, boolean fitC2, ImageProcessor c1Proc, ImageProcessor c2Proc, ByteProcessor thisC2Max) {
        int width = c1Proc.getWidth();
        int height = c1Proc.getHeight();
        int searchRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getBlobSize());
        int pSize = 2 * searchRad + 1;
        ByteProcessor C1Max = getLogMaxima(pSize, c1Proc.duplicate(), UserVariables.getC1ThreshMethod());
        if (c2Proc != null && fitC2) {
            thisC2Max = getLogMaxima(pSize, c2Proc.duplicate(), UserVariables.getC2ThreshMethod());
        }
        for (int c1X = 0; c1X < width; c1X++) {
            for (int c1Y = 0; c1Y < height; c1Y++) {
                if (C1Max.getPixel(c1X, c1Y) == UserVariables.FOREGROUND) {
                    double px = c1X * UserVariables.getSpatialRes();
                    double py = c1Y * UserVariables.getSpatialRes();
                    Blob p1 = new Blob(i, px, py, c1Proc.getPixelValue(c1X, c1Y));
                    Point p2 = null;
                    if (c2Proc != null) {
                        p2 = searchForMaximum(i, c1X, c1Y, searchRad, thisC2Max, c2Proc);
                    }
                    p1.setColocalisedParticle(p2);
                    if (p2 != null) {
                        p1.putFeature(Particle.COLOCALISED, 0.0);
                    } else {
                        p1.putFeature(Particle.COLOCALISED, 1.0);
                    }
                    particles.addDetection(i, p1);
                }
            }
        }
        if (UserVariables.isTrackRegions()) {
            findRegions(particles.getLevel(i), i, c1Proc);
        }
    }

    public void detectHessianBlobs(int i, ParticleArray particles, boolean fitC2, ImageProcessor c1Proc, ImageProcessor c2Proc) {
        ByteProcessor thisC2Max = new ByteProcessor(c1Proc.getWidth(), c1Proc.getHeight());
        int searchRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getBlobSize());
        ImagePlus imp = new ImagePlus("", c1Proc);
        Calibration cal = new Calibration();
        cal.pixelHeight = UserVariables.getSpatialRes();
        cal.pixelWidth = UserVariables.getSpatialRes();
        imp.setCalibration(cal);

        MultiThreadedMaximaFinder maxFinder = new MultiThreadedMaximaFinder(null);
        Properties props = new Properties();
        String[] propLabels = new String[MultiThreadedMaximaFinder.N_PROP_LABELS];
        propLabels[MultiThreadedMaximaFinder.HESSIAN_START_SCALE] = "Start";
//        propLabels[MultiThreadedMaximaFinder.HESSIAN_STOP_SCALE] = "Stop";
//        propLabels[MultiThreadedMaximaFinder.HESSIAN_SCALE_STEP] = "Step";
        propLabels[MultiThreadedMaximaFinder.HESSIAN_THRESH] = "Threshold";
        propLabels[MultiThreadedMaximaFinder.HESSIAN_ABS] = "Absolute";
        props.setProperty(propLabels[MultiThreadedMaximaFinder.HESSIAN_START_SCALE], String.valueOf(UserVariables.getBlobSize() / UserVariables.getSpatialRes()));
//        props.setProperty(propLabels[MultiThreadedMaximaFinder.HESSIAN_STOP_SCALE], String.valueOf(UserVariables.getBlobSize() / UserVariables.getSpatialRes()));
//        props.setProperty(propLabels[MultiThreadedMaximaFinder.HESSIAN_SCALE_STEP], "1.0");
        props.setProperty(propLabels[MultiThreadedMaximaFinder.HESSIAN_THRESH],   String.valueOf(UserVariables.getBlobThresh()));
        props.setProperty(propLabels[MultiThreadedMaximaFinder.HESSIAN_ABS], "false");
        maxFinder.setup(null, props, propLabels);
        maxFinder.hessianDetection(imp);
        ArrayList<int[]> c1Maxima = maxFinder.getMaxima();

        if (c2Proc != null && fitC2) {
            maxFinder = new MultiThreadedMaximaFinder(null);
            maxFinder.setup(null, props, propLabels);
            maxFinder.hessianDetection(new ImagePlus("", c2Proc));
            ArrayList<int[]> c2Maxima = maxFinder.getMaxima();
            for (int[] p : c2Maxima) {
                thisC2Max.putPixelValue(p[0], p[1], c2Proc.getPixelValue(p[0], p[1]));
            }
        }

        for (int[] p : c1Maxima) {
            double px = p[0] * UserVariables.getSpatialRes();
            double py = p[1] * UserVariables.getSpatialRes();
            Blob p1 = new Blob(i, px, py, c1Proc.getPixelValue(p[0], p[1]));
            Point p2 = null;
            if (c2Proc != null) {
                p2 = searchForMaximum(i, p[0], p[1], searchRad, thisC2Max, c2Proc);
            }
            p1.setColocalisedParticle(p2);
            if (p2 != null) {
                p1.putFeature(Particle.COLOCALISED, 0.0);
            } else {
                p1.putFeature(Particle.COLOCALISED, 1.0);
            }
            particles.addDetection(i, p1);
        }
        if (UserVariables.isTrackRegions()) {
            findRegions(particles.getLevel(i), i, c1Proc);
        }
    }

    Point searchForMaximum(int index, int x, int y, int searchRad, ByteProcessor localMaxImage, ImageProcessor ip) {
        Point point = null;
        int[][] localMax = Utils.searchNeighbourhood(x, y, searchRad, UserVariables.FOREGROUND, localMaxImage);
        if (localMax != null) {
            double px = localMax[0][0] * UserVariables.getSpatialRes();
            double py = localMax[0][1] * UserVariables.getSpatialRes();
            point = new Point(index, px, py, ip.getPixelValue(localMax[0][0], localMax[0][1]));
        }
        return point;
    }

    protected double getThreshold(ImageProcessor image, String threshMethod) {
        ImageStatistics stats = image.getStatistics();
        int tIndex = (new AutoThresholder()).getThreshold(AutoThresholder.Method.valueOf(threshMethod), stats.histogram);
        return (int) Math.round(stats.histMin + stats.binSize * tIndex);
    }

    void findRegions(ArrayList<Particle> particles, int frame, ImageProcessor inputProc) {
        Analyse_Movie am = new Analyse_Movie();
        PointRoi points = null;
        int N = particles.size();
        for (Particle p : particles) {
            if (points == null) {
                points = new PointRoi(p.getX(), p.getY());
            } else {
                points.addPoint(p.getX(), p.getY());
            }
        }
        ArrayList<CellData> cd = new ArrayList();
        RegionGrower.initialiseROIs(null, -1, frame + 1, null, points, inputProc.getWidth(), inputProc.getHeight(), frame + 1, cd, null, false, false);
        int t = RegionGrower.getThreshold(inputProc, true, -1, AutoThresholder.Method.Default.toString());
        ArrayList<Region> regions = RegionGrower.findCellRegions(inputProc, t, am.getCellData());
        for (int i = 0; i < cd.size(); i++) {
            Region[] allRegions = new Region[inputs[0].getImageStackSize()];
            allRegions[frame] = regions.get(i);
            cd.get(i).setCellRegions(allRegions);
            cd.get(i).setGreyThresholds(new int[]{t});
        }
        try {
            am.getMorphologyData(cd, false, Measurements.MEAN + Measurements.SHAPE_DESCRIPTORS, inputProc.duplicate(), UserVariables.getSigEstRed());
        } catch (Exception e) {
        }
        for (int i = 0; i < N; i++) {
            particles.get(i).setRegion(regions.get(i));
        }
    }

    ByteProcessor getLogMaxima(int pSize, ImageProcessor ip, String threshMethod) {
        ip.noise(NOISE);
        int searchRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getBlobSize());
        Blob_Detector bd1 = new Blob_Detector(UserVariables.getBlobSize(), pSize);
        ImageProcessor log1 = bd1.laplacianOfGaussian(ip);
        log1.invert();
        double t = getThreshold(log1, threshMethod);
        return Utils.findLocalMaxima(searchRad, searchRad, UserVariables.FOREGROUND, log1, t, false, 0);
    }

    public void detectGaussians(int i, ParticleArray particles, boolean floatingSigma, boolean fitC2Gaussian, double c1FitTol, ByteProcessor thisC1Max, FloatProcessor chan1Proc, ImageProcessor ip2, ByteProcessor thisC2Max, int searchRad) {
        int width = chan1Proc.getWidth();
        int height = chan1Proc.getHeight();
        int fitRad = calcParticleRadius(UserVariables.getSpatialRes(), UserVariables.getSigEstRed());
        int pSize = 2 * fitRad + 1;
        double[] xCoords = new double[pSize];
        double[] yCoords = new double[pSize];
        double[][] pixValues = new double[pSize][pSize];
        for (int c1X = 0; c1X < width; c1X++) {
            for (int c1Y = 0; c1Y < height; c1Y++) {
                if (thisC1Max.getPixel(c1X, c1Y) == UserVariables.FOREGROUND) {
                    Utils.extractValues(xCoords, yCoords, pixValues, c1X, c1Y, chan1Proc);
                    ArrayList<IsoGaussian> c1Fits = doFitting(xCoords, yCoords, pixValues,
                            floatingSigma, c1X, c1Y, fitRad, UserVariables.getSpatialRes(), i,
                            UserVariables.getSigEstRed() / UserVariables.getSpatialRes());
                    Particle p2 = null;
                    if (thisC2Max != null) {
                        p2 = searchForMaximum(i, c1X, c1Y, searchRad, thisC2Max, ip2);
                        if (fitC2Gaussian) {
                            Utils.extractValues(xCoords, yCoords, pixValues,
                                    (int) Math.round(p2.getX() / UserVariables.getSpatialRes()),
                                    (int) Math.round(p2.getY() / UserVariables.getSpatialRes()), ip2);
                            ArrayList<IsoGaussian> c2Fits = doFitting(xCoords, yCoords, pixValues,
                                    floatingSigma, c1X, c1Y, fitRad, UserVariables.getSpatialRes(),
                                    i, UserVariables.getSigEstGreen() / UserVariables.getSpatialRes());
                            p2 = c2Fits.get(0);
                            if (((IsoGaussian) p2).getFit() < c1FitTol) {
                                p2 = null;
                            }
                        }
                    }
                    if (c1Fits != null) {
                        for (IsoGaussian c1Fit : c1Fits) {
                            if (c1Fit.getFit() > c1FitTol) {
                                c1Fit.setColocalisedParticle(p2);
                                if (p2 != null) {
                                    c1Fit.putFeature(Particle.COLOCALISED, 0.0);
                                } else {
                                    c1Fit.putFeature(Particle.COLOCALISED, 1.0);
                                }
                                particles.addDetection(i, c1Fit);
                            }
                        }
                    }
                }
            }
        }
    }

    public void storeMaximaAsParticles(int i, ParticleArray particles, ByteProcessor thisC1Max, FloatProcessor chan1Proc, ImageProcessor ip2, int searchRad, ByteProcessor thisC2Max) {
        int width = chan1Proc.getWidth();
        int height = chan1Proc.getHeight();
        for (int c1X = 0; c1X < width; c1X++) {
            for (int c1Y = 0; c1Y < height; c1Y++) {
                if (thisC1Max.getPixel(c1X, c1Y) == UserVariables.FOREGROUND) {
                    Point p1 = new Point(i, c1X * UserVariables.getSpatialRes(), c1Y * UserVariables.getSpatialRes(), chan1Proc.getPixelValue(c1X, c1Y));
                    Point p2 = null;
                    if (ip2 != null) {
                        p2 = searchForMaximum(i, c1X, c1Y, searchRad, thisC2Max, ip2);
                    }
                    p1.setColocalisedParticle(p2);
                    if (p2 != null) {
                        p1.putFeature(Particle.COLOCALISED, 0.0);
                    } else {
                        p1.putFeature(Particle.COLOCALISED, 1.0);
                    }
                    particles.addDetection(i, p1);
                }
            }
        }
    }

    protected ArrayList<IsoGaussian> doFitting(double[] xCoords, double[] yCoords, double[][] pixValues, boolean floatingSigma, int x0, int y0, int fitRad, double spatialRes, int t, double sigma) {
        IsoGaussianFitter fitter = new IsoGaussianFitter(xCoords, yCoords, pixValues, floatingSigma, sigma);
        fitter.doFit();
        ArrayList<IsoGaussian> fits = new ArrayList();
        double x = (x0 - fitRad + fitter.getX0()) * spatialRes;
        double y = (y0 - fitRad + fitter.getY0()) * spatialRes;
        fits.add(new IsoGaussian(t, x, y, fitter.getMag(), fitter.getXsig(),
                fitter.getYsig(), fitter.getRSquared() > 0.0 ? fitter.getRSquared() : 0.0, null, -1, null));
        return fits;
    }

    public void updateTrajsForPreview(ParticleArray spotArray) {
        trajectories = new ArrayList();
        Map<Integer, Set<Spot>> spotMap = spotArray.getContent();
        Iterator<Integer> frameIterator = spotMap.keySet().iterator();
        while (frameIterator.hasNext()) {
            Iterator<Spot> spotIterator = (spotMap.get(frameIterator.next())).iterator();
            while (spotIterator.hasNext()) {
                Spot s = spotIterator.next();
                ParticleTrajectory traj = new ParticleTrajectory();
                Particle p = null;
                double x = s.getFeature(Spot.POSITION_X);
                double y = s.getFeature(Spot.POSITION_Y);
                double t = s.getFeature(Spot.FRAME);
                if (s instanceof Point) {
                    p = new Point((int) t, x, y, 0.0);
                } else if (s instanceof Blob) {
                    p = new Blob((int) t, x, y, 0.0);
                } else if (s instanceof IsoGaussian) {
                    p = new IsoGaussian((int) t, new IsoGaussian(x, y, 0.0, s.getFeature(Spot.RADIUS), s.getFeature(Spot.RADIUS), s.getFeature(Spot.QUALITY)));
                }
                traj.addPoint(p);
                trajectories.add(traj);
            }
        }
    }

    public void updateTrajs(ParticleArray particles, double spatialRes) {
        TrackMateTracker tm = new TrackMateTracker();
        tm.track(SpotCollection.fromMap(particles.getContent()), constructTrackMateSettings());
        tm.updateTrajectories(trajectories);
//            TrajectoryBuilder.updateTrajectories(particles, UserVariables.getTimeRes(), UserVariables.getTrajMaxStep(), spatialRes, Utils.getStackMinMax(inputs[0].getImageStack())[1], trajectories, UserVariables.isTrackRegions());
//            TrajectoryBridger.bridgeTrajectories(trajectories, new double[]{0.0, 0.0, 1.0}, UserVariables.getMaxFrameGap());
    }

    Map<String, Object> constructTrackMateSettings() {
        Map<String, Object> settings = LAPUtils.getDefaultSegmentSettingsMap();
        settings.put(TrackerKeys.KEY_ALLOW_GAP_CLOSING, true);
        settings.put(TrackerKeys.KEY_ALLOW_TRACK_MERGING, false);
        settings.put(TrackerKeys.KEY_ALLOW_TRACK_SPLITTING, false);
        settings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_DISTANCE, UserVariables.getTrajMaxStep());
        settings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_FRAME_GAP, UserVariables.getMaxFrameGap());
        settings.put(TrackerKeys.KEY_LINKING_MAX_DISTANCE, UserVariables.getTrajMaxStep());
        return settings;
    }

    /**
     * Produces a {@link Plot} of the trajectory specified by
     * <code>particleNumber</code>.
     *
     * @param width the width of the images from which the trajectory was
     * extracted
     * @param height the height of the images from which the trajectory was
     * extracted
     * @param particleNumber the trajectory index
     */
    public boolean plotTrajectory(int width, int height, int particleNumber, int label) {
        if (width * height == 0 || trajectories.size() < 1) {
            return false;
        }
        ParticleTrajectory traj = (ParticleTrajectory) (trajectories.get(particleNumber));
        if (traj == null) {
            return false;
        }
        Particle current = traj.getEnd();
        int size = traj.getSize();
        double xValues[] = new double[size];
        double yValues[] = new double[size];
        width *= UserVariables.getSpatialRes();
        height *= UserVariables.getSpatialRes();

        for (int i = size - 1; i >= 0; i--) {
            xValues[i] = (double) current.getX() / width;
            /*
             * Y-coordinates are inverted so trajectory is displayed as per the
             * image
             */
            yValues[i] = -(double) current.getY() / height;
            current = current.getLink();
        }

        Plot plot = new Plot("Particle " + label + " Trajectory",
                "X", "Y", xValues, yValues,
                (Plot.X_TICKS + Plot.Y_TICKS + Plot.X_NUMBERS + Plot.Y_NUMBERS));
        plot.setLimits(0, 1.0, -1.0, 0);
        plot.setLineWidth(2);
        plot.setColor(Color.BLUE);
        plot.draw();
        plot.show();

        return true;
    }

    /**
     * Outputs velocity and directionality data on the particle specified by
     * <code>particleNumber</code>. Directionality ( <code>D</code>) is
     * calculated according to: <br> <br>
     * <code>D = 1 / (1 + &lambda;<sub>1</sub>
     * &lambda;<sub>2</sub><sup>-1</sup>)</code>
     * <br> <br> where <code>&lambda;<sub>1</sub></code> and
     * <code>&lambda;<sub>2</sub></code> are the eigenvalues of the trajectory
     * data and      <code>&lambda;<sub>1</sub>&lambda;<sub>2</sub></code>.
     *
     */
    public boolean printData(int particleNumber, TextWindow output, int label) {
        if (trajectories.size() < 1) {
            return false;
        }
        DecimalFormat decFormat = new DecimalFormat("0.000");
        DecimalFormat msdFormat = new DecimalFormat("0.000000");
        ParticleTrajectory traj = (ParticleTrajectory) (trajectories.get(particleNumber));
        if (traj == null) {
            return false;
        }
        double points[][] = traj.getInterpolatedPoints();
        traj.calcDirectionality(points[0], points[1]);
        double displacement = traj.getDisplacement(traj.getEnd(), traj.getSize());
        double duration = traj.getNumberOfFrames() / UserVariables.getTimeRes();
        output.append(label + "\t"
                + decFormat.format(duration) + "\t"
                + decFormat.format(displacement)
                + "\t" + decFormat.format(displacement / duration) + "\t"
                + decFormat.format(traj.getDirectionality()) + "\t"
                + msdFormat.format(traj.getDiffCoeff()));
        return true;
    }

    /**
     * Constructed trajectories are drawn onto the original image sequence and
     * displayed as a stack sequence.
     */
    public Overlay mapTrajectories(ArrayList<ParticleTrajectory> trajectories, double spatialRes, boolean tracks, int startT, int endT, int index, float radius, int frames) {
        int n = trajectories.size();
        Overlay overlay = new Overlay();
        if (n < 1) {
            return null;
        }
        Random r = new Random();
        int tLength = (int) Math.round(UserVariables.getTrackLength() / UserVariables.getSpatialRes());
        ProgressDialog progress = new ProgressDialog(null, "Mapping Output...", false, title, false);
        progress.setVisible(true);
        for (int i = startT; i <= endT && i < n; i++) {
            progress.updateProgress(i, n);
            ParticleTrajectory traj = (ParticleTrajectory) (trajectories.get(i));
            if (traj != null) {
                Color thiscolor = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
                Particle current = traj.getEnd();
                double lastX = current.getX();
                double lastY = current.getY();
                int lastTP = current.getFrameNumber();
                current = current.getLink();
                while (current != null) {
                    for (int j = frames - 1; j >= lastTP; j--) {
                        if (j - 1 < lastTP) {
                            markParticle(lastX / spatialRes - radius,
                                    lastY / spatialRes - radius, radius, true, "" + index, overlay, thiscolor, j + 1);
                        }
                        if (tracks && j <= lastTP + tLength) {
                            Line line = new Line(current.getX() / spatialRes, current.getY() / spatialRes, lastX / spatialRes,
                                    lastY / spatialRes);
                            line.setPosition(j + 1);
                            line.setStrokeColor(thiscolor);
                            overlay.add(line);
                        }
                    }
                    lastX = current.getX();
                    lastY = current.getY();
                    lastTP = current.getFrameNumber();
                    current = current.getLink();
                }
                markParticle(lastX / spatialRes - radius,
                        lastY / spatialRes - radius, radius, true, "" + index, overlay, thiscolor, lastTP + 1);
                index++;
            }
        }
        progress.dispose();
        return overlay;
    }

    void markParticle(double x, double y, float radius, boolean string, String label, Overlay overlay, Color color, int position) {
        OvalRoi oval = new OvalRoi(x, y, 2 * radius + 1, 2 * radius + 1);
        oval.setStrokeColor(color);
        oval.setPosition(position);
        overlay.add(oval);
        if (string) {
            TextRoi text = new TextRoi(x + radius, y + 2 * radius, label, new Font("Helvetica", Font.PLAIN, (int) Math.round(radius)));
            text.setStrokeColor(color);
            text.setPosition(position);
            overlay.add(text);
        }
    }

    public int calcParticleRadius(double spatialRes) {
        return calcParticleRadius(spatialRes, UserVariables.getSigEstRed());
    }

    public int calcParticleRadius(double spatialRes, double sigEst) {
        return (int) Math.ceil(PART_RAD * sigEst / spatialRes);
    }

    public ArrayList<Integer> previewResults() {
        if (trajectories.size() < 1) {
            return null;
        }
        ResultsPreviewInterface previewUI = new ResultsPreviewInterface(IJ.getInstance(), true, title, this);
        previewUI.setVisible(true);
        if (previewUI.isWasOKed()) {
            return previewUI.getRemoveList();
        } else {
            return null;
        }
    }

    public ArrayList<ParticleTrajectory> getTrajectories() {
        return trajectories;
    }

    public ImageStack[] getAllStacks() {
        ImageStack[] stacks = new ImageStack[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            stacks[i] = inputs[i] != null ? inputs[i].getImageStack() : null;
        }
        return stacks;
    }

    public ImageStack getFociStack() {
        return inputs[0].getImageStack();
    }

    public ImageStack getFociColocStack() {
        return inputs[1] != null ? inputs[1].getImageStack() : null;
    }

    public boolean isMonoChrome() {
        return inputs[1] == null;
    }

    public ImagePlus[] getInputs() {
        return inputs;
    }

    /**
     * Procedure for obtaining Goshtasby coefficients - see Goshtasby (1988),
     * IEEE Transactions on Geoscience and Remote Sensing, 26:60-64
     *
     * 1. "Track" beads in two channels to provide list of coordinates 2. Input
     * list of coordinates for each channel to MATLAB 3. Run goshtasby.m to
     * obtain translation coefficients for both x and y, mapping 'green'
     * coordinates onto 'red'. 4. Export x-coeffs, y-coeffs and original green
     * channel coords (assuming virus is in red).
     *
     * @param ptraj
     * @param signalLength
     * @param signalWidth
     * @return
     */
    ImageStack[] extractTrajSignalValues(ParticleTrajectory ptraj, int signalLength, int signalWidth, float offset, int width, int height, int count) {
        ImageStack[] stacks = getAllStacks();
        String calParent = calDir + delimiter + "goshtasby" + delimiter + GOSHTASBY_M + "_" + GOSHTASBY_N;
        TextReader reader = new TextReader();
        double xdiv = width * UserVariables.getSpatialRes() / GOSHTASBY_M;
        double ydiv = height * UserVariables.getSpatialRes() / GOSHTASBY_N;
        Particle sigStartP = ptraj.getEnd();
        if (signalWidth % 2 == 0) {
            signalWidth++;
        }
        int size = ptraj.getSize();
        float xSigArray[], ySigArray[], xVirArray[], yVirArray[];
        ArrayList<Float> xSigPoints, ySigPoints, xVirPoints, yVirPoints;
        ImageProcessor[] sigTemps = new ImageProcessor[size];
        ImageProcessor[] virTemps = new ImageProcessor[size];
        Particle last = null, next;
//        ProgressDialog progress = new ProgressDialog(null, "Extracting Signal Areas " + count + "...", false, title, false);
//        progress.setVisible(true);
        double radius = calcParticleRadius(UserVariables.getSpatialRes(),
                UserVariables.getSigEstRed()) * UserVariables.getSpatialRes();
        for (int i = 0; i < size; i++) {
//            progress.updateProgress(i, size);
            Particle current = sigStartP;
            next = sigStartP.getLink();
            xSigPoints = new ArrayList();
            ySigPoints = new ArrayList();
            xVirPoints = new ArrayList();
            yVirPoints = new ArrayList();
            double x1, y1, x2, y2, t1, t2;
            if (last != null) {
                x1 = last.getX();
                y1 = last.getY();
                t1 = last.getFrameNumber() / UserVariables.getTimeRes();
            } else {
                x1 = sigStartP.getX();
                y1 = sigStartP.getY();
                t1 = sigStartP.getFrameNumber() / UserVariables.getTimeRes();
            }
            if (next != null) {
                x2 = next.getX();
                y2 = next.getY();
                t2 = next.getFrameNumber() / UserVariables.getTimeRes();
            } else {
                x2 = sigStartP.getX();
                y2 = sigStartP.getY();
                t2 = sigStartP.getFrameNumber() / UserVariables.getTimeRes();
            }
            double vel;
            if (t1 - t2 > 0.0) {
                vel = Utils.calcDistance(x1, y1, x2, y2) / (t1 - t2);
            } else {
                vel = 0.0;
            }
            for (int index = 1; index <= size && current != null; index++) {
                double xg = current.getX();
                double yg = current.getY();
                if (UserVariables.isUseCals()) {
                    double x = current.getX(), y = current.getY();
                    int xi = 1 + (int) Math.floor(x / xdiv);
                    int yi = 1 + (int) Math.floor(y / ydiv);
                    if (new File(calParent + delimiter + "xcoeffs" + xi + "_" + yi + ".txt").exists()) {
                        ImageProcessor xcoeffs = reader.open(calParent + delimiter + "xcoeffs" + xi + "_" + yi + ".txt");
                        ImageProcessor ycoeffs = reader.open(calParent + delimiter + "ycoeffs" + xi + "_" + yi + ".txt");
                        ImageProcessor coords = reader.open(calParent + delimiter + "coords" + xi + "_" + yi + ".txt");
                        if (xcoeffs == null || ycoeffs == null || coords == null) {
                            GenUtils.error("Incomplete calibration parameters - proceeding without.");
                            UserVariables.setUseCals(false);
                        } else {
                            xg = goshtasbyEval(xcoeffs, coords, x, y);
                            yg = goshtasbyEval(ycoeffs, coords, x, y);
                        }
                    }
                }
                xSigPoints.add((float) (xg / UserVariables.getSpatialRes()));
                ySigPoints.add((float) (yg / UserVariables.getSpatialRes()));
                xVirPoints.add((float) (current.getX() / UserVariables.getSpatialRes()));
                yVirPoints.add((float) (current.getY() / UserVariables.getSpatialRes()));
                current = current.getLink();
            }
            xSigArray = new float[xSigPoints.size() + 2];
            ySigArray = new float[ySigPoints.size() + 2];
            xVirArray = new float[xVirPoints.size() + 2];
            yVirArray = new float[yVirPoints.size() + 2];
            for (int j = 1; j < xSigPoints.size(); j++) {
                xSigArray[j] = xSigPoints.get(j - 1);
                ySigArray[j] = ySigPoints.get(j - 1);
                xVirArray[j] = xVirPoints.get(j - 1);
                yVirArray[j] = yVirPoints.get(j - 1);
            }
            extendSignalArea(xSigArray, ySigArray, offset, 1);
            extendSignalArea(xVirArray, yVirArray, offset, 1);
            PolygonRoi sigProi = new PolygonRoi(xSigArray, ySigArray, xSigArray.length, Roi.POLYLINE);
            PolygonRoi virProi = new PolygonRoi(xVirArray, yVirArray, xVirArray.length, Roi.POLYLINE);
            Straightener straightener = new Straightener();
            ImagePlus sigImp = new ImagePlus("", stacks[1].getProcessor(sigStartP.getFrameNumber() + 1));
            ImagePlus virImp = new ImagePlus("", stacks[0].getProcessor(sigStartP.getFrameNumber() + 1));
            sigImp.setRoi(sigProi);
            virImp.setRoi(virProi);
            sigTemps[size - 1 - i] = straightener.straighten(sigImp, sigProi, signalWidth);
            virTemps[size - 1 - i] = straightener.straighten(virImp, virProi, signalWidth);
            if (virTemps[size - 1 - i] != null) {
                virTemps[size - 1 - i].putPixelValue(0, 0, sigStartP.getFrameNumber());
                virTemps[size - 1 - i].putPixelValue(1, 0, vel);
            }
            last = sigStartP;
            sigStartP = sigStartP.getLink();
        }
//        progress.dispose();
        int xc = (int) Math.ceil(TRACK_OFFSET * offset);
        int yc = (signalWidth - 1) / 2;
        int outputWidth = (int) Math.round(signalLength + offset);
        ImageStack output[] = new ImageStack[2];
        output[0] = new ImageStack(outputWidth, signalWidth);
        output[1] = new ImageStack(outputWidth, signalWidth);

//        progress = new ProgressDialog(null, "Aligning Signal Areas " + count + "...", false, title, false);
//        progress.setVisible(true);
        for (int j = 0; j < size; j++) {
//            progress.updateProgress(j, size);
            if (virTemps[j] != null && sigTemps[j] != null && sigTemps[j].getWidth() >= outputWidth) {
                Particle alignmentParticle = null;
//                if (UserVariables.isUseCals()) {
                ImageStack virStack = new ImageStack(virTemps[j].getWidth(), virTemps[j].getHeight());
                virStack.addSlice(virTemps[j]);
//                IJ.saveAs(new ImagePlus("", virTemps[j]), "TIF", String.format("%s%s%s", "D:\\debugging\\particle_tracker_debug", File.separator, String.format("Pre-Align_%d", j)));
                ParticleArray particles = findParticles(0, 0, 0.0, virStack, null, false, false, false);
                ArrayList<Particle> detections = particles.getLevel(0);
                double mindist = Double.MAX_VALUE;
                int minindex = -1;
                for (int k = 0; k < detections.size(); k++) {
                    Particle p = detections.get(k);
                    double dist = Utils.calcDistance(p.getX(), p.getY(), xc * UserVariables.getSpatialRes(), yc * UserVariables.getSpatialRes());
                    if (dist < mindist && dist < radius) {
                        mindist = dist;
                        minindex = k;
                    }
                }
                if (minindex > -1) {
                    alignmentParticle = detections.get(minindex);
                }
//                }
                double xinc = 0.0;
                double yinc = 0.0;
                if (alignmentParticle != null) {
                    xinc = alignmentParticle.getFeature(Spot.POSITION_X) / UserVariables.getSpatialRes() - xc;
                    yinc = alignmentParticle.getFeature(Spot.POSITION_Y) / UserVariables.getSpatialRes() - yc;
                }
                String label = Float.toString(virTemps[j].getPixelValue(0, 0))
                        + "-" + numFormat.format(virTemps[j].getPixelValue(1, 0));
                virTemps[j].setInterpolate(true);
                virTemps[j].setInterpolationMethod(ImageProcessor.BICUBIC);
                sigTemps[j].setInterpolate(true);
                sigTemps[j].setInterpolationMethod(ImageProcessor.BICUBIC);
                virTemps[j].translate(-xinc, -yinc);
                sigTemps[j].translate(-xinc, -yinc);
//                    IJ.saveAs(new ImagePlus("", virTemps[j]), "TIF", String.format("%s%s%s", "D:\\debugging\\particle_tracker_debug", File.separator, String.format("Post-Align_%d", j)));
//                    System.out.println(String.format("%d apx:%f apy:%f xinc: %f yinc: %f",
//                            j, alignmentParticle.getFeature(Spot.POSITION_X) / UserVariables.getSpatialRes(),
//                            alignmentParticle.getFeature(Spot.POSITION_Y) / UserVariables.getSpatialRes(), xinc, yinc));
                FloatProcessor sigSlice = new FloatProcessor(outputWidth, signalWidth);
                FloatBlitter sigBlitter = new FloatBlitter(sigSlice);
                sigBlitter.copyBits(sigTemps[j], 0, 0, Blitter.COPY);
                output[1].addSlice(label, sigSlice);
                FloatProcessor virSlice = new FloatProcessor(outputWidth, signalWidth);
                FloatBlitter virBlitter = new FloatBlitter(virSlice);
                virBlitter.copyBits(virTemps[j], 0, 0, Blitter.COPY);
                output[0].addSlice(label, virSlice);
            }
        }
//        progress.dispose();
        return output;
    }

    void extendSignalArea(float[] xpoints, float[] ypoints, float dist, int window) {
        xpoints[0] = xpoints[1];
        ypoints[0] = ypoints[1];
        int l = xpoints.length;
        xpoints[l - 1] = xpoints[l - 2];
        ypoints[l - 1] = ypoints[l - 2];
        if (xpoints.length - 1 <= window || ypoints.length - 1 <= window) {
            return;
        }
        getExtension(xpoints, ypoints, dist, window, 0, 1);
        getExtension(xpoints, ypoints, dist, window, l - 1, l - 2);
    }

    void getExtension(float[] xpoints, float[] ypoints, float dist, int window, int index1, int index2) {
        float xdiff = 0.0f, ydiff = 0.0f;
        int a, b, inc;
        if (index1 < index2) {
            a = 1;
            b = a + window;
            inc = 1;
        } else {
            b = xpoints.length;
            a = xpoints.length - window;
            inc = -1;
        }
        for (int i = a; i < b; i++) {
            xdiff += xpoints[i] - xpoints[i + inc];
            ydiff += ypoints[i] - ypoints[i + inc];
        }
        float newX, newY;
        if (xdiff != 0.0) {
            float ratio = Math.abs(ydiff / xdiff);
            newX = dist / (float) Math.sqrt(1.0f + (float) Math.pow(ratio, 2.0f));
            newY = newX * ratio;
        } else {
            newX = 0.0f;
            newY = ydiff;
        }
        if (xdiff < 0.0) {
            xpoints[index1] = xpoints[index2] - newX;
        } else if (xdiff > 0.0) {
            xpoints[index1] = xpoints[index2] + newX;
        }
        if (ydiff < 0.0) {
            ypoints[index1] = ypoints[index2] - newY;
        } else if (ydiff > 0.0) {
            ypoints[index1] = ypoints[index2] + newY;
        }
    }

    void goshtasbyShiftEval(ImageProcessor xcoeffs, ImageProcessor ycoeffs, ImageProcessor coords) {
        for (int y = 0; y < 512; y++) {
            for (int x = 0; x < 256; x++) {
                double xg = goshtasbyEval(xcoeffs, coords, x * UserVariables.getSpatialRes(), y * UserVariables.getSpatialRes());
                System.out.print(" " + (xg - x * UserVariables.getSpatialRes()) + " ");
            }
            System.out.println();
        }
        System.out.println();
        for (int y = 0; y < 512; y++) {
            for (int x = 0; x < 256; x++) {
                double yg = goshtasbyEval(ycoeffs, coords, x * UserVariables.getSpatialRes(), y * UserVariables.getSpatialRes());
                System.out.print(" " + (yg - y * UserVariables.getSpatialRes()) + " ");
            }
            System.out.println();
        }
    }

    void goshtasbyErrorEval() {
        TextReader reader = new TextReader();
        ImageProcessor tcoords = reader.open(calDir + delimiter + "testcoords.txt");
        ImageProcessor xcoeffs = reader.open(calDir + delimiter + "xcoeffs.txt");
        ImageProcessor ycoeffs = reader.open(calDir + delimiter + "ycoeffs.txt");
        ImageProcessor coords = reader.open(calDir + delimiter + "coords.txt");
        int size = tcoords.getHeight();
        System.out.println("x,y,xg,yg");
        for (int i = 1; i <= size; i++) {
            double x = tcoords.getPixelValue(0, i - 1);
            double y = tcoords.getPixelValue(1, i - 1);
            double xg = goshtasbyEval(xcoeffs, coords, x, y);
            double yg = goshtasbyEval(ycoeffs, coords, x, y);
            System.out.println(x + "," + y + "," + xg + "," + yg + "");
        }
    }

    void multiGoshtasbyErrorEval(int m, int n, int width, int height) {
        double xdiv = width * UserVariables.getSpatialRes() / m;
        double ydiv = height * UserVariables.getSpatialRes() / n;
        TextReader reader = new TextReader();
        ImageProcessor tcoords = reader.open(calDir + delimiter + "testcoords.txt");
        int size = tcoords.getHeight();
        System.out.println(m + "_" + n);
        System.out.println("x,y,xg,yg");
        for (int i = 1; i <= size; i++) {
            double x = tcoords.getPixelValue(0, i - 1);
            double y = tcoords.getPixelValue(1, i - 1);
            int xi = 1 + (int) Math.floor(x / xdiv);
            int yi = 1 + (int) Math.floor(y / ydiv);
            ImageProcessor xcoeffs = reader.open(calDir + delimiter + "goshtasby"
                    + delimiter + m + "_" + n + delimiter + "xcoeffs" + xi + "_" + yi + ".txt");
            ImageProcessor ycoeffs = reader.open(calDir + delimiter + "goshtasby"
                    + delimiter + m + "_" + n + delimiter + "ycoeffs" + xi + "_" + yi + ".txt");
            ImageProcessor coords = reader.open(calDir + delimiter + "goshtasby"
                    + delimiter + m + "_" + n + delimiter + "coords" + xi + "_" + yi + ".txt");
            double xg = goshtasbyEval(xcoeffs, coords, x, y);
            double yg = goshtasbyEval(ycoeffs, coords, x, y);
            System.out.println(x + "," + y + "," + xg + "," + yg + "");
        }
    }

    double goshtasbyEval(ImageProcessor coeffs, ImageProcessor coords, double x, double y) {
        int l = coeffs.getHeight();
        double sum = 0.0;
        for (int i = 3; i < l; i++) {
//            if (Utils.calcDistance(x, y, coords.getPixelValue(0, i - 3), coords.getPixelValue(1, i - 3)) < 20.0) {
            double r = Math.pow((x - coords.getPixelValue(0, i - 3)), 2.0) + Math.pow((y - coords.getPixelValue(1, i - 3)), 2.0);
            if (r > 0.0) {
                double R = r * Math.log(r);
                sum = sum + coeffs.getPixelValue(0, i) * R;
//                System.out.println("x," + coords.getPixelValue(0, i - 3) + ",y," + coords.getPixelValue(1, i - 3) + ",c," + (coeffs.getPixelValue(0, i) * R));
            }
//            }
        }
        return coeffs.getPixelValue(0, 0) + coeffs.getPixelValue(0, 1) * x + coeffs.getPixelValue(0, 2) * y + sum;
    }

    protected void printParams(String dir) {
        File paramFile;
        PrintWriter paramStream;
        try {
            paramFile = new File(dir + delimiter + "params.csv");
            paramStream = new PrintWriter(new FileOutputStream(paramFile));
        } catch (FileNotFoundException e) {
            System.out.println("Error: Failed to create parameter file.\n");
            System.out.println(e.toString());
            return;
        }
        paramStream.println(title);
        paramStream.println(Utilities.getDate("dd/MM/yyyy HH:mm:ss"));
        paramStream.println();
        paramStream.println(VirusTrackerUI.getRedSigEstText() + "," + UserVariables.getSigEstRed());
        paramStream.println(VirusTrackerUI.getGreenSigEstText() + "," + UserVariables.getSigEstGreen());
        paramStream.println(VirusTrackerUI.getSpatResLabelText() + "," + UserVariables.getSpatialRes());
        paramStream.println(VirusTrackerUI.getChan1MaxThreshLabelText() + "," + UserVariables.getC1ThreshMethod());
        paramStream.println(VirusTrackerUI.getChan2MaxThreshLabelText() + "," + UserVariables.getC2ThreshMethod());
        paramStream.println(VirusTrackerUI.getCurveFitTolLabelText() + "," + UserVariables.getCurveFitTol());
        paramStream.println(VirusTrackerUI.getPreprocessToggleText() + "," + UserVariables.isPreProcess());
        paramStream.println(VirusTrackerUI.getGpuToggleText() + "," + UserVariables.isGpu());
        paramStream.println(VirusTrackerUI.getFpsLabelText() + "," + UserVariables.getTimeRes());
        paramStream.println(VirusTrackerUI.getMinTrajLengthLabelText() + "," + UserVariables.getMinTrajLength());
        paramStream.println(VirusTrackerUI.getMinTrajDistLabelText() + "," + UserVariables.getMinTrajDist());
        paramStream.println(VirusTrackerUI.getMinTrajMSDLabelText() + "," + UserVariables.getMsdThresh());
        paramStream.println(VirusTrackerUI.getMaxLinkDistLabelText() + "," + UserVariables.getTrajMaxStep());
        paramStream.println(VirusTrackerUI.getTrackLengthText() + "," + UserVariables.getTrackLength());
        paramStream.println(VirusTrackerUI.getColocalToggleText() + "," + UserVariables.isColocal());
//        paramStream.println(UserInterface.getPrevResToggleText() + "," + UserVariables.isPrevRes());
        paramStream.println(VirusTrackerUI.getExtractSigsToggleText() + "," + UserVariables.isExtractsigs());
        paramStream.println(VirusTrackerUI.getUseCalToggleText() + "," + UserVariables.isUseCals());
        paramStream.close();
    }

    public boolean isGpuEnabled() {
        return gpuEnabled;
    }

//    protected IsoGaussian getC2Gaussian(double x0, double y0, ImageProcessor ip2) {
//        if (ip2 == null) {
//            return null;
//        }
//        int rx = (int) Math.round(x0 / UserVariables.getSpatialRes());
//        int ry = (int) Math.round(y0 / UserVariables.getSpatialRes());
//        int width = (int) Math.round(1.0 / UserVariables.getSpatialRes());
//        int width2 = width * 2;
//        Rectangle r = new Rectangle(rx - width, ry - width, width2, width2);
//        IsoGaussian c2Gaussian = null;
//        ip2.setRoi(r);
//        ImageProcessor ip3 = ip2.crop();
//        FloatStatistics stats1 = new FloatStatistics(ip3, ImageStatistics.MEDIAN, null);
//        ip3.multiply(1.0 / stats1.median);
//        FloatStatistics stats2 = new FloatStatistics(ip3, ImageStatistics.MIN_MAX, null);
//        if (stats2.max >  UserVariables.getChan2MaxThresh()) {
//            c2Gaussian = new IsoGaussian(x0, y0, stats2.max * stats1.median, UserVariables.getSigEstGreen(), UserVariables.getSigEstGreen(), 0.0);
//        }
//        return c2Gaussian;
//    }
    boolean checkTrajColocalisation(ParticleTrajectory traj, double thresh, boolean colocal) {
        if (!colocal) {
            return true;
        } else if (traj.getType(thresh) == ParticleTrajectory.COLOCAL) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean getActiveImages(boolean sameLength) {
        ImageStack[] stacks = new ImageStack[2];
        if (IJ.getInstance() != null) {
            inputs = GenUtils.specifyInputs(labels);
            if (inputs == null) {
                return false;
            }
            stacks[0] = inputs[0].getImageStack();
            if (inputs[1] == null) {
            } else {
                stacks[1] = inputs[1].getImageStack();
            }
        }
        if (stacks[0].getProcessor(1).getNChannels() > 1 || (!(stacks[1] == null) && stacks[1].getProcessor(1).getNChannels() > 1)) {
            GenUtils.error("Monochrome images required.");
            return false;
        }
        if (!(stacks[1] == null) && sameLength && stacks[0].getSize() != stacks[1].getSize()) {
            GenUtils.error("Stacks must have same number of slices.");
            return false;
        }
        return true;
    }

    public void printTrajectories(ArrayList<ParticleTrajectory> trajectories, String outDir, int length) throws IOException, FileNotFoundException {
        String headings[] = PARTICLE_RESULTS_HEADINGS.split("\t", -1);
        int n = trajectories.size();
        ImageStack[] stacks = new ImageStack[2];
        stacks[0] = inputs[0].getImageStack();
        if (inputs[1] != null) {
            stacks[1] = inputs[1].getImageStack();
        } else {
            stacks[1] = null;
        }
        for (int i = 0; i < n; i++) {
            CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(String.format("%s%sTrajectory_%d.csv", outDir, File.separator, (i + 1))), GenVariables.ISO), CSVFormat.EXCEL);
            for (String heading : headings) {
                printer.print(heading);
            }
            printer.println();
            ParticleTrajectory pt = trajectories.get(i);
            Particle current = pt.getEnd();
            while (current != null) {
                printer.print(i + 1);
                double[] features = ParticleTrajectory.getFeatures(current, stacks);
                for (double f : features) {
                    printer.print(f);
                }
                printer.println();
                current = current.getLink();
            }
            printer.close();
        }
    }
}
