package cn.jji8.Floatingmarket.account;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class 测试 {
    public static void main(String[] args) {
        formula formula = new formula("变量1");
        Map map = new HashMap<String,Double>();
        map.put("变量1",10);
        map.put("变量2",13);
        map.put("变量3",14);
        map.put("变量4",8);
        map.put("变量5",5);
        double a = formula.calculation(map);
        System.out.println(a);

        File file = new File("C:\\工作路径\\新建文本文档.yml");
        Formulalist formulalist = new Formulalist();
        formulalist.setFormulalist(file);
        double s = formulalist.calculation(3,new HashMap<>());
        System.out.println(s);
    }
}
