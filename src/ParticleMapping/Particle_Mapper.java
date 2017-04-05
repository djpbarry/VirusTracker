/*
 * Copyright (C) 2017 Dave Barry <david.barry at crick.ac.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ParticleMapping;

import IAClasses.Utils;
import Math.Histogram;
import ParticleTracking.Analyse_;
import ParticleTracking.Particle;
import ParticleTracking.ParticleArray;
import ParticleTracking.UserVariables;
import Revision.Revision;
import UtilClasses.GenVariables;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Plot;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.EDM;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author Dave Barry <david.barry at crick.ac.uk>
 */
public class Particle_Mapper extends Analyse_ {

    LinkedHashMap<Integer, double[]> regionCentroidMap = new LinkedHashMap();

    public Particle_Mapper() {

    }

    /**
     *
     * @param arg
     */
    public void run(String arg) {
        File inputDir;
        title = title + "_v" + Revision.VERSION + "." + intFormat.format(Revision.revisionNumber);
        stacks = new ImageStack[2];
        if (IJ.getInstance() != null) {
            if (!getActiveImages()) {
                return;
            }
        } else {
            inputDir = buildStacks();
            if (inputDir == null) {
                return;
            }
        }
//        if (!showDialog()) {
//            return;
//        }
        try {
            ParticleArray pa = findParticles();
            double[][] centroids = buildTerritories(IJ.openImage().getProcessor());
            double[][] distances = calcDistances(assignParticlesToCells(pa, IJ.openImage().getProcessor(), centroids));
            showHistograms(distances, 30, 20, 0);
            cleanUp();
        } catch (IOException e) {
            IJ.error("IOException");
        }
    }

    public double[][] buildTerritories(ImageProcessor image) throws IOException {
        ImagePlus imp = new ImagePlus("", image);
        ResultsTable rt = Analyzer.getResultsTable();
        ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.CENTROID, rt, 0, Double.MAX_VALUE);
        pa.setHideOutputImage(true);
        pa.analyze(imp);
        File centFile = new File("C:\\Users\\barryd\\particle_mapper_debug\\cell_centroids.csv");
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(centFile), GenVariables.ISO), CSVFormat.EXCEL);
        double[][] centroids = {rt.getColumnAsDoubles(rt.getColumnIndex("X")), rt.getColumnAsDoubles(rt.getColumnIndex("Y"))};
        int N = rt.size();
        printer.printRecord((Object[]) rt.getHeadings());
        for (int i = 0; i < N; i++) {
            printer.printRecord(centroids[0][i], centroids[1][i]);
        }
        printer.close();
        EDM edm = new EDM();
        edm.setup("voronoi", imp);
        edm.run(image);
        IJ.saveAs(new ImagePlus("", image), "PNG", "C:\\Users\\barryd\\particle_mapper_debug\\voronoi");
        image.threshold(1);
        IJ.saveAs(new ImagePlus("", image), "PNG", "C:\\Users\\barryd\\particle_mapper_debug\\voronoi_thresh");
        image.invert();
        rt.reset();
        pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_ROI_MASKS, 0, rt, 0, Double.MAX_VALUE);
        pa.setHideOutputImage(true);
        pa.analyze(imp);
        IJ.saveAs(pa.getOutputImage(), "PNG", "C:\\Users\\barryd\\particle_mapper_debug\\voronoi_indexed");
        return centroids;
    }

    ArrayList<Particle>[] assignParticlesToCells(ParticleArray pa, ImageProcessor cellMap, double[][] cellCentroids) {
        ByteProcessor map = new ByteProcessor(cellMap.getWidth(), cellMap.getHeight());
        map.setValue(0);
        map.fill();
        map.setValue(255);
        ArrayList<Particle> detections = pa.getLevel(0);
        int M = cellCentroids[0].length;
        for (int i = 0; i < M; i++) {
            int xc = (int) Math.round(cellCentroids[0][i]);
            int yc = (int) Math.round(cellCentroids[1][i]);
            int pIndex = cellMap.getPixel(xc, yc) - 1;
            regionCentroidMap.put(pIndex, new double[]{cellCentroids[0][i], cellCentroids[1][i]});
        }
        int N = detections.size();
        ArrayList<Particle>[] assignments = new ArrayList[M];
        for (int i = 0; i < N; i++) {
            Particle p = detections.get(i);
            int xp = (int) Math.round(p.getX() / UserVariables.getSpatialRes());
            int yp = (int) Math.round(p.getY() / UserVariables.getSpatialRes());
            int pIndex = cellMap.getPixel(xp, yp) - 1;
            if (assignments[pIndex] == null) {
                assignments[pIndex] = new ArrayList();
            }
            assignments[pIndex].add(p);
//            particleRegionMap.put(p, pIndex);
            double[] centroid = regionCentroidMap.get(pIndex);
            map.drawLine(xp, yp,
                    (int) Math.round(centroid[0]), (int) Math.round(centroid[1]));
        }
        IJ.saveAs(new ImagePlus("", map), "PNG", "C:\\Users\\barryd\\particle_mapper_debug\\map");
        return assignments;
    }

    double[][] calcDistances(ArrayList<Particle>[] assignments) {
        int N = assignments.length;
        double[][] distances = new double[N][];
        for (int i = 0; i < N; i++) {
            if (assignments[i] != null) {
                int M = assignments[i].size();
                distances[i] = new double[M];
                double[] centroid = regionCentroidMap.get(i);
                for (int j = 0; j < M; j++) {
                    Particle p = assignments[i].get(j);
                    distances[i][j] = Utils.calcDistance(centroid[0]*UserVariables.getSpatialRes(), centroid[1]*UserVariables.getSpatialRes(), p.getX(), p.getY());
                }
            }
        }
        return distances;
    }

    void showHistograms(double[][] distances, int nBins, double max, double min) {
        int N = distances.length;
        int[][] histograms = new int[N][nBins];
        float[] meanHistogram = new float[nBins];
        float[] bins = new float[nBins];
        float binSize = (float) (max - min) / nBins;
        for (int i = 0; i < N; i++) {
            if (distances[i] != null) {
                histograms[i] = Histogram.calcHistogram(distances[i], min, max, nBins);
            }
        }
        for (int i = 0; i < nBins; i++) {
            DescriptiveStatistics mean = new DescriptiveStatistics();
            for (int j = 0; j < N; j++) {
                if (distances[j] != null) {
                    mean.addValue((double) 100.0 * histograms[j][i] / distances[j].length);
                }
            }
            meanHistogram[i] = (float) mean.getMean();
            bins[i] = i * binSize;
        }
        Plot histPlot = new Plot("Mean Foci Distance Distribtion",
                "Distance ("+IJ.micronSymbol+"m)", "% Frequency");
        histPlot.setLineWidth(3);
        histPlot.setColor(Color.blue);
        histPlot.addPoints(bins, meanHistogram, Plot.CONNECTED_CIRCLES);
        histPlot.draw();
        histPlot.show();
    }
}