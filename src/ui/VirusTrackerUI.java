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
import ParticleTracking.UserVariables;
import Particle_Analysis.ParticleTracker;
import Revision.Revision;
import UtilClasses.GenUtils;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import java.awt.Container;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

public class VirusTrackerUI extends javax.swing.JFrame implements GUIMethods {

    private final Properties props;
    private final ParticleTracker analyser;
    private String title = String.format("Virus Tracker v%d.%d", Revision.VERSION, Revision.revisionNumber);
    private boolean wasOKed = false, monoChrome;
    private static final String channel1LabelText = "Channel 1:";
    private static final String channel2LabelText = "Channel 2:";
    private static final String spatResLabelText = "Spatial resolution (" + IJ.micronSymbol + "m/pixel):";
    private static final String fpsLabelText = "Frames per second:";
    private static final String minTrajLengthLabelText = "Minimum trajectory length (s):";
    private static final String minTrajDistLabelText = "Minimum trajectory distance (" + IJ.micronSymbol + "m):";
    private static final String minTrajMSDLabelText = "Minimum trajectory MSD (" + IJ.micronSymbol + "m^2/s):";
    private static final String maxLinkDistLabelText = "Maximum linking distance:";
    private static final String chan1MaxThreshLabelText = "C1 Minimum peak size:";
    private static final String chan2MaxThreshLabelText = "C2 Minimum peak size:";
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
    private static final String maxFrameGapText = "Maximum Gap Size:";
    protected static final DefaultComboBoxModel<String> TRACKING_MODE_OPTIONS = new DefaultComboBoxModel(new String[]{"Random", "Directed"});
    private ImagePlus[] inputs;
    protected final String labels[] = {"Channel 1", "Channel 2"};

    /**
     * Creates new form UserInterface
     */
    public VirusTrackerUI() {
        getImages();
        this.analyser = new ParticleTracker(title, inputs);
        this.props = new Properties();
        if (monoChrome) {
            UserVariables.setColocal(!monoChrome);
        }
        initComponents();
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
        jPanel2 = new javax.swing.JPanel();
        previewToggleButton = new javax.swing.JToggleButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        detectionPanel = new ui.DetectionPanel(this,analyser.isGpuEnabled());
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
        trackingModeComboBox = new javax.swing.JComboBox<>();
        trackingModeLabel = new javax.swing.JLabel();
        maxFrameGapLabel = new javax.swing.JLabel();
        maxFrameGapTextField = new javax.swing.JTextField();

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
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(previewToggleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(timeResLabel, gridBagConstraints);

        timeResTextField.setText(String.valueOf(UserVariables.getTimeRes()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(maxTrajStepLabel, gridBagConstraints);

        minTrajLengthTextField.setText(String.valueOf(UserVariables.getMinTrajLength()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        trackingPanel.add(minTrajLengthTextField, gridBagConstraints);

        maxTrajStepTextField.setText(String.valueOf(UserVariables.getTrajMaxStep()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(minTrajDistLabel, gridBagConstraints);

        minTrajDistTextField.setText(String.valueOf(UserVariables.getMinTrajDist()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(trackLengthLabel, gridBagConstraints);

        trackLengthTextField.setText(String.valueOf(UserVariables.getTrackLength()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        trackingPanel.add(trackLengthTextField, gridBagConstraints);

        extractSigsToggleButton.setText(extractSigsToggleText);
        extractSigsToggleButton.setSelected(UserVariables.isExtractsigs());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(extractSigsToggleButton, gridBagConstraints);

        colocalThreshTextField.setText(String.valueOf(UserVariables.getColocalThresh()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        trackingPanel.add(colocalThreshTextField, gridBagConstraints);

        colocalToggleButton.setText(colocalToggleText);
        colocalToggleButton.setSelected(UserVariables.isColocal());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(colocalToggleButton, gridBagConstraints);

        colocalThreshLabel.setText(colocalThreshText);
        colocalThreshLabel.setLabelFor(colocalThreshTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(colocalThreshLabel, gridBagConstraints);

        useCalsToggleButton.setText(useCalToggleText);
        useCalsToggleButton.setSelected(UserVariables.isUseCals());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        trackingPanel.add(useCalsToggleButton, gridBagConstraints);

        minMSDPointsTextField.setText(String.valueOf(UserVariables.getMinMSDPoints()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        trackingPanel.add(minMSDPointsTextField, gridBagConstraints);

        minMSDPointsLabel.setText(minMSDPointsLabelText);
        minMSDPointsLabel.setLabelFor(minMSDPointsTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(minMSDPointsLabel, gridBagConstraints);

        trackingModeComboBox.setModel(TRACKING_MODE_OPTIONS);
        trackingModeComboBox.setSelectedIndex(UserVariables.getMotionModel() - UserVariables.RANDOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        trackingPanel.add(trackingModeComboBox, gridBagConstraints);

        trackingModeLabel.setText(trackingModeLabelText);
        trackingModeLabel.setLabelFor(trackingModeComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(trackingModeLabel, gridBagConstraints);

        maxFrameGapLabel.setText(maxFrameGapText);
        maxFrameGapLabel.setLabelFor(maxFrameGapTextField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        trackingPanel.add(maxFrameGapLabel, gridBagConstraints);

        maxFrameGapTextField.setText(String.valueOf(UserVariables.getMaxFrameGap()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
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
        wasOKed = false;
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (!setVariables()) {
            return;
        }
        this.dispose();
        analyser.run(null);
    }//GEN-LAST:event_okButtonActionPerformed

    private void previewToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewToggleButtonActionPerformed
        setVariables();
        DetectionGUI.viewDetections(analyser, detectionPanel.getSpatialRes());
    }//GEN-LAST:event_previewToggleButtonActionPerformed

    public boolean setVariables() {
        try {
            UserVariables.setChan1MaxThresh(detectionPanel.getC1MaxThresh());
            UserVariables.setChan2MaxThresh(detectionPanel.getC2MaxThresh());
//            UserVariables.setChan2MaxThresh(Double.parseDouble(chan2MaxThreshTextField.getText()));
            UserVariables.setMinTrajLength(Double.parseDouble(minTrajLengthTextField.getText()));
            UserVariables.setMinTrajDist(Double.parseDouble(minTrajDistTextField.getText()));
//            UserVariables.setMsdThresh(Double.parseDouble(minTrajMSDTextField.getText()));
            UserVariables.setSpatialRes(detectionPanel.getSpatialRes());
            UserVariables.setTimeRes(Double.parseDouble(timeResTextField.getText()));
            UserVariables.setTrajMaxStep(Double.parseDouble(maxTrajStepTextField.getText()));
            UserVariables.setBlobSize(detectionPanel.getBlobSize());
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
            UserVariables.setMotionModel(trackingModeComboBox.getSelectedIndex() + UserVariables.RANDOM);
            UserVariables.setMaxFrameGap(Integer.parseInt(maxFrameGapTextField.getText()));
//            printParams();
        } catch (NumberFormatException e) {
            IJ.error("Number formatting error " + e.toString());
            return false;
        }
        setProperties(props, this);
        return true;
    }

    public boolean isWasOKed() {
        return wasOKed;
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel colocalThreshLabel;
    private javax.swing.JTextField colocalThreshTextField;
    private javax.swing.JToggleButton colocalToggleButton;
    private ui.DetectionPanel detectionPanel;
    private javax.swing.JToggleButton extractSigsToggleButton;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JToggleButton previewToggleButton;
    private javax.swing.JLabel timeResLabel;
    private javax.swing.JTextField timeResTextField;
    private javax.swing.JLabel trackLengthLabel;
    private javax.swing.JTextField trackLengthTextField;
    private javax.swing.JComboBox<String> trackingModeComboBox;
    private javax.swing.JLabel trackingModeLabel;
    private javax.swing.JPanel trackingPanel;
    private javax.swing.JToggleButton useCalsToggleButton;
    // End of variables declaration//GEN-END:variables
}
