package by.epam.tc;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] matrixOne = new int[1000][1000];
        int[][] matrixTwo = new int[1000][1000];
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                matrixOne[i][j] = i + 1;
                matrixTwo[i][j] = i + 1;
            }
        }
        long start = System.currentTimeMillis();
        try {
            MatrixMultiplicator matrixMultiplicator = new MatrixMultiplicator(matrixOne, matrixTwo, 200);
            int[][] result = matrixMultiplicator.multiplyMatrices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long finish = System.currentTimeMillis();
        System.out.println(finish - start);
    }
}

