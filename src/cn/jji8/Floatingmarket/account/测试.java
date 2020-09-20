package cn.jji8.Floatingmarket.account;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class 测试 {
    public static void main(String[] args) {
        function function = new function(new File("C:/工作路径/123.js"));
        variable variable = new variable();
        variable.setNumberOfItems(10);
        double sss = function.Doublefunction("价格",variable);
        System.out.println(sss);
    }
}
