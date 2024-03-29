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

package palgadatatranslator.data.in;

import palgadatatranslator.data.out.OutputData;
import palgadatatranslator.data.out.OutputDataLongFormat;
import palgadatatranslator.utils.enumerate.OutputFormatType;
import palgadatatranslator.codebook.HousekeepingCodebookManager;
import palgadatatranslator.codebook.ProtocolCodebookManager;
import palgadatatranslator.settings.RunParameters;
import palgadatatranslator.utils.Romans;

import java.util.*;
import java.util.stream.Collectors;

/**
 * shared class for InputData types
 * some things to keep in mind here:
 *  - PALGA data can contain Roman number, which is something like colonbiopti, colonbioptii etc.
 *  - The codebooks are not aware of this, as the definition is in both cases colonbiopt
 *
 * Hence, we keep lists with the original header names, the header names without the roman numbers and a list with just the roman numbers
 */
abstract class InputDataDefault implements InputData {
    private static final String protocolVersionColName = "depvenr";

    private int protocolVersionIndex;

    // the data lines
    List<List<String>> lines = new ArrayList<>();
    // the original header
    List<String> origHeaderList = new ArrayList<>();
    // the header, without roman numbers
    List<String> noRomanHeaderList = new ArrayList<>();
    // the roman numbers
    List<String> romansInHeader = new ArrayList<>();

    // keep track of the maximum version used for each concept
    // this is necessary as the data could contain a column which no longer exists in the newest version
    // of the protocol used in the data file. In that case translating it using the newest version present
    // would result in an error
    String [] maxVersionForConcept;

    OutputData outputData;
    OutputFormatType outputFormatType;

    RunParameters runParameters;

    /**
     * constructor
     */
    InputDataDefault(RunParameters runParameters){
        this.runParameters = runParameters;
        this.outputFormatType = runParameters.getOutputFormatType();
    }

    /**
     * handle the header of the data file
     * @param line the header line, tab separated
     */
    void addHeader(String line){
        origHeaderList = Arrays.asList(line.split("\t"));
        protocolVersionIndex = origHeaderList.indexOf(protocolVersionColName);
        maxVersionForConcept = new String[origHeaderList.size()];
        Arrays.fill(maxVersionForConcept, "-1");
    }

    /**
     * add a line which contains data
     * @param line the line with data, tab separated
     */
    void addData(String line){
        List<String> newLine = prepareLine(line);
        checkMaxVersionConcept(newLine);
        lines.add(newLine);
    }

    /**
     * prepare the line, changing it to a list and cleaning it
     * @param line the line to prepare
     * @return list representation of the line
     */
    private List<String> prepareLine(String line){
        String [] splitLine = line.split("\t", -1);
        return Arrays.stream(splitLine).map(this::cleanValue).collect(Collectors.toList());
    }

    /**
     * trim the value and remove quotes that excel sometimes adds
     * @param value value to clean
     * @return cleaned value
     */
    private String cleanValue(String value){
        value = value.trim();
        if(value.startsWith("\"") && value.endsWith("\"")){
            value = value.substring(1, value.length()-1);
        }
        return value;
    }

    /**
     * translate data; creates outputdata and translates the header and values
     */
    @Override
    public final void translate(){
        outputData = createOutputData();
        translateHeader();
        translateValues();
    }

    /**
     * write the output to file
     */
    @Override
    public final void writeOutput() {
        outputData.writeData();
    }

    /**
     * return the protocol version number of a data line
     * @param line data line
     * @return version number
     */
    String getProtocolVersionForLine(List<String> line){
        return line.get(protocolVersionIndex);
    }

    /**
     * identify roman numbers in the header
     */
    void checkRomans(){
        ProtocolCodebookManager protocolCodebookManager = ProtocolCodebookManager.getProtocolManager(runParameters);
        HousekeepingCodebookManager housekeepingCodebookManager = HousekeepingCodebookManager.getProtocolManager(runParameters);
        origHeaderList.forEach(t->setRomans(housekeepingCodebookManager, protocolCodebookManager, t));
    }

    /**
     * identify roman numbers in a headername
     * @param protocolCodebookManager protocol codebook manager
     * @param origName                original name of header
     */
    private void setRomans(HousekeepingCodebookManager housekeepingCodebookManager, ProtocolCodebookManager protocolCodebookManager, String origName) {
        String noRomanName;
        String maxProtocolVersionForConcept = maxVersionForConcept[origHeaderList.indexOf(origName)];

        // check whether the column actually has data in it. If not, there's no need to search
        // for romans, as it won't be written anyway (and it would also be problematic as we wouldn't know
        // which version of the codebook to use for the translation of the header)
        if(addDataToOutput(origName) && !housekeepingCodebookManager.containsHeaderName(origName)){
            // retrieve a list of the romans which were possibly used in this headername
            List<String> romanList = Romans.romanNumberMatch(origName);

            if (romanList.size() > 0) {
                // loop over the romanList
                for (String aRomanNumber : romanList) {
                    // remove the roman number from the name
                    noRomanName = origName.substring(0, origName.length() - aRomanNumber.length());
                    // check whether this noRomanName exists in the codebook, as in that case we have a valid name
                    if (protocolCodebookManager.containsHeaderName(noRomanName, maxProtocolVersionForConcept)) {
                        // if it does, add both the noRomanName and the romannumber to our list
                        noRomanHeaderList.add(noRomanName.toLowerCase());
                        romansInHeader.add(aRomanNumber);
                        return;
                    }
                }
            }
        }

        // if the column could not be identified as a roman column, the noRoman is the original and the roman is blank
        noRomanHeaderList.add(origName.toLowerCase());
        romansInHeader.add("");
    }

    /**
     * for each concept in the line that has a value, checks whether the max stored version is smaller than the
     * current version and if so, stores this line's version as the max for the concept
     * @param line list representation of the line
     */
    private void checkMaxVersionConcept(List<String> line){
        String version = line.get(protocolVersionIndex);
        for(int i=0; i<line.size(); i++){
            if(!line.get(i).equalsIgnoreCase("")){
                if(Integer.parseInt(maxVersionForConcept[i])<Integer.parseInt(version)){
                    maxVersionForConcept[i] = version;
                }
            }
        }
    }

    /**
     * returns whether the concept is eligible for output. If the concept has a version number of -1, it is not.
     * @param headerName the concept to check
     * @return true/false
     */
    public boolean addDataToOutput(String headerName){
        return !maxVersionForConcept[origHeaderList.indexOf(headerName)].equalsIgnoreCase("-1");
    }

    /**
     * returns the header names without roman numbers
     * @return the header names without roman numbers
     */
    public List<String> getNoRomanHeaderList(){
        return noRomanHeaderList;
    }

    /**
     * returns the original header names, which may include roman numbers
     * @return the original header names, which may include roman numbers
     */
    public List<String> getOrigHeaderList(){
        return origHeaderList;
    }

    /**
     * returns a list containing only the roman numbers
     * @return a list containing only the roman numbers
     */
    public List<String> getRomanList(){
        return romansInHeader;
    }

    /**
     * create new output data
     * @return new OutputData object
     */
    private OutputData createOutputData(){
        // currently we only support Long format
        return new OutputDataLongFormat(runParameters);
    }

    abstract void translateHeader();
    abstract void translateValues();

}
