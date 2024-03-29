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

package palgadatatranslator.utils.enumerate;

/**
 * Types we can use for output
 * codes means e.g. the snomed code
 * descriptions means the e.g. the snomed description
 * codesystem_and_codes mean e.g. the snomed and snomed code
 */
public enum OutputFormatType {
    DESCRIPTIONS ("Text only"),
    CODES ("Code only"),
    CODESYSTEM_AND_CODES ("Codesystem and Code"),
    CODES_AND_DESCRIPTIONS ("Code and Text"),
    CODESYSTEM_AND_CODES_AND_DESCRIPTIONS ("Codesystem, Code and Text"),;

    private final String prettyString;

    /**
     * constructor
     * @param prettyString pretty string for the OutputFormatType
     */
    OutputFormatType(String prettyString){
        this.prettyString = prettyString;
    }

    /**
     * returns the pretty string
     * @return the pretty string
     */
    public String getPrettyString(){
        return prettyString;
    }

    /**
     * returns the OutputFormatType based on the pretty string
     * @param prettyString the pretty string for the Enum
     * @return the OutputFormatType
     */
    public static OutputFormatType getEnum(String prettyString){
        for(OutputFormatType outputColumnType: OutputFormatType.values()){
            if(outputColumnType.prettyString.equalsIgnoreCase(prettyString)){
                return outputColumnType;
            }
        }
        return null;
    }
}
