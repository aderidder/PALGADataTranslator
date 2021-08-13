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

class StaticTexts {

    /**
     * returns the about title
     * @return the about title
     */
    static String getAboutTitle(){
        return "About the PALGA Protocol Data Translator...";
    }

    /**
     * returns the welcome text
     * @return the welcome text
     */
    static String getWelcomeText(){
        return "Welcome to the PALGA Protocol Data Translator!\n\n" + getHelpText();
    }

    /**
     * returns the help text
     * @return the help text
     */
    static String getHelpText(){
        return  "Click Run to start the Wizard, which will guide you through the process!\n" +
                "The Wizard expects you to:\n" +
                "* Provide a data file, which you want to translate\n" +
                "* Select the PALGA protocol\n" +
                "* Select the type of output you would like to generate (currently only text available)\n" +
                "* The desired output - whether you would like to have the labels, codes, etc." +
                "* The language of the source file (second page of the wizard)\n" +
                "Output is written to the directory that contains your datafile.\n\n";
    }

    /**
     * returns the about text
     * @return the about text
     */
    static String getAboutText(){
        return  "The PALGA Protocol Data Translator was designed and created by:" +
                "\n\tSander de Ridder (NKI 2017; VUmc 2018/2019/2020/2021)"+
                "\n\tJeroen Belien (VUmc)\n" +
                "This project was sponsored by MLDS project OPSLAG and KWF project TraIT2Health-RI (WP: Registry-in-a-Box)\n\n" +
                "---------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+
                "Copyright 2017 NKI / AvL; 2018/2019/2020/2021 VUmc\n" +
                "\n" +
                "PALGA Protocol Data Translator is free software: you can redistribute it and/or modify\n" +
                "it under the terms of the GNU General Public License as published by\n" +
                "the Free Software Foundation, either version 3 of the License, or\n" +
                "(at your option) any later version.\n" +
                "\n" +
                "PALGA Protocol Data Translator is distributed in the hope that it will be useful,\n" +
                "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                "GNU General Public License for more details.\n" +
                "\n" +
                "You should have received a copy of the GNU General Public License\n" +
                "along with PALGA Protocol Data Translator. If not, see <http://www.gnu.org/licenses/>\n";
    }
}
