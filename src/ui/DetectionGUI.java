/* 
 * Copyright (C) 2014 David Barry <david.barry at cancer.org.uk>
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
package ui;

import UIClasses.PropertyExtractor;
import UIClasses.GUIMethods;
import IAClasses.Utils;
import Particle.IsoGaussian;
import ParticleTracking.ParticleTracker;
import Particle.Particle;
import Particle.ParticleArray;
import ParticleTracking.ParticleTrajectory;
import ParticleTracking.UserVariables;
import ParticleWriter.ParticleWriter;
import UIClasses.UIMethods;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.gui.Overlay;
import ij.process.ImageProcessor;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JSlider;

public class DetectionGUI extends javax.swing.JDialog implements GUIMethods {

    protected final ParticleTracker analyser;
    protected final ImagePlus imp;
    protected final String title;
    protected boolean wasOKed = false, monoChrome;
    private final Properties props = new Properties();

    /**
     * Creates new form UserInterface
     */
    public DetectionGUI(java.awt.Frame parent, boolean modal, String title, ParticleTracker analyser, boolean monoChrome) {
        super(parent, modal);
        this.title = title;
        this.analyser = analyser;
        ImageStack[] stacks = analyser.getStacks();
        this.monoChrome = monoChrome;
        if (this.monoChrome) {
            stacks[1] = null;
        }
        this.imp = new ImagePlus("", Utils.updateImage(stacks[0], stacks[1], 1));
        if (monoChrome) {
            UserVariables.setColocal(!monoChrome);
        }
        initComponents();
        UIMethods.centreContainer(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel3 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        canvas1 = new ImageCanvas(imp);
        previewTextField = new javax.swing.JTextField();
        previewSlider = new javax.swing.JSlider(JSlider.HORIZONTAL, 1, analyser.getStacks()[0].size(),1);
        previewToggleButton = new javax.swing.JButton();
        detectionPanel = new ui.DetectionPanel(this, analyser.isGpuEnabled(), monoChrome);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(title);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.GridBagLayout());

        okButton.setText("Run");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridBagLayout());

        canvas1.setMinimumSize(new java.awt.Dimension(analyser.getStacks()[0].getWidth()/4,analyser.getStacks()[0].getHeight()/4));
        canvas1.setPreferredSize(new java.awt.Dimension(analyser.getStacks()[0].getWidth(),analyser.getStacks()[0].getHeight()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        jPanel2.add(canvas1, gridBagConstraints);

        previewTextField.setText(String.valueOf(previewSlider.getValue()));
        previewTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        jPanel2.add(previewTextField, gridBagConstraints);

        previewSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                previewSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(previewSlider, gridBagConstraints);

        previewToggleButton.setText("Preview");
        previewToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 20, 10);
        jPanel2.add(previewToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(detectionPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
        wasOKed = false;
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (!setVariables()) {
            return;
        }
        this.dispose();
        wasOKed = true;
    }//GEN-LAST:event_okButtonActionPerformed

    private void previewSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_previewSliderStateChanged
        previewTextField.setText(String.valueOf(previewSlider.getValue()));
    }//GEN-LAST:event_previewSliderStateChanged

    private void previewToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewToggleButtonActionPerformed
        if (!previewSlider.getValueIsAdjusting() && setVariables()) {
            viewDetections(analyser, monoChrome, detectionPanel.getSpatialRes(), previewSlider.getValue(), canvas1, imp);
        }
    }//GEN-LAST:event_previewToggleButtonActionPerformed

    public boolean setVariables() {
        try {
            UserVariables.setC1ThreshMethod(detectionPanel.getC1ThreshMethod());
            UserVariables.setC2ThreshMethod(detectionPanel.getC2ThreshMethod());
            UserVariables.setSpatialRes(detectionPanel.getSpatialRes());
            UserVariables.setCurveFitTol(detectionPanel.getCurveFitTol());
            UserVariables.setPreProcess(detectionPanel.isPreProcess());
            UserVariables.setGpu(detectionPanel.isGPU());
            UserVariables.setSigEstRed(detectionPanel.getSigmaC1());
//            UserVariables.setSigEstGreen(Double.parseDouble(greenSigmaTextField.getText()));
            UserVariables.setBlobSize(detectionPanel.getBlobSize());
            UserVariables.setFilterRadius(detectionPanel.getFilterRadius());
        } catch (NumberFormatException e) {
            IJ.error("Number formatting error " + e.toString());
            return false;
        }
        setProperties(props, this);
        return true;
    }

    public void setProperties(Properties p, Container container) {
        PropertyExtractor.setProperties(p, container, PropertyExtractor.WRITE);
    }

    public static void viewDetections(ParticleTracker analyser, boolean monoChrome, double spatRes, int psv, Canvas canvas1, ImagePlus imp) {
        analyser.calcParticleRadius(UserVariables.getSpatialRes());
        ImageStack stacks[] = analyser.getStacks();
        if (monoChrome) {
            stacks[1] = null;
        }
        ParticleArray detections;
        if (psv < 1) {
            psv = 1;
        }
//        if (analyser instanceof GPUAnalyse && UserVariables.isGpu()) {
//            detections = ((GPUAnalyse) analyser).cudaFindParticles(false, psv - 1, psv - 1, stacks[1]);
//        } else {
        detections = analyser.findParticles(psv - 1, psv - 1, UserVariables.getCurveFitTol(), stacks[0], stacks[1]);
//        }
        if (detections != null) {
            ImageProcessor output = Utils.updateImage(stacks[0], stacks[1], psv);
            double mag = 1.0 / UIMethods.getMagnification(output, canvas1);
            ArrayList<Particle> particles = detections.getLevel(0);
            outputDetections(particles);
            Color c1Color = !monoChrome ? Color.red : Color.white;
            output.setLineWidth((int) Math.round(1.0 / mag));
            output.setColor(c1Color);
            double radius = analyser.calcParticleRadius(spatRes) * spatRes;
            if (UserVariables.getDetectionMode() != UserVariables.GAUSS) {
                radius = UserVariables.getBlobSize();
            }
            ParticleWriter.drawDetections(particles, output, true, radius, UserVariables.getSpatialRes(), false, null);
            if (!monoChrome) {
                ArrayList<Particle> particles2 = new ArrayList();
                for (Particle p : particles) {
                    Particle p2 = p.getColocalisedParticle();
                    if (p2 != null) {
                        particles2.add(p2);
                    }
                }
                output.setColor(Color.green);
                ParticleWriter.drawDetections(particles2, output, true, radius, UserVariables.getSpatialRes(), false, null);
            }
            imp.setProcessor("", output);
            ((ImageCanvas) canvas1).setMagnification(mag);
            canvas1.repaint();
        }
    }

    static void outputDetections(ArrayList<Particle> particles) {
        for (Particle p : particles) {
            if (p instanceof IsoGaussian) {
                IsoGaussian g = (IsoGaussian) p;
                IJ.log(String.format("x:%f, y:%f, mag:%f, fit:%f", g.getX() / UserVariables.getSpatialRes(), g.getY() / UserVariables.getSpatialRes(), g.getMagnitude(), g.getFit()));
            }
        }
    }

    public static void drawParticle(ImageProcessor image,
            boolean drawOval, Color colour, Particle p) {
        image.setColor(colour);
        int radius = (int) Math.round(UserVariables.getBlobSize() / UserVariables.getSpatialRes());
        int x = (int) Math.round(p.getX() / UserVariables.getSpatialRes());
        int y = (int) Math.round(p.getY() / UserVariables.getSpatialRes());
        switch (UserVariables.getDetectionMode()) {
            case UserVariables.BLOBS:
                image.drawOval((x - radius), (y - radius), 2 * radius, 2 * radius);
                break;
            case UserVariables.GAUSS:
                radius = (int) Math.round(UserVariables.getSigEstRed() / UserVariables.getSpatialRes());
                image.drawOval((x - radius), (y - radius), 2 * radius, 2 * radius);
                break;
            default:
                image.drawLine(x, y - radius, x, y + radius);
                image.drawLine(x + radius, y, x - radius, y);
        }
    }

    public boolean isWasOKed() {
        return wasOKed;
    }

    public Properties getProperties() {
        return props;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private java.awt.Canvas canvas1;
    private ui.DetectionPanel detectionPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton okButton;
    private javax.swing.JSlider previewSlider;
    private javax.swing.JTextField previewTextField;
    private javax.swing.JButton previewToggleButton;
    // End of variables declaration//GEN-END:variables
}
