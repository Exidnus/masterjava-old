package ru.javaops.masterjava.matrix;

import java.util.concurrent.RecursiveAction;

/**
 * Created by DVarygin on 12.07.2016.
 */
public class MyFork extends RecursiveAction {

    public static int[][] result = new int[MainMatrix.MATRIX_SIZE][MainMatrix.MATRIX_SIZE];

    private int[][] big1;

    private int[][] big2;

    public MyFork(int[][] big1, int[][] big2) {
        this.big1 = big1;
        this.big2 = big2;
    }

    @Override
    protected void compute() {
        if (big1.length == 2) {
            multiplyWithStrassenAlgorithm(big1);
        } else {
            Four four1 = getFourMatrix(big1);
            Four four2 = getFourMatrix(big2);

            invokeAll(new MyFork(four1.matrix1, four2.matrix1), new MyFork(four1.matrix2, four2.matrix2),
                    new MyFork(four1.matrix3, four2.matrix3), new MyFork(four1.matrix4, four2.matrix4));
        }
    }

    private void multiplyWithStrassenAlgorithm(int[][] matrix) {

    }

    private Four getFourMatrix(int[][] big) {
        int newSize = big1.length >> 1;
        int[][] matrix1 = new int[newSize][newSize];
        int[][] matrix2 = new int[newSize][newSize];
        int[][] matrix3 = new int[newSize][newSize];
        int[][] matrix4 = new int[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            System.arraycopy(big1[i], 0, matrix1[i], 0, newSize);
        }
        for (int i = newSize; i < big1.length; i++) {
            System.arraycopy(big1[i], 0, matrix2[i], 0, newSize);
        }
        for (int i = 0; i < newSize; i++) {
            System.arraycopy(big1[i], newSize, matrix3[i], newSize, big1.length - newSize);
        }
        for (int i = newSize; i < big1.length; i++) {
            System.arraycopy(big1[i], newSize, matrix4[i], newSize, big1.length - newSize);
        }
        return new Four(matrix1, matrix2, matrix3, matrix4);
    }

    private static class Four {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int[][] matrix3;
        private final int[][] matrix4;

        private Four(int[][] matrix1, int[][] matrix2, int[][] matrix3, int[][] matrix4) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.matrix3 = matrix3;
            this.matrix4 = matrix4;
        }
    }
}
