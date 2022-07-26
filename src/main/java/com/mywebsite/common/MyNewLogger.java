/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.mywebsite.common;

/**
 *
 * @author cgl
 */
public class MyNewLogger implements Logger{

    public MyNewLogger(String className, int infoLvl, String dateFormat) {
        
    }
    String getClassName(String className, int debugLvl) {
        
        return className;
    }
    @Override
    public void info(String text) {
        System.out.println(text);
    }
    
    @Override
    public void debug(String text) {
        System.out.println(text);
    }
    @Override
    public void error(String text) {
        System.err.println(text);
    }
    @Override
    public void warn(String text) {
        System.out.println(text);
    }
    @Override
    public void error(String text, Exception e) {
        System.err.println(text);
//        e.printStackTrace();
        error(e);
    }
    @Override
    public void error(Exception error) {
        error.printStackTrace();
    }
}
