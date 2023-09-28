package org.example.pojo.po;


import org.example.pojo.exception.OperandException;

import java.util.Random;

/**
 * 操作数类
 * @author 3121005035, OuroborosNo2
 * */
public class Operand extends Arithmetic{
    //操作数类型,区分自然数(0)和真分数(1)
    private int type;
    // 操作数的真值
    private float trueValue;

    public int getType() {
        return type;
    }

    public float getTrueValue() {
        return trueValue;
    }
    public Operand(int type, String value) throws OperandException {
        this.type = type;
        this.value = value;
        //运算数的优先级一定是最小的
        this.priority = -1;
        if(type == 0) {
            this.trueValue = Integer.parseInt(value);
        }else if(type == 1){
            //如果value的值不合法,会报错
            try {
                String[] split = value.split("/");
                this.trueValue = Float.parseFloat(split[0]) / Float.parseFloat(split[1]);
            }catch(Exception e){
                e.printStackTrace();
                throw new OperandException("非法value(操作数)");
            }
        }else{
            throw new OperandException("非法type(操作数种类),只能输入0或1");
        }
    }

    /**
     * 获取指定范围随机整数
     * @param lowEnd 下限
     * @param upEnd 上限
     * @return 随机整数
     * */
    public Operand getRandInteger(int lowEnd, int upEnd){
        int index = upEnd - lowEnd;
        Random r = new Random();
        try {
            return new Operand(0 ,r.nextInt(index) + lowEnd + "");
        } catch (OperandException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定范围随机真分数
     * @param upEnd 0＜分母＜upEnd
     * @return 随机真分数
     * */
    public Operand getRandFraction(int upEnd){
        Random r = new Random();
        int denominator = r.nextInt(upEnd - 1) + 1;
        int numerator = r.nextInt(denominator);
        try {
            return new Operand(1, numerator + "/" + denominator);
        } catch (OperandException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equals(Operand operand){
        return Math.abs(this.getTrueValue() - operand.getTrueValue()) < 0.000001;
    }
}
