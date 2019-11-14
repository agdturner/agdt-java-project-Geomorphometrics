/*
 * Copyright 2019 Centre for Computational Geography, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.projects.geomorphometrics.io;

import java.io.File;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;
import uk.ac.leeds.ccg.andyt.projects.geomorphometrics.core.G_Strings;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class G_Files extends Generic_Files {
    
    public G_Files(File dataDir) throws IOException{
        super(dataDir);        
    }
    
    /**
     * @return {@code new File(getDefaultGenericDir(), Vector_Strings.s_Math)}.
     */
    public static File getDefaultDir() {
        File d = new File(Generic_Defaults.getDataDir(), G_Strings.s_project);
        d = new File(d, G_Strings.s_geomorphometrics);
        return d;
    }
}
