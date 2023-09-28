package org.example;

import org.example.pojo.po.Equation;
import org.example.pojo.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static Integer r = null;
    private static Integer n = null;
    private static String e = null;
    private static String a = null;

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    /**
     * @param exercisePath 内含表达式的文件路径
     * @param answerPath 存放计算结果的文件路径
     * @param gradePath 统计对错情况及对应题目序号的文件路径
     * */
    public static void analyse(String exercisePath, String answerPath, String gradePath){
        List<Equation> exercises = FileUtil.readAsEquation(exercisePath);
        List<Float> answers = FileUtil.readAsAnswer(answerPath);
        if(exercises.size() != answers.size()){
            System.out.println("练习与答案数量不相同,只分析写了答案的题目");
        }

        //记录对错情况
        List<String> corrects = new ArrayList<>();
        List<String> wrongs = new ArrayList<>();
        for(int i=0;i<answers.size();i++){
            if(Math.abs(answers.get(i) - exercises.get(i).getResult()) < 0.000001){
                corrects.add(i+"");
            }else{
                wrongs.add(i+"");
            }
        }
        StringBuilder sb1 = new StringBuilder();
        sb1.append("Correct:").append(corrects.size()).append("(");
        sb1.append(String.join(",", corrects)).append(")");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Wrong:").append(wrongs.size()).append("(");
        sb2.append(String.join(",", wrongs)).append(")");
        FileUtil.write(gradePath, Arrays.asList(sb1.toString(), sb2.toString()));
    }
}