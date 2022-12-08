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

package palgadatatranslator;
import javafx.application.Application;
import javafx.stage.Stage;
import palgadatatranslator.gui.MainWindow;

public class PALGADataTranslator {

    public static void main(String [] args) {
        Application.launch(StartApplication.class, args);
    }

    public static class StartApplication extends Application {
        @Override
        public void start(Stage stage) {
            MainWindow mainWindow = new MainWindow();
            mainWindow.createMainWindow(stage);
        }
    }
}
