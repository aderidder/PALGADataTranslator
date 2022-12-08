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

package palgadatatranslator.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import palgadatatranslator.utils.enumerate.OutputFileType;
import palgadatatranslator.utils.enumerate.OutputFormatType;

import java.io.File;

/**
 * contains all the user parameters the ui generates
 * maybe change this at some point to be layered if we want to support additional output types
 */
public class RunParameters {
    private static final Logger logger = LogManager.getLogger(RunParameters.class.getName());
    private final String fromLanguage;

    private final OutputFormatType outputFormatType;
    private final OutputFileType outputFileType;
    private final String protocolPrefix;
    private final String inputFileName;
    private final String protocolName;

    /**
     * constructor for default run parameters
     */
    public RunParameters(){
        this("", GlobalSettings.getDefaultProtocolName(), OutputFormatType.DESCRIPTIONS, OutputFileType.TEXT, "");
    }

    /**
     * constructor for run parameters
     * @param inputFileName    name of the input file
     * @param protocolName     name of the protocol
     * @param outputFormatType type of output (e.g. CODES)
     * @param outputFileType   type of output file (currently always text)
     * @param fromLanguage     language of the source file
     */
    public RunParameters(String inputFileName, String protocolName, OutputFormatType outputFormatType, OutputFileType outputFileType, String fromLanguage){
        this.inputFileName =  inputFileName;
        this.protocolName = protocolName;
        this.protocolPrefix = GlobalSettings.getProtocolPrefix(protocolName);
        this.outputFormatType = outputFormatType;
        this.outputFileType = outputFileType;
        this.fromLanguage = fromLanguage;
    }

    /**
     * returns a string of the parameters chosen by the user
     * @return a string of the parameters chosen by the user
     */
    public String getSummaryString(){
        String summaryText=
                "data file: "+inputFileName+"\n" +
                "protocol: "+protocolName+"\n" +
                "filetype: "+outputFileType.getPrettyString()+"\n" +
                "containing: "+outputFormatType.getPrettyString()+"\n" +
                "source language: "+fromLanguage;
        return summaryText;
    }

    /**
     * returns the output format type
     * @return the output format type
     */
    public OutputFormatType getOutputFormatType() {
        return outputFormatType;
    }

    /**
     * returns the output file type
     * @return the output file type
     */
    public OutputFileType getOutputFileType() {
        return outputFileType;
    }

    /**
     * returns the protocol prefix
     * @return the protocol prefix
     */
    public String getProtocolPrefix() {
        return protocolPrefix;
    }

    /**
     * returns the from language
     * @return the from language
     */
    public String getFromLanguage() {
        return fromLanguage;
    }

    /**
     * returns the name of the input file
     * @return the name of the input file
     */
    public String getInputFileName(){
        return inputFileName;
    }

    /**
     * returns the protocol name
     * @return the protocol name
     */
    public String getProtocolName() {
        return protocolName;
    }

    /**
     * returns the filename without the directory
     * @return the filename without the directory
     */
    public String getShortDataOutFileName(){
        String outFileName = getDataOutFileName();
        return outFileName.substring(outFileName.lastIndexOf(File.separator)+1);
    }

    /**
     * returns the name of the data output file
     * @return the name of the data output file
     */
    public String getDataOutFileName(){
        String outFileName = inputFileName.substring(0, inputFileName.lastIndexOf("."));
        outFileName += "_out.txt";
        return outFileName;
    }

    /**
     * returns whether the settings are valid
     * @return true/false
     */
    public boolean validSettings(){
        boolean valid = true;
        if (!isValidInputFile(inputFileName)) {
            logger.error("Please select a valid datafile before running");
            valid = false;
        }
        return valid;
    }

    /**
     * returns whether a file is a valid file
     * @param fileName file to validate
     * @return true/false
     */
    private boolean isValidInputFile(String fileName){
        File file = new File(fileName);
        return !fileName.equalsIgnoreCase("") && file.exists() && !file.isDirectory();
    }
}
