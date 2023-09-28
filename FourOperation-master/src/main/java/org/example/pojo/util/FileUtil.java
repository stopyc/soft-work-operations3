package org.example.pojo.util;

import org.example.pojo.exception.OperandException;
import org.example.pojo.po.Equation;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author 3121005018, yc
 * */
public class FileUtil {
    /**
     * 将内容写入指定路径文件
     * @param path 文件路径
     * @param context 写入内容
     * @return 成功与否
     * */
    public static boolean write(String path, List<String> context){
        try {
            List<String> list = new ArrayList<>();
            //加上序号
            for(int i=0;i<context.size();i++) {
                list.add(i, i + "、" + context.get(i));
            }
            Files.write(Paths.get(path), list);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 从指定路径文件读取成表达式数组
     * @param path 文件路径
     * @return 表达式数组
     * */
    public static List<Equation> readAsEquation(String path){
        List<Equation> lines;
        try {
            lines = Files.readAllLines(Paths.get(path)).stream().map(s -> {
                try {
                    //除去序号
                    return Equation.parse(s.substring(s.indexOf("、")+1));
                } catch (OperandException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return lines;
    }
    public static List<Float> readAsAnswer(String path){
        List<Float> lines;
        try {
            //除去序号
            lines = Files.readAllLines(Paths.get(path)).stream().map(s -> Float.parseFloat(s.substring(s.indexOf("、")+1))).toList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return lines;
    }
}
