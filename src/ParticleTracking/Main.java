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
package ParticleTracking;

public class Main {

//    public static void main(String args[]) {
//        TestGenerator tg = new TestGenerator();
////        tg.twoColocalised(512, 10, "c:\\Users\\barryd\\debugging\\particle_sim_data\\");
//        int nParticles = 1;
//        double D = 0.01;
//        int nFrames = 1000;
//        double initVel = 0.5;
////        String dir1 = GenUtils.openResultsDirectory(String.format("C://Users/barryd/debugging/particle_sim_data/Brownian/%d_%d_%f", nParticles, nFrames, D));
////        String dir2 = GenUtils.openResultsDirectory(String.format("C://Users/barryd/debugging/particle_sim_data/Directed/%d_%d_%f_%f", nParticles, nFrames, D, initVel));
//        String dir3 = GenUtils.openResultsDirectory(String.format("C://Users/barryd/debugging/particle_sim_data/%d_%d_%f", nParticles, nFrames, D));
////        tg.generateBrownian(nParticles, 512, 512,nFrames, D, dir1);
//        tg.generateMulti(nParticles, 512, 512, nFrames, dir3, false, initVel, D);
////        tg.generateMulti(nParticles, 512, 512, nFrames, dir3, true);
////        tg.generateMulti(40, 10, 512, 512, tg.generateNuclei(10, 512, 512, 24, 36));
//        System.exit(0);
//    }
//    public static void main(String args[]) {
//        Particle_Colocaliser instance = new Particle_Colocaliser();
//        instance.run(null);
//        System.exit(0);
//    }
    public static void main(String args[]) {
        GPUAnalyse instance = new GPUAnalyse();
        instance.run(null);
        System.exit(0);
    }
//    public static void main(String args[]) {
//        Trajectory_Analyser ta = new Trajectory_Analyser(new File("D:\\OneDrive - The Francis Crick Institute\\Working Data\\Ultanir\\Control shRNA\\Particle Tracker_v5.170_Output\\results.txt"));
//        ta.run();
//        System.exit(0);
//    }
//    public static void main(String args[]) {
//        Particle_Mapper instance = new Particle_Mapper();
//        instance.run(null);
//        System.exit(0);
//    }
//    public static void main(String args[]) {
//        ImagePlus imp = IJ.openImage();
//        ArrayList<int[]> maxima = Utils.findLocalMaxima(5, imp.getImageStack(), 1.0, true, false, 3);
//        ImageStack output = new ImageStack(imp.getWidth(), imp.getHeight());
//        for(int i=1;i<=imp.getNSlices();i++){
//            ByteProcessor slice = new ByteProcessor(imp.getWidth(), imp.getHeight());
//            slice.setValue(0.0);
//            slice.fill();
//            slice.setValue(1.0);
//            output.addSlice(slice);
//        }
//        for(int[] p:maxima){
//            ImageProcessor slice = output.getProcessor(p[2]+1);
//            slice.putPixelValue(p[0], p[1], 1.0);
//        }
//        IJ.saveAs(new ImagePlus("", output), "TIF", "c:/users/barryd/desktop/maxima");
//        System.exit(0);
//    }
}
