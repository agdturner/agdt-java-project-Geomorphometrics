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
public class Geomorphometrics_Object implements Serializable {
    
    public Geomorphometrics_Environment env;
    
    public Geomorphometrics_Object(Geomorphometrics_Environment e) {
        env = e;
    }
}
