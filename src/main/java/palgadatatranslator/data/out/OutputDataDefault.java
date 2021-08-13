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

import palgadatatranslator.settings.RunParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Defaults for OutputData
 */
abstract class OutputDataDefault implements OutputData{
    List<OutputHeaderItem> headerList = new ArrayList<>();
    RunParameters runParameters;

    /**
     * constructor
     * @param runParameters run parameters
     */
    OutputDataDefault(RunParameters runParameters){
        this.runParameters = runParameters;
    }

}
