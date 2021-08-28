package se.liu.ida.logic;

import java.awt.EventQueue;

/**
 * Startup class
 */
public class Startup    {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        EventQueue.invokeLater(Startup::new);
    }

}