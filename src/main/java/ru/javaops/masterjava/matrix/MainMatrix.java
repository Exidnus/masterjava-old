package ru.javaops.masterjava.matrix;

/**
 * gkislin
 * 03.07.2016
 */
public class MainMatrix {
    // Multiplex matrix
    static final int MATRIX_SIZE = 1000;
    static final int THREAD_NUMBER = 10;

    public static void main(String[] args) {
        final int[][] matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
        final int[][] matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
        MatrixUtil.fillMatrixWithRandomNumbers(matrixA);
        MatrixUtil.fillMatrixWithRandomNumbers(matrixB);

        long start = System.currentTimeMillis();
        final int[][] matrixC = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
        System.out.println("Single thread multiplication time, sec: " + (System.currentTimeMillis() - start) / 1000.);

        start = System.currentTimeMillis();
        final int[][] matrixD = MatrixUtil.multiplyWithExecutorService(matrixA, matrixB);
        System.out.println("Multi thread multiplication time, sec: " + (System.currentTimeMillis() - start) / 1000.);
        System.out.println(MatrixUtil.checkOnEqualsMatrixs(matrixC, matrixD) ?
                "matrixC and matrixD are equals" : "matrixC and matrixD are not equals");

        start = System.currentTimeMillis();
        final int[][] matrixE = MatrixUtil.multiplyWithOnlyJava14(matrixA, matrixB);
        System.out.println("Multi thread multiplication in Java 1.4 time, sec: " + (System.currentTimeMillis() - start) / 1000.);
        System.out.println(MatrixUtil.checkOnEqualsMatrixs(matrixC, matrixE) ?
                "matrixC and matrixE are equals" : "matrixC and matrixE are not equals");

        // TODO implement parallel multiplication matrixA*matrixB
        // TODO compare wih matrixC;
    }
}
