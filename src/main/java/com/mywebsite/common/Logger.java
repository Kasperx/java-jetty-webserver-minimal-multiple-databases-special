/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.java.com.mywebsite.common;

/**
 *
 * @author cgl
 */
public interface Logger {
    void warn(String text);
    void info(String text);
    void debug(String text);
    void error(String text);
    void error(Exception error);
    void error(String text, Exception error);
}
