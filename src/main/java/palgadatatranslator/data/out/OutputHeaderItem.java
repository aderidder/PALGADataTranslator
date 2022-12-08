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

package palgadatatranslator.data.out;

/**
 * class which stores information about an item in the original header: the original headername, its translation and a repeat number
 * the repeat number was added to support a wide-format for tranSMART. Though tranSMART support was
 * removed we decided to keep the repeat for possible future updates
 * when the data is not written in wide-format, the repeat will always be 1; otherwise it will be >=1
 *
 * as we removed transmart and it's wideformat, this class seems unnecessary. Maybe remove it in the future or add
 * "normal" support for the wide format?
 */
public class OutputHeaderItem {
    private final String origHeaderName;
    private final String translatedName;
    private final int repeat;

    /**
     * constructor for header without repeats (long format)
     * @param origHeaderName original header name
     * @param translatedName translated header name
     */
    OutputHeaderItem(String origHeaderName, String translatedName){
        this(origHeaderName, translatedName, 1);
    }

    /**
     * constructor for header with >=1 repeats (wide format)
     * @param origHeaderName original header name
     * @param translatedName translated header name
     * @param repeat         repeat number of this header item
     */
    OutputHeaderItem(String origHeaderName, String translatedName, int repeat){
        this.origHeaderName = origHeaderName;
        this.translatedName = translatedName;
        this.repeat = repeat;
    }

    /**
     * returns the original header name
     * @return the original header name
     */
    public String getOrigHeaderName() {
        return origHeaderName;
    }

    /**
     * returns this header entry's repeat number
     * @return the repeat number
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * returns the translated name
     * @return the translated name
     */
    String getTranslatedName() {
        return translatedName;
    }

}
