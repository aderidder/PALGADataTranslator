/*
 * Copyright 2017 NKI/AvL; VUmc 2018/2019/2020
 *
 * This file is part of PALGA Protocol Data Translator.
 *
 * PALGA Protocol Data Translator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PALGA Protocol Data Translator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PALGA Protocol Data Translator. If not, see <http://www.gnu.org/licenses/>
 */

package palgadatatranslator.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import palgadatatranslator.utils.enumerate.OutputFileType;
import palgadatatranslator.utils.enumerate.OutputFormatType;
import palgadatatranslator.codebook.ProtocolCodebookManager;
import palgadatatranslator.settings.GlobalSettings;
import palgadatatranslator.settings.RunParameters;


import java.io.File;
import java.util.*;


/**
 * class for the GUI GUIWizard
 */
class GUIWizard {
    private static final Logger logger = LogManager.getLogger(GUIWizard.class.getName());
    // create file extension filters
    private static final FileChooser.ExtensionFilter allFilesExtensionFilter = new FileChooser.ExtensionFilter("all files", "*.*");
    private static final FileChooser.ExtensionFilter txtFilesExtensionFilter = new FileChooser.ExtensionFilter("txt files", "*.txt");

    private static final int wizardWidth = 600;
    private static final int wizardHeight = 300;
    private RunParameters runParameters;
    private boolean canRun = false;

    private WizardFlow wizardFlow;

    /**
     * retrieve the value of something from the wizard settings map
     * @param wizardSettings    map with the wizardsettings
     * @param setting           the setting for which we want the value
     * @return the string value of the setting
     */
    private static String getStringSetting(Map<String, Object> wizardSettings, String setting){
        if(wizardSettings.containsKey(setting)){
            return (String) wizardSettings.get((setting));
        }
        return "";
    }

    /**
     * add a tooltip to an item
     * @param control  the item to which to add the tooltip
     * @param helpText the text to show
     */
    private static void addTooltip(Control control, String helpText){
        Tooltip tooltip = new Tooltip(helpText);
        control.setTooltip(tooltip);
    }


    /**
     * Constructor
     */
    GUIWizard(){
    }

    /**
     * create the wizard
     * parameters from the previous run are used to set some values, such as the filenames
     * @param oldParameters    the parameters used in the previous run
     * @return true/false, whether the wizard was successfully completed
     */
    boolean startWizard(RunParameters oldParameters) {
        canRun = false;
        // create the pages
        Wizard wizard = new Wizard();
        wizardFlow = new WizardFlow();
        createPage1(oldParameters);
        createLanguagePage();
        createSummaryPage();
        wizard.setFlow(wizardFlow);

        // show wizard and wait for response
        wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                canRun = true;
            }
            else if(result == ButtonType.CANCEL) {
                logger.log(Level.INFO, "Cancel button pressed...");
            }
        });
        return canRun;
    }

    /**
     * returns the runparameters which were created during the wizard
     * @return the runparameters
     */
    RunParameters getRunParameters(){
        return runParameters;
    }

    /**
     * set the initial browsing directory to the previous directory if possible
     * @param textField    textfield may already contain a directory or file
     * @return the File to which to set the initial directory
     */
    private File getInitialDirectory(TextField textField) {
        String curContent = textField.getText();
        if (!curContent.equalsIgnoreCase("")) {
            File file = new File(curContent);
            if (file.isDirectory()) {
                return file;
            } else {
                return file.getParentFile();
            }
        }
        return null;
    }

    /**
     * create a filechooser and set the textfield to the selected value
     * @param textField          the textfield which may contain a previous value and will contain the selected value
     * @param extensionFilter    filters for file extensions
     */
    private void browseFile(TextField textField, FileChooser.ExtensionFilter ... extensionFilter){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(getInitialDirectory(textField));
            fileChooser.setTitle("Select data file");
            fileChooser.getExtensionFilters().addAll(extensionFilter);
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                textField.setText(selectedFile.getCanonicalPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create a row with a label textfield and file browse button, browsing files
     * @param gridPane            the gridpane to which the items will be added
     * @param id                  base id of the new items
     * @param label               label to be added
     * @param oldVal              old value, which will be the initial value
     * @param row                 row number
     * @param extensionFilters    filename extension filters
     * @return the textfield which was created for the browse row
     */
    private TextField createBrowseFileRow(GridPane gridPane, String id, String label, String oldVal, int row, FileChooser.ExtensionFilter ... extensionFilters){
        // add label and textfield
        gridPane.add(new Label(label), 0, row);
        TextField textField = createTextField(id, oldVal);
        gridPane.add(textField, 1, row);

        // add browse button
        addBrowseButton(gridPane, id, row, event -> browseFile(textField, extensionFilters));
        return textField;
    }

    /**
     * create a directorychooser and set the textfield to the selected value
     * @param textField    the textfield which may contain a previous value and will contain the selected value
     */
    private void browseDir(TextField textField){
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(getInitialDirectory(textField));
            directoryChooser.setTitle("Select directory");
            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                textField.setText(selectedDirectory.getCanonicalPath()+File.separator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create a button with functionality
     * @param gridPane            the gridpane to which the items will be added
     * @param id                  base id of the new items
     * @param row                 row number
     * @param eventHandler        eventhandler which will be called when the button is clicked
     */
    private void addBrowseButton(GridPane gridPane, String id, int row, EventHandler eventHandler){
        Button browseButton = new Button("Browse");
        browseButton.setId(id+"Button");
        browseButton.setOnAction(eventHandler);
        gridPane.add(browseButton, 2, row);
    }

    /**
     * create the first wizard page
     * @param oldParameters previous run parameters
     * @return the wizard page
     */
    private void createPage1(RunParameters oldParameters){
        new WizardPane() {

            private TextField dataFile;
            private ComboBox <String> protocolComboBox;
            private ComboBox <String> outputFileTypesComboBox;
            private ComboBox <String> translateFormatComboBox;
            private ValidationSupport validationSupport = new ValidationSupport();

            {
                wizardFlow.addPage(this);
                this.getStylesheets().clear();
                this.setPrefWidth(wizardWidth);
                this.setPrefHeight(wizardHeight);
                this.setHeaderText("Page 1");
                createContent();
            }

            /**
             * create the content of this page
             */
            private void createContent(){
                // create the gridpane and add the textfields, buttons and labels
                int rowNum = 0;
                GridPane gridPane = createGridPane();
                dataFile  = createBrowseFileRow(gridPane, "protocolFile", "Protocol data file:", oldParameters.getInputFileName(), rowNum, txtFilesExtensionFilter, allFilesExtensionFilter);

                protocolComboBox = createComboBox("protocol", FXCollections.observableArrayList(GlobalSettings.getProtocols()));
                gridPane.add(new Label("Protocol:"),0,++rowNum);
                gridPane.add(protocolComboBox,1,rowNum);
                protocolComboBox.setValue(oldParameters.getProtocolName());

                outputFileTypesComboBox = createComboBox("translateTo", FXCollections.observableArrayList(getOutputFileTypeList()));
                gridPane.add(new Label("Translate to:"),0,++rowNum);
                gridPane.add(outputFileTypesComboBox,1,rowNum);
                outputFileTypesComboBox.setValue(oldParameters.getOutputFileType().getPrettyString());

                translateFormatComboBox = createComboBox("outputFormat", FXCollections.observableArrayList(getOutputFormatTypeList()));
                gridPane.add(new Label("Select desired output format:"),0,++rowNum);
                gridPane.add(translateFormatComboBox,1,rowNum);
                translateFormatComboBox.setValue(oldParameters.getOutputFormatType().getPrettyString());

                // create and add the clearbutton
                gridPane.add(createClearButton(), 1, ++rowNum);

                // set the content
                this.setContent(gridPane);

                addValidation();
            }

            /**
             * add the options to the outputFormatTypes dropdown
             */
            private List<String> getOutputFormatTypeList(){
                List<String> settings = new ArrayList<>();
                // fetch the pretty string for the user interface
                for(OutputFormatType outputFormatType: OutputFormatType.values()){
                    settings.add(outputFormatType.getPrettyString());
                }
                return settings;
            }

            /**
             * add the output file types to the dropdown
             */
            private List<String> getOutputFileTypeList() {
                List<String> settings = new ArrayList<>();
                // fetch the pretty string for the user interface
                for(OutputFileType outputFileType: OutputFileType.values()){
                    settings.add(outputFileType.getPrettyString());
                }

                return settings;
            }

            /**
             * adds validations to fields
             */
            private void addValidation(){
                // add validation
                validationSupport.initInitialDecoration();

                // workaround for bug https://bitbucket.org/controlsfx/controlsfx/issues/539/multiple-dialog-fields-with-validation
                Platform.runLater(() -> {
                    validationSupport.registerValidator(dataFile, Validator.createEmptyValidator("Protocol datafile required"));
                });
            }

            /**
             * create a clear button
             * @return the clear button
             */
            private Button createClearButton(){
                Button clearButton = new Button("Clear all");
                clearButton.setId("clearAll");
                clearButton.setOnAction(event -> {
                    dataFile.clear();

                });
                return clearButton;
            }


            /**
             * things to do when we enter the page
             * @param wizard    the wizard
             */
            @Override
            public void onEnteringPage(Wizard wizard) {
                wizard.invalidProperty().unbind();
                wizard.invalidProperty().bind(validationSupport.invalidProperty());
            }

            /**
             * things to do when we leave the page
             * @param wizard    the wizard
             */
            @Override
            public void onExitingPage(Wizard wizard){
            }
        };
    }

    /**
     * Creates the page in which the user selects the data's language. Only languages that exist in the protocol can be selected
     */
    private void createLanguagePage(){
        new WizardPane(){
            private ComboBox <String> languageComboBox;

            {
                wizardFlow.addPage(this);
                this.getStylesheets().clear();
                this.setPrefWidth(wizardWidth);
                this.setPrefHeight(wizardHeight);
                this.setHeaderText("Languages for Protocol");
                createContent();
            }

            /**
             * create content for the language page
             */
            private void createContent() {
                int row = 0;
                GridPane gridPane = createGridPane();
                languageComboBox = new ComboBox<>();
                languageComboBox.setId("languages");
                gridPane.add(new Label("Source file language:"),0,++row);
                gridPane.add(languageComboBox,1,row);

                this.setContent(gridPane);
            }

            /**
             * page setup when entering the page
             * @param wizard wizard
             */
            private void setupPage(Wizard wizard){
                // create the protocol info for the protocol. This contains which languages are available for a protocol
                ProtocolCodebookManager.createProtocolInfo(GlobalSettings.getProtocolPrefix(getStringSetting(wizard.getSettings(), "protocol")));
                setLanguages(wizard);
            }

            /**
             * loads the available languages
             * @param wizard wizard
             */
            private void setLanguages(Wizard wizard){
                String protocolPrefix = GlobalSettings.getProtocolPrefix(getStringSetting(wizard.getSettings(), "protocol"));
                languageComboBox.setItems(FXCollections.observableArrayList(ProtocolCodebookManager.getProtocolLanguages(protocolPrefix)));
                languageComboBox.getSelectionModel().select(0);
            }

            /**
             * things to do when entering the page
             * @param wizard wizard
             */
            @Override
            public void onEnteringPage(Wizard wizard) {
                wizard.invalidProperty().unbind();
                setupPage(wizard);
            }

        };
    }

    /**
     * create the summary page
     */
    private void  createSummaryPage(){
        new WizardPane(){
            {
                wizardFlow.addPage(this);
                this.getStylesheets().clear();
                this.setPrefWidth(wizardWidth);
                this.setPrefHeight(wizardHeight);
                this.setHeaderText("Summary");
            }

            /**
             * collect the necessary information for the runparameters
             * @param wizard wizard
             */
            private void createRunParameters(Wizard wizard){
                String dataFile = getStringSetting(wizard.getSettings(), "protocolFile");
                String protocol = getStringSetting(wizard.getSettings(), "protocol");
                OutputFileType outputFileType = OutputFileType.getEnum(getStringSetting(wizard.getSettings(), "translateTo"));
                OutputFormatType outputFormat = OutputFormatType.getEnum(getStringSetting(wizard.getSettings(), "outputFormat"));
                String fromLanguage = getStringSetting(wizard.getSettings(), "languages");

                runParameters = new RunParameters(dataFile, protocol, outputFormat, outputFileType, fromLanguage);
            }

            /**
             * things to do when entering the page
             * @param wizard wizard
             */
            @Override
            public void onEnteringPage(Wizard wizard) {
                wizard.invalidProperty().unbind();
                createRunParameters(wizard);
                this.setContentText(runParameters.getSummaryString());
            }
        };
    }

    /**
     * creates grid pane
     * @return grid pane
     */
    private GridPane createGridPane(){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        return gridPane;
    }

    /**
     * creates standard textfield
     * @param id          id for the textfield
     * @param initialText contents for the textfield
     * @return the new textfield
     */
    private TextField createTextField(String id, String initialText) {
        TextField textField = new TextField();
        textField.setId(id);
        textField.setText(initialText);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        return textField;
    }

    /**
     * creates standard combobox
     * @param id    id for the textfield
     * @return the new combobox
     */
    private ComboBox<String> createComboBox(String id, ObservableList <String> itemList) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setId(id);
        comboBox.setItems(itemList);
        GridPane.setHgrow(comboBox, Priority.ALWAYS);
        return comboBox;
    }


    /**
     * create a different type of wizard flow that will allow us to dynamically add pages
     * the idea is that if different/more/fewer languages are selected, the user can add the
     * necessary information on newly generated pages
     */
    static class WizardFlow implements Wizard.Flow {
        private final List<WizardPane> pageList;

        public WizardFlow(Collection<WizardPane> pages) {
            this.pageList = new ArrayList<>(pages);
        }

        public WizardFlow(WizardPane... pages) {
            this((Collection)Arrays.asList(pages));
        }

        void addPage(WizardPane wizardPane) {
            if (!pageList.contains(wizardPane)) {
                pageList.add(wizardPane);
            }
        }

        @Override
        public Optional<WizardPane> advance(WizardPane currentPage) {
            int pageIndex = this.pageList.indexOf(currentPage);
            ++pageIndex;
            return Optional.ofNullable(this.pageList.get(pageIndex));
        }

        @Override
        public boolean canAdvance(WizardPane currentPage) {
            int pageIndex = this.pageList.indexOf(currentPage);
            return this.pageList.size() - 1 > pageIndex;
        }
    }
}


