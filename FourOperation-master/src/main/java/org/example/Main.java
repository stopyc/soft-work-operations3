package org.example;

import org.example.pojo.po.Equation;
import org.example.pojo.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    private static Integer r = null;
    private static Integer n = null;
    private static String e = null;
    private static String a = null;


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
    /**
     * @param exerciseNum 所需生成的题目数量
     * @param valueLimitation 指定范围参数,操作数及操作数分母＜valueLimitation
     * */
    public static void generateExercise(int exerciseNum, int valueLimitation){
        List<Equation> list = new ArrayList<>();
        Random r = new Random();
        //随机生成表达式,并筛掉重复的,直到数量达到目标
        for(;list.size()<exerciseNum;) {
            int operandNo = r.nextInt(3) + 2;
            int operatorNo = operandNo - 1;
            int bracketsNo = 1;
            list.add(Equation.generate(operandNo, operatorNo, bracketsNo, 1, valueLimitation));
            Equation.filter(list);
        }
        //写入文件
        FileUtil.write("Exercises.txt", list.stream().map(Equation::toString).toList());
        FileUtil.write("Answers.txt", list.stream().map(e -> e.getResult() + "").toList());
    }


    /**
     * @param args 命令行参数数组
     * */
    public static void processArgs(String[] args){
        for(int i=0;i< args.length;i++){
            switch (args[i]){
                //可能有输入错误
                case "-r" -> r = Integer.parseInt(args[i+1]);
                case "-n" -> n = Integer.parseInt(args[i+1]);
                case "-e" -> e = args[i+1];
                case "-a" -> a = args[i+1];
            }
        }
    }

    /**
     * @param equation1 表达式1
     * @param equation2 表达式2
     * @return 返回两表达式是否重复
     * */
    public static boolean checkRepetition(Equation equation1, Equation equation2){
        return true;
    }
}