package cn.jji8.Floatingmarket.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
/**
 * 用于计算表达试
 * 过时了，改用js了
 * */
public class formula {
    String formula;
    static boolean tiaoshi = false;//开启调试模式可以获得计算步骤
    /**
     * 设置调试模式开关
     * */
    static void settiaoshi(boolean a){
        tiaoshi = a;
    }
    /**
     * 通过一个公式构造公式
     * */
    formula(String formula){
        this.formula = formula;
    }
    /**
     *
     * */
    /**
     * 静态的使用公式计算值
     * @param  map <变量，变量对应数值>
     * */
    static double calculation(Map<String,Double> map,String formula){
        Set<String> set = map.keySet();
        String operation = new String(formula);
        for(String a:set){
            operation = operation.replaceAll(a,String.valueOf(map.get(a)));
        }
        return noTranslatecalCulation(operation);
    }
    /**
     * 计算带括号的试子
     * */
    static double bracketsCulation(String formula){
        String formulaX = formula;
        int Pointer = 0;//指针，指向字符串读取到的位置。
        List<Integer> Symbol = new ArrayList<Integer>();//括号的位置列表
        int bracketsnumber = 0;//括号的堆叠数量
        int Recapitulation = 0;//回括的数量
        for(;Pointer<formula.length();Pointer++){
            switch (formula.charAt(Pointer)){
                case '(':{
                    bracketsnumber++;
                    Symbol.add(Pointer);
                    break;
                }
                case ')':{
                    Recapitulation++;
                    if(bracketsnumber==Recapitulation){
                        String zifu = formula.substring(Symbol.get(Symbol.size()-Recapitulation)+1,Pointer);
                        String s = Double.toString(bracketsCulation(zifu));
                        formulaX = formulaX.replace("("+zifu+")",s);
                        Recapitulation = 0;
                        bracketsnumber = 0;
                    }else if(bracketsnumber<0){
                        System.out.println("你有')'但是前面缺少'（'");
                    }
                    break;
                }
            }
        }
        return noTranslatecalCulation(formulaX);
    }
    /**
     * 计算没有任何变量并且不带括号的的式子
     * */
    static double noTranslatecalCulation(String formula){
        int Pointer = 0;//指针，指向字符串读取到的位置。
        /**     1  +
                2  -
                3  *
                4  /      * */
        int Symbol = 1;//上一个符号
        double result = 0;//计算结果
        StringBuffer number = new StringBuffer();//数字字符
        for(;Pointer<formula.length();Pointer++){
            switch (formula.charAt(Pointer)){
                case '+':{
                    try {
                        result = calculation(Symbol,result,Double.valueOf(number.toString()));
                    }catch (NumberFormatException a){
                        System.out.println(number.toString()+"不是一个数值");
                    }
                    Symbol = 1;
                    number = new StringBuffer();
                    break;
                }
                case '~':{
                    try {
                        result = calculation(Symbol,result,Double.valueOf(number.toString()));
                    }catch (NumberFormatException a){
                        System.out.println(number.toString()+"不是一个数值");
                    }
                    Symbol = 2;
                    number = new StringBuffer();
                    break;
                }
                case '*':{
                    try {
                        result = calculation(Symbol,result,Double.valueOf(number.toString()));
                    }catch (NumberFormatException a){
                        System.out.println(number.toString()+"不是一个数值");
                    }
                    Symbol = 3;
                    number = new StringBuffer();
                    break;
                }
                case '/':{
                    try {
                        result = calculation(Symbol,result,Double.valueOf(number.toString()));
                    }catch (NumberFormatException a){
                        System.out.println(number.toString()+"不是一个数值");
                    }
                    Symbol = 4;
                    number = new StringBuffer();
                    break;
                }
                default:{
                    number.append(formula.charAt(Pointer));
                    break;
                }
            }
        }
        try {
            result = calculation(Symbol,result,Double.valueOf(number.toString()));
        }catch (NumberFormatException a){
            System.out.println(number.toString()+"不是一个数值");
        }
        if(tiaoshi){
            System.out.println(formula+"="+result);
        }
        return result;
    }
    /**
     * 使用公式计算数值
     * @param  map <变量，变量对应数值>
     * */
    double calculation(Map<String, Double> map){
        return calculation(map,formula);
    }
    /**
     * 计算两个数
     * @param symbol
     *          1  +
     *          2  -
     *          3  *
     *          4  /
     * @return b 错误
     * */
    static double calculation(int symbol, double a, double b){
        switch (symbol){
            case 1:{
                return a + b;
            }
            case 2:{
                return a - b;
            }
            case 3:{
                return a * b;
            }
            case 4:{
                return a / b;
            }
        }
        return b;
    }
}
