package OrionLuouo.test.Craft.nonsense.blah;

import static java.lang.Math.*;

public class Heart {
    public static void main(String[] args) {
        heart2();
    }
    public static void heart2() {
        double x, y, a;
        char s[]=new char[]{'I',' ','l','o','v','e',' ','y','o','u','!'}; // 定义一个字符数组，存储要打印的文字信息
        int index=0; // 定义索引变量，用于迭代字符数组
        //x,y的初始值，限制条件可以调整
        for(y=1.3;y>-1.1;y-=0.15) { // 遍历y坐标，从1.3递减到-1.1，步长为0.15
            index=0; // 重置索引变量，准备开始新的行打印
            for(x=-1.2;x<=1.2;x+=0.05) { // 遍历x坐标，从-1.2递增到1.2，步长为0.05
                double result=x*x+pow((5.0*y/4.0-sqrt(abs(x))),2); // 计算一个方程的值，用于判断点(x,y)是否在心形内部
                if(result<=1) { // 如果点在心形内部
                    System.out.print(s[index]); // 打印当前索引对应的字符
                    index=(index+1)%11; // 索引增加，取模11确保字符数组循环
                } else { // 如果点不在心形内部
                    System.out.print(' '); // 打印空格
                }
            }
            System.out.println(" "); // 打印换行符，开始新的一行
        }
    }
}