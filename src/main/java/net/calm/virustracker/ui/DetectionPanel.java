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
package net.calm.virustracker.ui;

import ij.IJ;
import ij.process.AutoThresholder;
import net.calm.iaclasslibrary.UIClasses.GUIMethods;
import net.calm.trackerlibrary.ParticleTracking.UserVariables;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Dave Barry <david.barry at crick.ac.uk>
 */
public class DetectionPanel extends javax.swing.JPanel {

    protected boolean wasOKed = false;
    protected static final String spatResLabelText = "Spatial resolution (" + IJ.micronSymbol + "m/pixel):";
    protected static final String chan1MaxThreshLabelText = "C1 minimum peak size:";
    protected static final String chan2MaxThreshLabelText = "C2 minimum peak size:";
    protected static final String c1CurveFitTolLabelText = "Curve fit tolerance:";
    protected static final String preprocessToggleText = "Pre-Process Images";
    protected static final String gpuToggleText = "Use GPU";
    protected static final String redSigEstText = "PSF radius (" + IJ.micronSymbol + "m):";
//    protected static final String greenSigEstText = "C2 PSF Width (" + IJ.micronSymbol + "m):";
    protected static final String DETECT_MODE = "Detection Mode:";
    protected static final String blobSizeText = "Blob size (" + IJ.micronSymbol + "m):";
    protected static final String blobThreshText = "Blob Detection Threshold";
    protected static final String filterRadiusText = "Gaussian Filter Radius (" + IJ.micronSymbol + "m):";
    protected static final DefaultComboBoxModel<String> DETECT_MODE_OPTIONS = new DefaultComboBoxModel(new String[]{"Points", "Blobs", "PSFs"});
    private final boolean gpuEnabled, monoChrome;
    private final GUIMethods parent;

    /**
     * Creates new form DetectionPanel
     */
    public DetectionPanel() {
        this(null, false, false);
    }

    public DetectionPanel(GUIMethods parent, boolean gpuEnabled, boolean monochrome) {
        this.parent = parent;
        this.gpuEnabled = gpuEnabled;
        this.monoChrome = monochrome;
        initComponents();
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

        spatResLabel = new javax.swing.JLabel();
        chan1MaxThreshLabel = new javax.swing.JLabel();
        spatResTextField = new javax.swing.JTextField();
        preProcessToggleButton = new javax.swing.JToggleButton();
        curveFitTolLabel = new javax.swing.JLabel();
        curveFitTolTextField = new javax.swing.JTextField();
        gpuToggleButton = new javax.swing.JToggleButton();
        sigmaLabel = new javax.swing.JLabel();
        sigmaTextField = new javax.swing.JTextField();
        chan2MaxThreshLabel = new javax.swing.JLabel();
        detectionModeComboBox = new javax.swing.JComboBox<>();
        detectionModeLabel = new javax.swing.JLabel();
        blobSizeLabel = new javax.swing.JLabel();
        blobSizeTextField = new javax.swing.JTextField();
        filterRadiusLabel = new javax.swing.JLabel();
        filterRadiusTextField = new javax.swing.JTextField();
        c1ThreshComboBox = new javax.swing.JComboBox<>();
        c2ThreshComboBox = new javax.swing.JComboBox<>();
        blobThreshLabel = new javax.swing.JLabel();
        blobThreshTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        spatResLabel.setText(spatResLabelText);
        spatResLabel.setLabelFor(spatResTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(spatResLabel, gridBagConstraints);

        chan1MaxThreshLabel.setText(chan1MaxThreshLabelText);
        chan1MaxThreshLabel.setLabelFor(c1ThreshComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(chan1MaxThreshLabel, gridBagConstraints);

        spatResTextField.setText(String.valueOf(UserVariables.getSpatialRes()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(spatResTextField, gridBagConstraints);

        preProcessToggleButton.setText(preprocessToggleText);
        preProcessToggleButton.setSelected(UserVariables.isPreProcess());
        preProcessToggleButton.setEnabled(!(UserVariables.getDetectionMode()==UserVariables.BLOBS));
        preProcessToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preProcessToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(preProcessToggleButton, gridBagConstraints);

        curveFitTolLabel.setText(c1CurveFitTolLabelText);
        curveFitTolLabel.setLabelFor(curveFitTolTextField);
        curveFitTolLabel.setEnabled(UserVariables.getDetectionMode()==UserVariables.GAUSS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(curveFitTolLabel, gridBagConstraints);

        curveFitTolTextField.setText(String.valueOf(UserVariables.getCurveFitTol()));
        curveFitTolTextField.setEnabled(UserVariables.getDetectionMode()==UserVariables.GAUSS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(curveFitTolTextField, gridBagConstraints);

        gpuToggleButton.setText(gpuToggleText);
        gpuToggleButton.setEnabled(gpuEnabled);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(gpuToggleButton, gridBagConstraints);

        sigmaLabel.setText(redSigEstText);
        sigmaLabel.setLabelFor(sigmaTextField);
        sigmaLabel.setEnabled(UserVariables.getDetectionMode()==UserVariables.GAUSS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(sigmaLabel, gridBagConstraints);

        sigmaTextField.setText(String.valueOf(UserVariables.getSigEstRed()));
        sigmaTextField.setEnabled(UserVariables.getDetectionMode()==UserVariables.GAUSS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(sigmaTextField, gridBagConstraints);

        chan2MaxThreshLabel.setText(chan2MaxThreshLabelText);
        chan2MaxThreshLabel.setLabelFor(c2ThreshComboBox);
        chan2MaxThreshLabel.setEnabled(!monoChrome);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(chan2MaxThreshLabel, gridBagConstraints);

        detectionModeComboBox.setModel(DETECT_MODE_OPTIONS);
        detectionModeComboBox.setSelectedIndex(UserVariables.getDetectionMode()-UserVariables.MAXIMA);
        detectionModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detectionModeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(detectionModeComboBox, gridBagConstraints);

        detectionModeLabel.setText(DETECT_MODE);
        detectionModeLabel.setLabelFor(detectionModeComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(detectionModeLabel, gridBagConstraints);

        blobSizeLabel.setText(blobSizeText);
        blobSizeLabel.setLabelFor(blobSizeTextField);
        blobSizeLabel.setEnabled(!(UserVariables.getDetectionMode()==UserVariables.GAUSS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(blobSizeLabel, gridBagConstraints);

        blobSizeTextField.setText(String.format("%1.3f", UserVariables.getBlobSize()));
        blobSizeTextField.setEnabled(!(UserVariables.getDetectionMode()==UserVariables.GAUSS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(blobSizeTextField, gridBagConstraints);

        filterRadiusLabel.setText(filterRadiusText);
        filterRadiusLabel.setLabelFor(filterRadiusTextField);
        filterRadiusLabel.setEnabled(UserVariables.isPreProcess() && !(UserVariables.getDetectionMode()==UserVariables.BLOBS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(filterRadiusLabel, gridBagConstraints);

        filterRadiusTextField.setText(String.format("%1.3f", UserVariables.getFilterRadius()));
        filterRadiusTextField.setEnabled(UserVariables.isPreProcess()&&!(UserVariables.getDetectionMode()==UserVariables.BLOBS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(filterRadiusTextField, gridBagConstraints);

        c1ThreshComboBox.setModel(new DefaultComboBoxModel(AutoThresholder.Method.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(c1ThreshComboBox, gridBagConstraints);

        c2ThreshComboBox.setModel(new DefaultComboBoxModel(AutoThresholder.Method.values()));
        c2ThreshComboBox.setEnabled(!monoChrome);
        c2ThreshComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c2ThreshComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(c2ThreshComboBox, gridBagConstraints);

        blobThreshLabel.setText(blobThreshText);
        blobThreshLabel.setLabelFor(blobThreshTextField);
        blobThreshLabel.setEnabled(UserVariables.getDetectionMode()==UserVariables.BLOBS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(blobThreshLabel, gridBagConstraints);

        blobThreshTextField.setText(String.format("%1.3f", UserVariables.getBlobThresh()));
        blobThreshTextField.setEnabled(UserVariables.getDetectionMode()==UserVariables.BLOBS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(blobThreshTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void preProcessToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preProcessToggleButtonActionPerformed
        preProcessToggleButton.setEnabled(!(UserVariables.getDetectionMode() == UserVariables.BLOBS));
        filterRadiusLabel.setEnabled(preProcessToggleButton.isSelected() && !(UserVariables.getDetectionMode() == UserVariables.BLOBS));
        filterRadiusTextField.setEnabled(preProcessToggleButton.isSelected() && !(UserVariables.getDetectionMode() == UserVariables.BLOBS));
    }//GEN-LAST:event_preProcessToggleButtonActionPerformed

    private void detectionModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detectionModeComboBoxActionPerformed
        UserVariables.setDetectionMode(getDetectionMode());
        parent.setVariables();
        boolean psfs = UserVariables.getDetectionMode() == UserVariables.GAUSS;
        curveFitTolTextField.setEnabled(psfs);
        curveFitTolLabel.setEnabled(psfs);
        sigmaTextField.setEnabled(psfs);
        sigmaLabel.setEnabled(psfs);
        blobSizeLabel.setEnabled(!psfs);
        blobSizeTextField.setEnabled(!psfs);
        boolean blobs = UserVariables.getDetectionMode() == UserVariables.BLOBS;
        blobThreshLabel.setEnabled(blobs);
        blobThreshTextField.setEnabled(blobs);
        c1ThreshComboBox.setEnabled(!blobs);
        c2ThreshComboBox.setEnabled(!blobs);
        chan1MaxThreshLabel.setEnabled(!blobs);
        chan2MaxThreshLabel.setEnabled(!blobs);
        preProcessToggleButtonActionPerformed(evt);
    }//GEN-LAST:event_detectionModeComboBoxActionPerformed

    private void c2ThreshComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c2ThreshComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c2ThreshComboBoxActionPerformed

    public int getDetectionMode() {
        return detectionModeComboBox.getSelectedIndex() + UserVariables.MAXIMA;
    }

    public String getC1ThreshMethod() {
        return String.valueOf(c1ThreshComboBox.getSelectedItem());
    }

    public String getC2ThreshMethod() {
        return String.valueOf(c2ThreshComboBox.getSelectedItem());
    }

    public double getSpatialRes() {
        return Double.parseDouble(spatResTextField.getText());
    }

    public double getCurveFitTol() {
        return Double.parseDouble(curveFitTolTextField.getText());
    }

    public boolean isPreProcess() {
        return preProcessToggleButton.isSelected();
    }

    public boolean isGPU() {
        return gpuToggleButton.isSelected();
    }

    public double getSigmaC1() {
        return Double.parseDouble(sigmaTextField.getText());
    }

    public double getBlobSize() {
        return Double.parseDouble(blobSizeTextField.getText());
    }
    
    public double getBlobThresh() {
        return Double.parseDouble(blobThreshTextField.getText());
    }

    public double getFilterRadius() {
        return Double.parseDouble(filterRadiusTextField.getText());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blobSizeLabel;
    private javax.swing.JTextField blobSizeTextField;
    private javax.swing.JLabel blobThreshLabel;
    private javax.swing.JTextField blobThreshTextField;
    private javax.swing.JComboBox<String> c1ThreshComboBox;
    private javax.swing.JComboBox<String> c2ThreshComboBox;
    private javax.swing.JLabel chan1MaxThreshLabel;
    private javax.swing.JLabel chan2MaxThreshLabel;
    private javax.swing.JLabel curveFitTolLabel;
    private javax.swing.JTextField curveFitTolTextField;
    private javax.swing.JComboBox<String> detectionModeComboBox;
    private javax.swing.JLabel detectionModeLabel;
    private javax.swing.JLabel filterRadiusLabel;
    private javax.swing.JTextField filterRadiusTextField;
    private javax.swing.JToggleButton gpuToggleButton;
    private javax.swing.JToggleButton preProcessToggleButton;
    private javax.swing.JLabel sigmaLabel;
    private javax.swing.JTextField sigmaTextField;
    private javax.swing.JLabel spatResLabel;
    private javax.swing.JTextField spatResTextField;
    // End of variables declaration//GEN-END:variables
}