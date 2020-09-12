package cn.jji8.Floatingmarket.account;

import java.util.Map;
import java.util.Set;

public class formula {
    String formula;
    /**
     * 通过一个公式构造公式
     * */
    formula(String formula){
        this.formula = formula;
    }
    /**
     * 使用公式计算数值
     * @param  map <变量，变量对应数值>
     * */
    double calculation(Map<String,Double> map){
        Set<String> set = map.keySet();
        String operation = new String(formula);
        for(String a:set){
            operation = operation.replaceAll(a,String.valueOf(map.get(a)));
        }
        System.out.println(operation);
        /**

         1  +
         2  -
         3  *
         4  /

         * */
        int Symbol = 0;//符号
        double value = 0;//值
        StringBuffer number = new StringBuffer();
        boolean initialread = true;//初始读取
        for(int i = 0;i<operation.length();i++){
            char zhi = operation.charAt(i);
            if(initialread){
                switch (zhi){
                    case ' ':{
                        break;
                    }
                    case '+':{
                        Symbol = 1;
                        try {
                            value = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        initialread = false;
                        number = new StringBuffer();
                        break;
                    }
                    case '-':{
                        Symbol = 2;
                        try {
                            value = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        initialread = false;
                        number = new StringBuffer();
                        break;
                    }
                    case '*':{
                        Symbol = 3;
                        try {
                            value = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        initialread = false;
                        number = new StringBuffer();
                        break;
                    }
                    case '/':{
                        Symbol = 4;
                        try {
                            value = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        initialread = false;
                        number = new StringBuffer();
                        break;
                    }
                    default:{
                        number.append(zhi);
                        break;
                    }
                }
            }else {
                double temporary = 1;
                switch (zhi){
                    case ' ':{
                        break;
                    }
                    case '+':{
                        try {
                            temporary = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        value = calculation(Symbol,value,temporary);
                        Symbol = 1;
                        number = new StringBuffer();
                        break;
                    }
                    case '-':{
                        try {
                            temporary = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        value = calculation(Symbol,value,temporary);
                        Symbol = 2;
                        number = new StringBuffer();
                        break;
                    }
                    case '*':{
                        try {
                            temporary = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        value = calculation(Symbol,value,temporary);
                        Symbol = 3;
                        number = new StringBuffer();
                        break;
                    }
                    case '/':{
                        try {
                            temporary = Double.valueOf(number.toString());
                        }catch (NumberFormatException a){
                            System.out.println(number.toString()+"不是一个数值");
                        }
                        value = calculation(Symbol,value,temporary);
                        Symbol = 4;
                        number = new StringBuffer();
                        break;
                    }
                    default:{
                        number.append(zhi);
                    }
                }
            }
        }
        double temporary = 0;
        try {
            temporary = Double.valueOf(number.toString());
        }catch (NumberFormatException a){
            System.out.println(number.toString()+"不是一个数值");
        }
        value = calculation(Symbol,value,temporary);
        return value;
    }
    /**
     * 计算两个数
     * @param symbol
     *          1  +
     *          2  -
     *          3  *
     *          4  /
     * @return 0 错误
     * */
    double calculation(int symbol,double a,double b){
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
        return 0;
    }
}
