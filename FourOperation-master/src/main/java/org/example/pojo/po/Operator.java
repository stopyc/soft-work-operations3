package org.example.pojo.po;

/**
 * 运算符类
 * @author 3121005035, OuroborosNo2
 * */
public class Operator extends Arithmetic{

    //等号
    public static final Operator EQUAL = new Operator(0, 1, "=", 0);
    //加号
    public static final Operator PLUS = new Operator(1, 2, "+", 1);
    //减号
    public static final Operator MINUS = new Operator(1, 2, "-", 2);
    //乘号
    public static final Operator MULTIPLY = new Operator(2, 2, "×", 3);
    //除号
    public static final Operator DIVIDE = new Operator(2, 2, "÷", 4);

    /**
     * 根据this.value判断需要进行的运算,如果想更成体系,应该把Float换成Opeand
     * @param num1 主动操作数
     * @param num2 被动操作数
     * @return 结果
     * */
    public Float operate (Float num1, Float num2){
        switch (this.value){
            case "+" : return num1 + num2;
            case "-" : return num1 - num2;
            case "×" : return num1 * num2;
            case "÷" : return num1 / num2;
            default: return 0f;
        }
    }

    // 运算目数
    private int nOperation;
    // 表达式序号
    private int index;

    public Operator(int priority, int nOperation, String value, int index) {
        this.priority = priority;
        this.nOperation = nOperation;
        this.value = value;
        this.index = index;
    }

    /**
     * @param index 运算符号类型
     * @return  返回对应的运算符号对象
     * */
    public static Operator getByIndex(int index){
        switch (index){
            case 0 : return Operator.EQUAL;
            case 1 : return Operator.PLUS;
            case 2 : return Operator.MINUS;
            case 3 : return Operator.MULTIPLY;
            case 4 : return Operator.DIVIDE;
            default: return null;
        }
    }
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getnOperation() {
        return nOperation;
    }

    public void setnOperation(int nOperation) {
        this.nOperation = nOperation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
