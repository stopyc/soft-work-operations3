package org.example.pojo.po;


import org.example.pojo.exception.OperandException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 表达式类
 * @author 3121005018, yc
 * */
public class Equation {
    //中缀表达式
    private List<Arithmetic> infix;
    //后缀表达式
    private List<Arithmetic> postfix;
    //运算结果
    private float result;
    //运算过程是否含负数
    private boolean of;

    public Equation(List<Arithmetic> infix) {
        this.infix = new ArrayList<>(infix);
        this.postfix = this.infixToPostfix();
        this.result = this.calculate();
    }

    public String toString(){
        //把List转成流,对流里的每个元素调用其toString(),然后将流整合成一个以空格分隔的字符串返回
        return infix.stream().map(Arithmetic::toString).collect(Collectors.joining(" "));
    }

    /**
     * 用来过滤重复表达式,按顺序层层筛选,由于转换成后缀表达式,不用考虑括号
     * 1. 先去除运算过程含负数的
     * 2. 先比较结果
     * 3. 比较表达式是否一样
     * 4. 再比较包含的运算符是否相同
     * 5. 比较第一次运算的两数是否只是交换位置
     * @param list 要过滤的表达式数组
     * @return 过滤完成的表达式
     * */
    public static List<Equation> filter(List<Equation> list){
        for(int i=0;i < list.size();i++){
            Equation equation = list.get(i);
            //如果运算过程含负数,则跳过
            if(equation.isOf()){
                list.remove(equation);
                //remove会整体前移
                i--;
                continue;
            }
            //和整个list比较
            //标签方便下面层层嵌套能直接goto出来
            flag:
            for(int o=0;o< list.size();o++){
                Equation toCompare = list.get(o);
                //删除后有空位,要跳过
                if(toCompare == null){
                    continue;
                }
                //遇到自己就跳过
                if(equation == toCompare){
                    continue;
                }
                //先比较结果
                if(Math.abs(equation.getResult() - toCompare.getResult()) < 0.000001) {
                    //结果相同,看是否完全一样
                    if(equation.equals(toCompare)){
                        list.remove(equation);
                        //remove会整体前移
                        i--;
                        break flag;
                    }
                    //再比较运算符
                    List<Arithmetic> postfix1 = equation.getPostfix();
                    List<Arithmetic> postfix2 = toCompare.getPostfix();
                    List<Operator> operators1 = equation.getOperators();
                    List<Operator> operators2 = toCompare.getOperators();
                    //有不同运算符就保留
                    if(operators1.size() != operators2.size()){
                        break flag;
                    }
                    for(int j=0;j<operators1.size();j++){
                        if(operators1.get(j) != operators2.get(j)){
                            break flag;
                        }
                    }

                    //运算符相同,只比较第一次计算的两数字是否交换位置
                    //找到第一个运算符,取前两个数字
                    List<Operand> operands1 = new ArrayList<>();
                    List<Operand> operands2 = new ArrayList<>();
                    for(int j=0;j<postfix1.size();j++){
                        if(postfix1.get(j) instanceof Operator){
                            operands1.add((Operand) postfix1.get(j-1));
                            operands1.add((Operand) postfix1.get(j-2));
                            break;
                        }
                    }
                    for(int j=0;j<postfix1.size();j++){
                        if(postfix2.get(j) instanceof Operator){
                            operands2.add((Operand) postfix2.get(j-1));
                            operands2.add((Operand) postfix2.get(j-2));
                            break;
                        }
                    }
                    //比较两对数字
                    if((operands1.get(0).equals(operands2.get(0)) || operands1.get(0).equals(operands2.get(1)))
                            && (operands1.get(1).equals(operands2.get(0)) || operands1.get(1).equals(operands2.get(1)))){
                        list.remove(equation);
                        //remove会整体前移
                        i--;
                        break flag;
                    }else{
                        //两对数字不相同,保留
                        break flag;
                    }
                }else{
                    //结果不一样,保留
                    break flag;
                }
            }

        }
        return list.stream().toList();
    }

    /**
     * 用来生成随机表达式
     * @param operandNo 操作数数量
     * @param operatorNo 运算符数量
     * @param bracketsNo 括号数量
     * @param lowEnd 生成表达式中操作数和真分数分母范围的下限
     * @param upEnd 生成表达式中操作数和真分数分母范围的上限
     * @return 生成的表达式
     * */
    public static Equation generate(int operandNo, int operatorNo, int bracketsNo
            , int lowEnd, int upEnd){
        Random r = new Random();
        int scope = upEnd - lowEnd;
        List<Arithmetic> arithmetics = new ArrayList<>();
        List<Operand> operands = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();
        List<Brackets> brackets = new ArrayList<>();

        try {
            for (int i = 0; i < operandNo; i++) {
                // 操作数类型 自然数(0),真分数(1)
                int type = r.nextInt(10)%2;
                if(0 == type){
                    //生成随机整数
                    operands.add(new Operand(type, r.nextInt(scope) + lowEnd + ""));
                }else if (1 == type){
                    //生成真分数
                    int denominator = r.nextInt(scope) + lowEnd + 1;
                    // 分子 > 0
                    int numerator = r.nextInt(denominator - 1) + 1;
                    String str = numerator + "/" + denominator;
                    operands.add(new Operand(type, str));
                }
            }

            for (int i = 0; i < operatorNo; i++) {
                // 除去等号
                int index = r.nextInt(4) + 1;
                operators.add(Operator.getByIndex(index));
            }

            for (int i = 0; i < bracketsNo; i++) {
                brackets.add(Brackets.getByIndex(0));
                brackets.add(Brackets.getByIndex(1));
            }

            for (int i = 0; i < operands.size(); i++) {
                if(operands.get(i) != null){
                    arithmetics.add(operands.get(i));
                }
                if(i == operands.size()-1){
                    break;
                }
                if(operators.get(i) != null) {
                    arithmetics.add(operators.get(i));
                }
            }

            /*int type = r.nextInt(10);
            switch(operatorNo){
                case 2:
                    switch (bracketsNo){
                        case 0

                    };
                    break;
                case 3:
                    switch (bracketsNo){

                    }
                    break;

            }*/


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return new Equation(arithmetics);
    }

    /**
     * 计算表达式
     * @return 结果
     * */
    public Float calculate(){
        setOf(false);

        Stack<Float> stack = new Stack<>();
        Arithmetic arithmetic;
        for (Arithmetic value : postfix) {
            arithmetic = value;
            //一遇到运算符就运算一次
            if (arithmetic instanceof Operator) {
                //被动数
                Float num2 = stack.pop();
                //主动数
                Float num1 = stack.pop();
                //计算
                Float result = ((Operator) arithmetic).operate(num1, num2);
                if(result < 0){
                    setOf(true);
                }
                stack.push(result);
            } else {
                stack.push(((Operand) arithmetic).getTrueValue());
            }
        }
        return stack.pop();
    }

    /**
     * 将中缀表达式转换为后缀表达式
     * @return 返回转换结果
     * */
    public List<Arithmetic> infixToPostfix(){
        Stack<Arithmetic> stack = new Stack<>();
        List<Arithmetic> postfix = new ArrayList<>();
        for(int start = 0; start < infix.size(); start++){
            //如果是运算符
            if(infix.get(start).priority > 0) {
                //栈空 或 "(" 或 符号优先级>栈顶符号 且 不为")" 直接进栈
                if (stack.isEmpty() || infix.get(start).priority == 3 ||
                        ((infix.get(start).priority > stack.peek().priority) && infix.get(start).priority < 4)) {
                    stack.push(infix.get(start));
                } else if (!stack.isEmpty() && infix.get(start).priority <= stack.peek().priority) {
                    //栈非空 且 符号优先级≤栈顶符号, 出栈; 直到 栈为空 或 遇到了"("
                    while (!stack.isEmpty() && infix.get(start).priority <= stack.peek().priority) {
                        if (stack.peek().priority == 3) {
                            stack.pop();
                            break;
                        }
                        postfix.add(stack.pop());
                    }
                    stack.push(infix.get(start));
                } else if (infix.get(start).priority == 4) {
                    //")",依次出栈直到空栈或遇到第一个"(",此时"("出栈
                    while (!stack.isEmpty()) {
                        if (stack.peek().priority == 3) {
                            stack.pop();
                            break;
                        }
                        postfix.add(stack.pop());
                    }

                }
            }else if(infix.get(start).priority == -1){
                postfix.add(infix.get(start));
            }
        }
        while(!stack.isEmpty()){
            postfix.add(stack.pop());
        }
        return postfix;
    }

    public boolean isOf() {
        return of;
    }
    /**
     * 从表达式中提取出所有运算符
     * @return 运算符数组
     * */
    public List<Operator> getOperators(){
        List<Operator> operators = new ArrayList<>();
        for(int j=0;j<postfix.size();j++){
            if(postfix.get(j) instanceof Operator){
               operators.add((Operator)(postfix.get(j)));
            }
        }
        return operators;
    }
    /**
     * 从表达式中提取出所有操作数
     * @return 操作数数组
     * */
    public List<Operand> getOperands(){
        List<Operand> operands = new ArrayList<>();
        for(int j=0;j<postfix.size();j++){
            if(postfix.get(j) instanceof Operand){
                operands.add((Operand)(postfix.get(j)));
            }
        }
        return operands;
    }

    public List<Arithmetic> getInfix() {
        return infix;
    }

    public void setOf(boolean of) {
        this.of = of;
    }

    public List<Arithmetic> getPostfix() {
        return postfix;
    }

    public float getResult() {
        return result;
    }

    /**
     * 将字符串表达式转为表达式对象Equation
     * @param str 要转换的字符串
     * @return 表达式
     * */
    public static Equation parse(String str) throws OperandException {
        List<Arithmetic> list = new ArrayList<>();
        //\\s+正则表达式,以空格分隔成多个字符串
        for(String s : str.split("\\s+")){
            //真分数
            if(s.contains("/")){
                list.add(new Operand(1, s));
                continue;
            }
            //其他
            switch (s) {
                case "+" -> list.add(Operator.PLUS);
                case "-" -> list.add(Operator.MINUS);
                case "×" -> list.add(Operator.MULTIPLY);
                case "÷" -> list.add(Operator.DIVIDE);

                default -> list.add(new Operand(0, s));
            }
        }
        return new Equation(list);
    }

    public boolean equals(Equation equation){
        return this.toString().equals(equation.toString());
    }
}
