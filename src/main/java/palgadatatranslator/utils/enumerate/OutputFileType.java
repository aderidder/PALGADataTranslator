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
 * types we can produce as output
 * originally we also supported transmart as an output type, but this was removed due to transmart being phased out
 */
public enum OutputFileType {
    TEXT ("Text file");

    private final String prettyString;

    /**
     * constructor
     * @param prettyString a pretty string for the Enum
     */
    OutputFileType(String prettyString){
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
     * returns the OutputFileType based on the pretty string
     * @param prettyString the pretty string
     * @return the OutputFileType
     */
    public static OutputFileType getEnum(String prettyString){
        for(OutputFileType outputFileType: OutputFileType.values()){
            if(outputFileType.prettyString.equalsIgnoreCase(prettyString)){
                return outputFileType;
            }
        }
        return null;
    }
}
