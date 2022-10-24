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
package net.calm.virustracker.ui;

import net.calm.iaclasslibrary.MetaData.ParamsReader;
import net.calm.iaclasslibrary.UIClasses.GUIMethods;
import net.calm.iaclasslibrary.UIClasses.PropertyExtractor;
import net.calm.iaclasslibrary.UtilClasses.GenUtils;
import net.calm.trackerlibrary.ParticleTracking.ParticleTrajectory;
import net.calm.trackerlibrary.ParticleTracking.UserVariables;
import net.calm.virustracker.ParticleTracking.ParticleTracker;
import Revision.Revision;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Overlay;
import java.awt.Container;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

public class VirusTrackerUI extends javax.swing.JFrame implements GUIMethods {

    private final Properties props;
    private final ParticleTracker analyser;
    private final String title = String.format("Virus Tracker v%d.%s", Revision.VERSION, new DecimalFormat("000").format(Revision.revisionNumber));
    private final boolean monoChrome;
    private static final String channel1LabelText = "Channel 1:";
    private static final String channel2LabelText = "Channel 2:";
    private static final String spatResLabelText = "Spatial resolution (" + IJ.micronSymbol + "m/pixel):";
    private static final String fpsLabelText = "Frames per second (Hz):";
    private static final String minTrajLengthLabelText = "Minimum trajectory length (s):";
    private static final String minTrajDistLabelText = "Minimum trajectory distance (" + IJ.micronSymbol + "m):";
    private static final String minTrajMSDLabelText = "Minimum trajectory MSD (" + IJ.micronSymbol + "m^2/s):";
    private static final String maxLinkDistLabelText = "Maximum linking distance (" + IJ.micronSymbol + "m):";
    private static final String chan1MaxThreshLabelText = "C1 Thresholding Method:";
    private static final String chan2MaxThreshLabelText = "C2 Thresholding Method:";
//    private static final String chan2MaxThreshLabelText = "Minimum peak size (C2):";
    private static final String c1CurveFitTolLabelText = "Curve fit tolerance:";
//    private static final String c2CurveFitTolLabelText = "Curve fit tolerance (C2):";
    private static final String colocalToggleText = "Co-Localised Only";
    private static final String preprocessToggleText = "Pre-Process Images";
    private static final String gpuToggleText = "Use GPU";
    private static final String useCalToggleText = "Use Spatial Calibration";
    private static final String extractSigsToggleText = "Extract Fluorescence Profiles";
    private static final String trackLengthText = "Track Length (" + IJ.micronSymbol + "m):";
    private static final String prevResToggleText = "Preview Results";
    private static final String colocalThreshText = "Colocalisation Threshold:";
    private static final String redSigEstText = "C1 PSF Width (" + IJ.micronSymbol + "m):";
    private static final String greenSigEstText = "C2 PSF Width (" + IJ.micronSymbol + "m):";
    private static final String minMSDPointsLabelText = "Minimum Points for MSD Calculation:";
    private static final String trackingModeLabelText = "Tracking Model:";
    private static final String maxFrameGapText = "Maximum Gap Size (Frames):";
    protected static final DefaultComboBoxModel<String> TRACKING_MODE_OPTIONS = new DefaultComboBoxModel(new String[]{"Random", "Directed"});
    private ImagePlus[] inputs;
    protected final String labels[] = {"Channel 1", "Channel 2"};

    /**
     * Creates new form UserInterface
     */
    public VirusTrackerUI() {
        getImages();
        this.monoChrome = inputs[1] == null;
        this.props = new Properties();
        this.analyser = new ParticleTracker(title, inputs, props);
        if (inputs[0] != null) {
            readParamsFromImage();
            initComponents();
        }
    }

    private void getImages() {
        this.inputs = new ImagePlus[2];
        if (IJ.getInstance() != null) {
            if (!getActiveImages(true)) {
                return;
            }
        } else {
            inputs[0] = IJ.openImage();
            inputs[1] = IJ.openImage();
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
        previewButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        detectionPanel = new DetectionPanel(this,analyser.isGpuEnabled(),monoChrome);
        trackingPanel = new javax.swing.JPanel();
        timeResLabel = new javax.swing.JLabel();
        timeResTextField = new javax.swing.JTextField();
        minTrajLengthLabel = new javax.swing.JLabel();
        maxTrajStepLabel = new javax.swing.JLabel();
        minTrajLengthTextField = new javax.swing.JTextField();
        maxTrajStepTextField = new javax.swing.JTextField();
        minTrajDistLabel = new javax.swing.JLabel();
        minTrajDistTextField = new javax.swing.JTextField();
        trackLengthLabel = new javax.swing.JLabel();
        trackLengthTextField = new javax.swing.JTextField();
        extractSigsToggleButton = new javax.swing.JToggleButton();
        colocalThreshTextField = new javax.swing.JTextField();
        colocalToggleButton = new javax.swing.JToggleButton();
        colocalThreshLabel = new javax.swing.JLabel();
        useCalsToggleButton = new javax.swing.JToggleButton();
        minMSDPointsTextField = new javax.swing.JTextField();
        minMSDPointsLabel = new javax.swing.JLabel();
        maxFrameGapLabel = new javax.swing.JLabel();
        maxFrameGapTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(title);
        setMinimumSize(new java.awt.Dimension(480, 640));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(cancelButton, gridBagConstraints);

        previewButton.setText("Preview Detections");
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(previewButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        getContentPane().add(jPanel3, gridBagConstraints);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(350, 270));
        jTabbedPane1.addTab("Detection", detectionPanel);

        trackingPanel.setLayout(new java.awt.GridBagLayout());

        timeResLabel.setText(fpsLabelText);
        timeResLabel.setLabelFor(timeResTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(timeResLabel, gridBagConstraints);

        timeResTextField.setText(String.valueOf(UserVariables.getTimeRes()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(timeResTextField, gridBagConstraints);

        minTrajLengthLabel.setText(minTrajLengthLabelText);
        minTrajLengthLabel.setLabelFor(minTrajLengthTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minTrajLengthLabel, gridBagConstraints);

        maxTrajStepLabel.setText(maxLinkDistLabelText);
        maxTrajStepLabel.setLabelFor(maxTrajStepTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(maxTrajStepLabel, gridBagConstraints);

        minTrajLengthTextField.setText(String.valueOf(UserVariables.getMinTrajLength()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minTrajLengthTextField, gridBagConstraints);

        maxTrajStepTextField.setText(String.valueOf(UserVariables.getTrajMaxStep()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(maxTrajStepTextField, gridBagConstraints);

        minTrajDistLabel.setText(minTrajDistLabelText);
        minTrajDistLabel.setLabelFor(minTrajDistTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minTrajDistLabel, gridBagConstraints);

        minTrajDistTextField.setText(String.valueOf(UserVariables.getMinTrajDist()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minTrajDistTextField, gridBagConstraints);

        trackLengthLabel.setText(trackLengthText);
        trackLengthLabel.setLabelFor(trackLengthTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(trackLengthLabel, gridBagConstraints);

        trackLengthTextField.setText(String.valueOf(UserVariables.getTrackLength()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(trackLengthTextField, gridBagConstraints);

        extractSigsToggleButton.setText(extractSigsToggleText);
        extractSigsToggleButton.setSelected(UserVariables.isExtractsigs() && !monoChrome);
        extractSigsToggleButton.setEnabled(!monoChrome);
        extractSigsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractSigsToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(extractSigsToggleButton, gridBagConstraints);

        colocalThreshTextField.setText(String.valueOf(UserVariables.getColocalThresh()));
        colocalThreshTextField.setEnabled(!monoChrome && UserVariables.isColocal());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(colocalThreshTextField, gridBagConstraints);

        colocalToggleButton.setText(colocalToggleText);
        colocalToggleButton.setSelected(UserVariables.isColocal() && !monoChrome);
        colocalToggleButton.setEnabled(!monoChrome);
        colocalToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colocalToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(colocalToggleButton, gridBagConstraints);

        colocalThreshLabel.setText(colocalThreshText);
        colocalThreshLabel.setLabelFor(colocalThreshTextField);
        colocalThreshLabel.setEnabled(!monoChrome && UserVariables.isColocal());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(colocalThreshLabel, gridBagConstraints);

        useCalsToggleButton.setText(useCalToggleText);
        useCalsToggleButton.setSelected(UserVariables.isUseCals() && !monoChrome);
        useCalsToggleButton.setEnabled(!monoChrome && extractSigsToggleButton.isSelected());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(useCalsToggleButton, gridBagConstraints);

        minMSDPointsTextField.setText(String.valueOf(UserVariables.getMinMSDPoints()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minMSDPointsTextField, gridBagConstraints);

        minMSDPointsLabel.setText(minMSDPointsLabelText);
        minMSDPointsLabel.setLabelFor(minMSDPointsTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(minMSDPointsLabel, gridBagConstraints);

        maxFrameGapLabel.setText(maxFrameGapText);
        maxFrameGapLabel.setLabelFor(maxFrameGapTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(maxFrameGapLabel, gridBagConstraints);

        maxFrameGapTextField.setText(String.valueOf(UserVariables.getMaxFrameGap()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        trackingPanel.add(maxFrameGapTextField, gridBagConstraints);

        jTabbedPane1.addTab("Tracking", trackingPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (!setVariables()) {
            return;
        }
        this.dispose();
        inputs[0].setOverlay(null);
        analyser.run(null);
    }//GEN-LAST:event_okButtonActionPerformed

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
        if (!setVariables()) {
            return;
        }
        inputs[0].setOverlay(null);
        viewDetections(analyser, UserVariables.getSpatialRes());
    }//GEN-LAST:event_previewButtonActionPerformed

    private void viewDetections(ParticleTracker analyser, double spatRes) {
        inputs[0].setOverlay(null);
        ImageStack stacks[] = analyser.getAllStacks();
        analyser.updateTrajsForPreview(analyser.findParticles(inputs[0].getCurrentSlice() - 1, inputs[0].getCurrentSlice() - 1, UserVariables.getCurveFitTol(), stacks[0], stacks[1]));
        ArrayList<ParticleTrajectory> trajectories = analyser.getTrajectories();
        float radius;
        if (UserVariables.getDetectionMode() == UserVariables.GAUSS) {
            radius = Math.round(2.0 * UserVariables.getSigEstRed() / spatRes);
        } else {
            radius = Math.round(UserVariables.getBlobSize() / spatRes);
        }
        Overlay overlay = analyser.mapTrajectories(trajectories, spatRes, true, 0, trajectories.size() - 1, 1, radius, stacks[0].getSize());
        inputs[0].setOverlay(overlay);
        inputs[0].show();
        inputs[0].draw();
    }

    private void colocalToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colocalToggleButtonActionPerformed
        colocalThreshLabel.setEnabled(colocalToggleButton.isSelected());
        colocalThreshTextField.setEnabled(colocalToggleButton.isSelected());
    }//GEN-LAST:event_colocalToggleButtonActionPerformed

    private void extractSigsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractSigsToggleButtonActionPerformed
        useCalsToggleButton.setEnabled(extractSigsToggleButton.isSelected());
    }//GEN-LAST:event_extractSigsToggleButtonActionPerformed

    public boolean setVariables() {
        try {
            UserVariables.setC1ThreshMethod(detectionPanel.getC1ThreshMethod());
            UserVariables.setC2ThreshMethod(detectionPanel.getC2ThreshMethod());
//            UserVariables.setChan2MaxThresh(Double.parseDouble(chan2MaxThreshTextField.getText()));
            UserVariables.setMinTrajLength(Double.parseDouble(minTrajLengthTextField.getText()));
            UserVariables.setMinTrajDist(Double.parseDouble(minTrajDistTextField.getText()));
//            UserVariables.setMsdThresh(Double.parseDouble(minTrajMSDTextField.getText()));
            UserVariables.setSpatialRes(detectionPanel.getSpatialRes());
            UserVariables.setTimeRes(Double.parseDouble(timeResTextField.getText()));
            UserVariables.setTrajMaxStep(Double.parseDouble(maxTrajStepTextField.getText()));
            UserVariables.setBlobSize(detectionPanel.getBlobSize());
              UserVariables.setBlobThresh(detectionPanel.getBlobThresh());
            UserVariables.setCurveFitTol(detectionPanel.getCurveFitTol());
//            UserVariables.setC2CurveFitTol(Double.parseDouble(c2CurveFitTolTextField.getText()));
            UserVariables.setColocal(colocalToggleButton.isSelected());
            UserVariables.setPreProcess(detectionPanel.isPreProcess());
//            UserVariables.setC1Index(c1ComboBox.getSelectedIndex());
//            UserVariables.setC2Index(c2ComboBox.getSelectedIndex());
            UserVariables.setGpu(detectionPanel.isGPU());
            UserVariables.setTrackLength(Double.parseDouble(trackLengthTextField.getText()));
//            UserVariables.setPrevRes(previewResultsToggleButton.isSelected());
            UserVariables.setUseCals(useCalsToggleButton.isSelected());
            UserVariables.setExtractsigs(extractSigsToggleButton.isSelected());
            UserVariables.setColocalThresh(Double.parseDouble(colocalThreshTextField.getText()));
            UserVariables.setSigEstRed(detectionPanel.getSigmaC1());
            UserVariables.setSigEstGreen(detectionPanel.getSigmaC1());
            UserVariables.setMinMSDPoints(Integer.parseInt(minMSDPointsTextField.getText()));
//            UserVariables.setMotionModel(trackingModeComboBox.getSelectedIndex() + UserVariables.RANDOM);
            UserVariables.setMaxFrameGap(Integer.parseInt(maxFrameGapTextField.getText()));
//            printParams();
        } catch (NumberFormatException e) {
            GenUtils.logError(e, "Unable to set variables.");
            IJ.error("At least one numeric variable is incorrectly formatted.");
            return false;
        }
        setProperties(props, this);
        return true;
    }

    public static String getChannel1LabelText() {
        return channel1LabelText;
    }

    public static String getChannel2LabelText() {
        return channel2LabelText;
    }

    public static String getSpatResLabelText() {
        return spatResLabelText;
    }

    public static String getFpsLabelText() {
        return fpsLabelText;
    }

    public static String getMinTrajLengthLabelText() {
        return minTrajLengthLabelText;
    }

    public static String getMaxLinkDistLabelText() {
        return maxLinkDistLabelText;
    }

    public static String getChan1MaxThreshLabelText() {
        return chan1MaxThreshLabelText;
    }

//    public static String getChan2MaxThreshLabelText() {
//        return chan2MaxThreshLabelText;
//    }
    public static String getCurveFitTolLabelText() {
        return c1CurveFitTolLabelText;
    }

//    public static String getC2CurveFitTolLabelText() {
//        return c2CurveFitTolLabelText;
//    }
    public static String getColocalToggleText() {
        return colocalToggleText;
    }

    public static String getPreprocessToggleText() {
        return preprocessToggleText;
    }

    public static String getGpuToggleText() {
        return gpuToggleText;
    }

    public static String getTrackLengthText() {
        return trackLengthText;
    }

    public static String getMinTrajDistLabelText() {
        return minTrajDistLabelText;
    }

    public static String getPrevResToggleText() {
        return prevResToggleText;
    }

    public static String getRedSigEstText() {
        return redSigEstText;
    }

    public static String getGreenSigEstText() {
        return greenSigEstText;
    }

    public static String getMinTrajMSDLabelText() {
        return minTrajMSDLabelText;
    }

    public static String getUseCalToggleText() {
        return useCalToggleText;
    }

    public static String getExtractSigsToggleText() {
        return extractSigsToggleText;
    }

    public static String getChan2MaxThreshLabelText() {
        return chan2MaxThreshLabelText;
    }

    public void setProperties(Properties p, Container container) {
        PropertyExtractor.setProperties(p, container, PropertyExtractor.WRITE);
    }

    public Properties getProps() {
        return props;
    }

    private void readParamsFromImage() {
        ParamsReader reader = new ParamsReader(inputs[0]);
        UserVariables.setSpatialRes(reader.getXYSpatialRes());
        UserVariables.setTimeRes(reader.getFrameRate());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel colocalThreshLabel;
    private javax.swing.JTextField colocalThreshTextField;
    private javax.swing.JToggleButton colocalToggleButton;
    private DetectionPanel detectionPanel;
    private javax.swing.JToggleButton extractSigsToggleButton;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel maxFrameGapLabel;
    private javax.swing.JTextField maxFrameGapTextField;
    private javax.swing.JLabel maxTrajStepLabel;
    private javax.swing.JTextField maxTrajStepTextField;
    private javax.swing.JLabel minMSDPointsLabel;
    private javax.swing.JTextField minMSDPointsTextField;
    private javax.swing.JLabel minTrajDistLabel;
    private javax.swing.JTextField minTrajDistTextField;
    private javax.swing.JLabel minTrajLengthLabel;
    private javax.swing.JTextField minTrajLengthTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JButton previewButton;
    private javax.swing.JLabel timeResLabel;
    private javax.swing.JTextField timeResTextField;
    private javax.swing.JLabel trackLengthLabel;
    private javax.swing.JTextField trackLengthTextField;
    private javax.swing.JPanel trackingPanel;
    private javax.swing.JToggleButton useCalsToggleButton;
    // End of variables declaration//GEN-END:variables
}
