package cn.jji8.Floatingmarket.account;

import java.util.HashMap;
import java.util.Map;

public class 测试 {
    public static void main(String[] args) {
        formula formula = new formula("变量1+变量2-变量3*变量4/变量5");
        Map map = new HashMap<String,Double>();
        map.put("变量1",10);
        map.put("变量2",13);
        map.put("变量3",14);
        map.put("变量4",8);
        map.put("变量5",5);
        double a = formula.calculation(map);
        System.out.println(a);
    }
}
