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

import IAClasses.IsoGaussian;
import IAClasses.Utils;
import Particle_Analysis.Particle_Tracker;
import ParticleTracking.GPUAnalyse;
import Particle.Particle;
import Particle.ParticleArray;
import ParticleTracking.UserVariables;
import UIClasses.UIMethods;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultBoundedRangeModel;

public class DetectionGUI extends javax.swing.JDialog {

    private final Particle_Tracker analyser;
    private final ImagePlus imp;
    private final String title;
    private boolean wasOKed = false, monoChrome;
    private static final String channel1LabelText = "Channel 1:";
    private static final String spatResLabelText = "Spatial resolution (" + IJ.micronSymbol + "m/pixel):";
    private static final String chan1MaxThreshLabelText = "Minimum peak size:";
    private static final String c1CurveFitTolLabelText = "Curve fit tolerance:";
    private static final String preprocessToggleText = "Pre-Process Images";
    private static final String gpuToggleText = "Use GPU";
    private static final String redSigEstText = "PSF Width (" + IJ.micronSymbol + "m):";

    /**
     * Creates new form UserInterface
     */
    public DetectionGUI(java.awt.Frame parent, boolean modal, String title, Particle_Tracker analyser) {
        super(parent, modal);
        this.title = title;
        this.analyser = analyser;
        ImageStack[] stacks = analyser.getStacks();
        this.monoChrome = (stacks[1] == null);
        this.imp = new ImagePlus("", Utils.updateImage(stacks[0], stacks[1], 1));
        if (monoChrome) {
            UserVariables.setColocal(!monoChrome);
        }
        initComponents();
        UIMethods.centreDialog(this);
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
        jSplitPane1 = new javax.swing.JSplitPane();
        detectionPanel = new javax.swing.JPanel();
        spatResLabel = new javax.swing.JLabel();
        chan1MaxThreshLabel = new javax.swing.JLabel();
        spatResTextField = new javax.swing.JTextField();
        chan1MaxThreshTextField = new javax.swing.JTextField();
        preprocessToggleButton = new javax.swing.JToggleButton();
        c1CurveFitTolLabel = new javax.swing.JLabel();
        c1CurveFitTolTextField = new javax.swing.JTextField();
        gpuToggleButton = new javax.swing.JToggleButton();
        redSigmaLabel = new javax.swing.JLabel();
        redSigmaTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        canvas1 = new ImageCanvas(imp);
        previewTextField = new javax.swing.JTextField();
        previewScrollBar = new java.awt.Scrollbar();
        previewToggleButton = new javax.swing.JButton();

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        getContentPane().add(jPanel3, gridBagConstraints);

        jSplitPane1.setDividerSize(3);
        jSplitPane1.setResizeWeight(0.3);

        detectionPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        detectionPanel.setLayout(new java.awt.GridBagLayout());

        spatResLabel.setText(spatResLabelText);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(spatResLabel, gridBagConstraints);

        chan1MaxThreshLabel.setText(chan1MaxThreshLabelText);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(chan1MaxThreshLabel, gridBagConstraints);

        spatResTextField.setText(String.valueOf(UserVariables.getSpatialRes()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        detectionPanel.add(spatResTextField, gridBagConstraints);

        chan1MaxThreshTextField.setText(String.valueOf(UserVariables.getChan1MaxThresh()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        detectionPanel.add(chan1MaxThreshTextField, gridBagConstraints);

        preprocessToggleButton.setText(preprocessToggleText);
        preprocessToggleButton.setSelected(UserVariables.isPreProcess());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(preprocessToggleButton, gridBagConstraints);

        c1CurveFitTolLabel.setText(c1CurveFitTolLabelText);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(c1CurveFitTolLabel, gridBagConstraints);

        c1CurveFitTolTextField.setText(String.valueOf(UserVariables.getCurveFitTol()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        detectionPanel.add(c1CurveFitTolTextField, gridBagConstraints);

        gpuToggleButton.setText(gpuToggleText);
        gpuToggleButton.setEnabled(analyser.isGpuEnabled());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(gpuToggleButton, gridBagConstraints);

        redSigmaLabel.setText(redSigEstText);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        detectionPanel.add(redSigmaLabel, gridBagConstraints);

        redSigmaTextField.setText(String.valueOf(UserVariables.getSigEstRed()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        detectionPanel.add(redSigmaTextField, gridBagConstraints);

        jSplitPane1.setLeftComponent(detectionPanel);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        jPanel2.add(canvas1, gridBagConstraints);

        previewTextField.setText(String.valueOf(previewScrollBar.getValue()));
        previewTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        jPanel2.add(previewTextField, gridBagConstraints);

        previewScrollBar.setOrientation(java.awt.Scrollbar.HORIZONTAL);
        previewScrollBar.setMinimum(1);
        previewScrollBar.setMaximum(analyser.getStacks()[0].getSize());
        previewScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                previewScrollBarAdjustmentValueChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        jPanel2.add(previewScrollBar, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(previewToggleButton, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        getContentPane().add(jSplitPane1, gridBagConstraints);

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

    private void previewScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_previewScrollBarAdjustmentValueChanged
        previewTextField.setText(String.valueOf(previewScrollBar.getValue()));
    }//GEN-LAST:event_previewScrollBarAdjustmentValueChanged

    private void previewToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewToggleButtonActionPerformed
        if (previewScrollBar.getValueIsAdjusting() || !setVariables()) {
            return;
        }
        viewDetections();
    }//GEN-LAST:event_previewToggleButtonActionPerformed

    boolean setVariables() {
        try {
            UserVariables.setChan1MaxThresh(Double.parseDouble(chan1MaxThreshTextField.getText()));
            UserVariables.setSpatialRes(Double.parseDouble(spatResTextField.getText()));
            UserVariables.setCurveFitTol(Double.parseDouble(c1CurveFitTolTextField.getText()));
            UserVariables.setPreProcess(preprocessToggleButton.isSelected());
            UserVariables.setGpu(gpuToggleButton.isSelected());
            UserVariables.setSigEstRed(Double.parseDouble(redSigmaTextField.getText()));
        } catch (NumberFormatException e) {
            IJ.error("Number formatting error " + e.toString());
            return false;
        }
        return true;
    }

    public void viewDetections() {
        analyser.calcParticleRadius(UserVariables.getSpatialRes());
        ImageStack stacks[] = analyser.getStacks();
        ParticleArray detections;
        int psv = previewScrollBar.getValue();
        if (psv < 1) {
            psv = 1;
        }
        if (analyser instanceof GPUAnalyse && UserVariables.isGpu()) {
            detections = ((GPUAnalyse) analyser).cudaFindParticles(true, psv - 1, psv - 1, stacks[1]);
        } else {
            detections = analyser.findParticles(true, psv - 1, psv - 1, UserVariables.getCurveFitTol(), stacks[0], stacks[1]);
        }
        if (detections != null) {
            ImageProcessor output = Utils.updateImage(stacks[0], stacks[1], psv);
            double mag = 1.0 / UIMethods.getMagnification(output, canvas1);
            double sr = 1.0 / Double.parseDouble(spatResTextField.getText());
//        int radius = (int)Math.round(sr);
            int radius = analyser.calcParticleRadius(UserVariables.getSpatialRes());
            IsoGaussian c1, c2;
            ArrayList<Particle> particles = detections.getLevel(0);
            Color c1Color = !monoChrome ? Color.red : Color.white;
            Color c2Color = !monoChrome ? Color.green : Color.white;
            output.setLineWidth(1);
            for (Particle particle : particles) {
                c1 = particle.getC1Gaussian();
                c2 = particle.getC2Gaussian();
                drawDetections(output, (int) (Math.round(sr * c1.getX())), (int) (Math.round(sr * c1.getY())),
                        radius, true, c1Color);
                if (c2 != null) {
                    drawDetections(output, (int) (Math.round(sr * c2.getX())),
                            (int) (Math.round(sr * c2.getY())), radius,
                            false, c2Color);
                }
            }
            imp.setProcessor("", output);
            ((ImageCanvas) canvas1).setMagnification(mag);

            canvas1.repaint();
        }
    }

    public void drawDetections(ImageProcessor image, int x, int y, int radius,
            boolean drawOval, Color colour) {
        image.setColor(colour);
        if (drawOval) {
            image.drawOval((x - radius), (y - radius), 2 * radius, 2 * radius);
        } else {
            image.drawRect((x - radius), (y - radius), 2 * radius, 2 * radius);
        }
    }

    public boolean isWasOKed() {
        return wasOKed;
    }

    public static String getChannel1LabelText() {
        return channel1LabelText;
    }

    public static String getSpatResLabelText() {
        return spatResLabelText;
    }

    public static String getChan1MaxThreshLabelText() {
        return chan1MaxThreshLabelText;
    }

    public static String getCurveFitTolLabelText() {
        return c1CurveFitTolLabelText;
    }

    public static String getPreprocessToggleText() {
        return preprocessToggleText;
    }

    public static String getGpuToggleText() {
        return gpuToggleText;
    }

    public static String getRedSigEstText() {
        return redSigEstText;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel c1CurveFitTolLabel;
    private javax.swing.JTextField c1CurveFitTolTextField;
    private javax.swing.JButton cancelButton;
    private java.awt.Canvas canvas1;
    private javax.swing.JLabel chan1MaxThreshLabel;
    private javax.swing.JTextField chan1MaxThreshTextField;
    private javax.swing.JPanel detectionPanel;
    private javax.swing.JToggleButton gpuToggleButton;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JToggleButton preprocessToggleButton;
    private java.awt.Scrollbar previewScrollBar;
    private javax.swing.JTextField previewTextField;
    private javax.swing.JButton previewToggleButton;
    private javax.swing.JLabel redSigmaLabel;
    private javax.swing.JTextField redSigmaTextField;
    private javax.swing.JLabel spatResLabel;
    private javax.swing.JTextField spatResTextField;
    // End of variables declaration//GEN-END:variables
}