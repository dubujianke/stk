//package com.harmonics;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Harmonics {
//
//    static double PI = Math.PI;
//
//    static int degree_ = 256;
//    static vec3[] coefs;
//
//    static double sqrt(double v) {
//        return Math.sqrt(v);
//    }
//
//    static double sin(double v) {
//        return Math.sin(v);
//    }
//
//    static double cos(double v) {
//        return Math.cos(v);
//    }
//
//    static double pow(double v, double b) {
//        return Math.pow(v, b);
//    }
//
//    class vectort<E> extends ArrayList {
//
//    }
//
//    class vec3 {
//        double x;
//        double y;
//        double z;
//    }
//
//    class Vertex {
//        double theta;
//        double phi;
//        vec3 color;
//    }
//
//    /**
//     * Kml
//     */
//    static double K(int l, int m) {
//        return (sqrt(((2 * l + 1) * Factorial(l - m)) / (4 * PI * Factorial(l + m))));
//    }
//
//    static int Factorial(int v) {
//        if (v == 0)
//            return (1);
//
//        int result = v;
//        while (--v > 0)
//            result *= v;
//        return (result);
//    }
//
//    double P(int l, int m, double x)
//    {
//        // 公式二不需要递归计算
//        if (l == m)
//            return (pow(-1.0f, m) * DoubleFactorial(2 * m - 1) * pow(sqrt(1 - x * x), m));
//
//        // 公式三
//        if (l == m + 1)
//            return (x * (2 * m + 1) * P(m, m, x));
//
//        // 公式一
//        return ((x * (2 * l - 1) * P(l - 1, m, x) - (l + m - 1) * P(l - 2, m, x)) / (l - m));
//    }
//
//    //二次阶乘
//    int DoubleFactorial(int x)
//    {
//        if (x == 0 || x == -1)
//            return (1);
//
//        int result = x;
//        while ((x -= 2) > 0)
//            result *= x;
//        return (result);
//    }
//
//    double y(int l, int m, double theta, double phi)
//    {
//        if (m == 0)
//            return (K(l, 0) * P(l, 0, cos(theta)));
//
//        if (m > 0)
//            return (sqrt(2.0f) * K(l, m) * cos(m * phi) * P(l, m, cos(theta)));
//
//        // 当m小于0时，预先乘上-1，再传给K
//        return (sqrt(2.0f) * K(l, -m) * sin(-m * phi) * P(l, -m, cos(theta)));
//    }
//
//    public static void main(String[] args) {
//        int f = Factorial(-4);
//        double v = K(0, 0);
//        System.out.println(f);
//    }
//
//
//    void Evaluate(List<Vertex> vertices)
//    {
//        int n = (degree_ + 1)*(degree_ + 1);
//        coefs = new vec3[n];
//
//
//        //对球面上的所有采样点进行积分
//        for (Vertex v : vertices)
//        {
//            double[] Y = Basis(v.theta,v.phi);
//            for (int i = 0; i < n; i++)
//            {
//                //v.color是我们从原函数f(s)中采样到的颜色
//                coefs[i] = coefs[i] + Y[i] * v.color;
//            }
//        }
//        for (vec3 coef : coefs)
//        {
//            coef = 4.0*PI*coef / (double) vertices.size();
//        }
//    }
//
//
//    vec3 Render(double theta, double phi)
//    {
//        int n = (degree_ + 1)*(degree_ + 1);
//
//        double[] Y = Basis(theta,phi);
//        vec3 color= new vec3();
//        for (int i = 0; i < n; i++)
//        {
//            color = color + Y[i] * coefs[i];
//        }
//        return color;
//    }
//
//    double[] Basis(double theta,double phi)
//    {
//        int n = (degree_ + 1)*(degree_ + 1);
//        double[] Y = new double[n];
//
//        for(int l=0 ; l<=degree_; l++){
//            for(int m = -1 * l; m <= l; m++){
//                //利用索引:n=l(l+1)+m
//                Y[l*(l+1)+m] = y(l, m, theta, phi);
//            }
//        }
//        return Y;
//    }
//}
