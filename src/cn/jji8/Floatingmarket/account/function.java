package cn.jji8.Floatingmarket.account;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 一个函数类，用于加载调用js函数
 * */
public class function {
    Invocable Invocable;
    File file;
    /**
     * 通过一个js文件创建一个js
     * */
    public function(File file){
        this.file = file;
        FileReader FileReader = null;
        try {
            FileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("没用找到文件");
            e.printStackTrace();
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scriptEngine = manager.getEngineByName("js");
        try {
            scriptEngine.eval(FileReader);
        } catch (ScriptException e) {
            System.out.println(file+":你的脚本初始化出错");
            e.printStackTrace();
        }
        try {
            FileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Invocable = (Invocable)scriptEngine;
    }
    /**
     * 调用js中的函数
     * */
    public Object function(String name,variable value){
        Object sss = null;
        try {
            sss = Invocable.invokeFunction(name,value);
        } catch (ScriptException e) {
            System.out.println(file+":你的脚本运行出错");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println(file+":你的脚本中没有"+name+"方法");
            e.printStackTrace();
        }
        return sss;
    }
    /**
     * 调用js中的指定方法，返回一个double
     * */
    public double Doublefunction(String name,variable value){
        Object sss = function(name,value);
        if(sss==null){
            return -1;
        }
        double qqq = -1;
        try {
            qqq = Double.parseDouble(sss.toString());
        }catch (NumberFormatException eee){
            System.out.println(file+":你的脚本返回的”"+sss.toString()+"“不是一个数");
            eee.printStackTrace();
        }
        return qqq;
    }
}
