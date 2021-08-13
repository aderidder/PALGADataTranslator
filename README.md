# The PALGA Protocol Data Translator
## Introduction
PALGA is the national pathology registry of the Netherlands. A large portion of their data is collected using the PALGA protocols. Usage of these protocols ensures that data is collected in a standardised way, thereby ensuring high quality data. Researchers can request a dataset from PALGA for their research. Unfortunately, the data is available only in Dutch. 

The VUmc, NKI and PALGA worked together in a pilot project to design a process to make PALGA data available for international research. The process has two phases:
* Phase 1: 
    * Generate an Excel codebook for a PALGA protocol using the PALGA Protocol Codebook Generator
    * Translate the codebook / map it to a medical thesaurus
    * Convert the Excel codebook to an ART-DECOR XML using the PALGA Protocol Codebook to XML
    * Make the codebook available online in ART-DECOR 
* Phase 2:
    * Researcher with PALGA Protocol data translates his/her data using the PALGA Protocol Data Translator

The software you are currently looking at is the PALGA Protocol Data Translator, which can translate PALGA protocol data using the online codebooks, provided the necessary version of the codebook is available online in ART-DECOR.

## Creating an executable jar
You can use maven to create an executable jar file, using mvn package. The jar is placed in the target directory and can be run using java -jar <generated_jar_file>

## Translating a file
The program currently simply generates a translated text file. We may add support for additional output types in the future if there's a demand for it. 

### Usage parameters
| parameter | explanation | 
| --- | --- | 
| Protocol file | directory and name of the data file |
| Protocol | the palga protocol that was used to collect the data |
| Translate to | tekst file |
| Output format | determines what the output file will show, e.g. tekst only / codes only / combinations |

## How does it work
When the codebooks are created, each concept in the codebook is given a property called "PALGA_COLNAME". The value of this property matches the actual column name as found in the PALGA Protocol's data. This links the data file to the codebook. The Data Translator uses the protocol selected by the user to fetch which codebooks are available online. It then retrieves the codebook versions when necessary.    

## Remarks
* The program tries to find a version of the protocol that is specified in the datafile. If that version doesn't exist, the progam can't translate using the version and hence fails to translate the concept and values for these entries. 
* The program tries to translate the concepts and values, but to be able to do so, the entries have to be identical. So if the datafile contains "Yes, but" and the codebook contains "Yes but", the values are not identical and translation is not possible. In such a case the program reports the problem and writes the original value(s) to the output file.
* Obviously, quality of the translated data depends on the quallity of the codebooks. For example, if a codebook is mapped to e.g. SNOMED, the SNOMED codes can be retrieved; if an internal codesystem is used, only that id can be retrieved. 
* Although originally written for translating PALGA Protocol data, not much is preventing the tool from being usable for translating other datasets using other codebooks (assuming they are compatible). Basically all that is required is:
    * a link between the codebook and the datafile 
    * an entry in the list which links the codebook name to its ART-DECOR identifier
    * the codebook to have a PALGA_COLNAME property. This name is currently still hardcoded in the code, but this could be turned into a textual property if desired 

## About
PALGA Protocol Data Translator was designed and created by **Sander de Ridder** (NKI 2017; VUmc 2018/2019/2020) and Jeroen Belien (VUmc)<br>
This project was sponsored by MLDS project OPSLAG and KWF project TraIT2Health-RI (WP: Registry-in-a-Box)<br>

PALGA Protocol Data Translator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

PALGA Protocol Data Translator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.