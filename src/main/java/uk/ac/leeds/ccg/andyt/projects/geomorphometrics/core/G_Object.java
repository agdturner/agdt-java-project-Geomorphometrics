/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.geomorphometrics.core;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class G_Object implements Serializable {
    
    public G_Environment env;
    
    public G_Object(G_Environment e) {
        env = e;
    }
}
