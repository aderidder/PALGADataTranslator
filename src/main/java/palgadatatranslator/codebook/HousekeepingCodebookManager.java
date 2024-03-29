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

package palgadatatranslator.codebook;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import palgadatatranslator.utils.enumerate.OutputFormatType;
import palgadatatranslator.settings.RunParameters;
import palgadatatranslator.utils.ArtDecorCalls;
import palgadatatranslator.utils.LogTracker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * manager for the housekeeping protocol
 * the housekeeping protocol is a way to handle all the items that are not part of any protocol but which PALGA can
 * provide, such as the t-number, exceptid etc.
 *
 * we're storing one housekeeping codebook manager in a language to codebookmanager map
 * the codebookmanager contains a single codebook, as I'm not sure whether we'll get multiple versions here
 * even if we do get multiple versions, we'll probably want to use the newest anyway
 */
public class HousekeepingCodebookManager {
    private static final Logger logger = LogManager.getLogger(HousekeepingCodebookManager.class.getName());
    // language to HousekeepingCodebook map
    private static Map<String, HousekeepingCodebookManager> protocolCodebookManagerMap = new HashMap<>();
    private static final String protocolPrefix = "housekeeping";

    // I'm assuming the housekeeping stuff will always return a description. We could also change this to follow the way the protocol manager handles this
    private static final OutputFormatType outputType = OutputFormatType.DESCRIPTIONS;

//    private Map<String, DecorCodebook> codebookMap = new HashMap<>();
    private DecorCodebook codebook;
    private String datasetId;
    private final String fromLanguage; //e.g. nl-NL

    /**
     * return the housekeeping codebook manager for the language which is available in the runsettings
     * @param runParameters settings for the run
     * @return the housekeeping codebook manager
     */
    public static HousekeepingCodebookManager getProtocolManager(RunParameters runParameters){
        // currently I'm expecting there will only be nl-NL, but we're storing it a language to codebook map anyway
        // perhaps useful in the future
        String fromLanguage = runParameters.getFromLanguage();
        String key = protocolPrefix+fromLanguage;
        if(!protocolCodebookManagerMap.containsKey(key)){
            protocolCodebookManagerMap.put(key, new HousekeepingCodebookManager(fromLanguage));
        }
        return protocolCodebookManagerMap.get(key);
    }

    /**
     * create a new codebook manager
     * @param fromLanguage the source language
     */
    private HousekeepingCodebookManager(String fromLanguage){
        this.fromLanguage = fromLanguage;
        setProtocolVersionToIdMap();
        addCodebook();
    }

    /**
     * checks whether the codebook contains the headername
     * @param headerName the headername to check
     * @return true/false
     */
    public boolean containsHeaderName(String headerName){
        if(codebook==null){
            return false;
        }
        return codebook.containsHeaderName(headerName);
    }

    /**
     * translate a value
     * @param headerName the headername for the value
     * @param value      the value to translate
     * @return the translated value or the original value if we don't have a codebook
     */
    public String translateValue(String headerName, String value){
        if(codebook==null){
            return value;
        }
        return  codebook.translateConceptValue(outputType, value, headerName);
    }

    /**
     * translate a concept
     * @param headerName the headername to translate
     * @return the translated headername or the original value if we don't have a codebook
     */
    public String translateConcept(String headerName){
        if(codebook==null){
            return headerName;
        }
        return codebook.translateConcept(outputType, headerName);
    }

    /**
     * creates a codebook
     */
    private void addCodebook(){
        if(datasetId!=null) {
            codebook = new DecorCodebook(fromLanguage, datasetId, "1");
        }
    }

    /**
     * attempts to retrieve version and identifier information from a uri for the housekeeping protocol
     */
    private void setProtocolVersionToIdMap(){
        logger.log(Level.INFO, "Trying to retrieve available versions of the housekeeping codebook (this is experimental...)");
        String uri = ArtDecorCalls.getProjectIndexURI(protocolPrefix);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document dom = documentBuilder.parse(uri);

            //get the root element
            Element domElement = dom.getDocumentElement();

            //get a nodelist of elements
            NodeList nodeList = domElement.getElementsByTagName("dataset");

            if(nodeList != null) {
                // newest version will be at the last position of the list
                Element element = (Element) nodeList.item(nodeList.getLength()-1);
                datasetId = element.getAttribute("id");
                logger.log(Level.INFO, "Found version {} with id {}",element.getAttribute("versionLabel"), element.getAttribute("id"));
            }
        } catch (Exception e){
            String message = "There was an issue retrieving retrieving version information of available housekeeping codebook data using the following uri: "+uri+"\nPerhaps it doesn't exist yet?";
            LogTracker.logMessage(this.getClass(), message);
        }
    }
}
