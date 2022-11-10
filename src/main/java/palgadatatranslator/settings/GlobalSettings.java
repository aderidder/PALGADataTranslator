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

import java.util.*;

/**
 * stores some global settings
 */
public class GlobalSettings {
    public static String server = "https://decor.nictiz.nl/services/";
    private static Map<String, String> protocolNameToPrefixMap = new TreeMap<>();

    // in the future this will be read from some file
    static{
        protocolNameToPrefixMap.put("Colonbiopt", "ppcolbio-");
        protocolNameToPrefixMap.put("ColonRectumcarcinoom", "ppcolcar-");
        protocolNameToPrefixMap.put("inherit_test", "s2nki-");
    }

    /**
     * returns the default protocol name
     * @return the default protocol name
     */
    static String getDefaultProtocolName(){
        return "Colonbiopt";
    }

    /**
     * get all the protocol available
     * @return a set with all the protocols
     */
    public static Set<String> getProtocols(){
        return protocolNameToPrefixMap.keySet();
    }

    /**
     * get a prefix of a protocol, which is necessary for the art-decor calls
     * @param protocolName name of the protocol
     * @return  prefix of the protocol
     */
    public static String getProtocolPrefix(String protocolName){
        return protocolNameToPrefixMap.get(protocolName);
    }

}
