package com.lph.seckill;

import java.util.Scanner;

public class test1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] numbers = new int[n];
        int res1 = 0;
        int res2 = 0;
        for(int i = 0; i < n; i++){
            numbers[i] = sc.nextInt();
        }
        int[] maxArray = new int[n];
        int i = 0;
        for(; i < n ; i++){
            if(i <= 1){
                maxArray[i] = numbers[i];
            }
            else if(i == 2){
                maxArray[i] = numbers[i]+numbers[0];
            }
            else {
                maxArray[i] = numbers[i]+Math.max(maxArray[i-2],maxArray[i-3]);
            }
        }
        System.out.println(Math.max(maxArray[i-1],maxArray[i-2]));
    }
}
