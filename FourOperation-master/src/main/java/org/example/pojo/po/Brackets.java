package org.example.pojo.po;

/**
 * 括号类
 * @author 3121005017, yef
 * */
public class Brackets extends Arithmetic{
    //括号类型,只有左(0),右(1)两种
    private int type;
    public static final Brackets LEFT_BRACKET = new Brackets(0, 3, "(");
    public static final Brackets RIGHT_BRACKET = new Brackets(1, 4, ")");

    /**
     * @param index 括号类型
     * @return 返回对应的括号对象
     * */
    public static Brackets getByIndex(int index){
        switch (index) {
            case 0: return LEFT_BRACKET;
            case 1: return RIGHT_BRACKET;
            default: return null;
        }
    }
    public Brackets(int type, int priority, String value) {
        this.type = type;
        this.priority = priority;
        this.value = value;
    }
}
