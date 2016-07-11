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
        final int[][] standard = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
        System.out.println("Single thread multiplication time, sec: " + (System.currentTimeMillis() - start) / 1000.);

        start = System.currentTimeMillis();
        final int[][] resultOfMultiplyingWithExecutorService = MatrixUtil.multiplyWithExecutorService(matrixA, matrixB);
        System.out.println("Multi thread multiplication time, sec: " + (System.currentTimeMillis() - start) / 1000.);
        System.out.println(MatrixUtil.checkOnEqualsMatrixs(standard, resultOfMultiplyingWithExecutorService) ?
                "Multi thread multiplication with ExecutorService executed successfully" : "Multi thread multiplication with ExecutorService wasn't successful!");

        start = System.currentTimeMillis();
        final int[][] resultOfMultiplyingWithOnlyJava14 = MatrixUtil.multiplyWithOnlyJava14(matrixA, matrixB);
        System.out.println("Multi thread multiplication in Java 1.4 time, sec: " + (System.currentTimeMillis() - start) / 1000.);
        System.out.println(MatrixUtil.checkOnEqualsMatrixs(standard, resultOfMultiplyingWithOnlyJava14) ?
                "Multi thread multiplication with in Java 1.4 executed successfully" : "Multi thread multiplication in Java 1.4 successful!");

        start = System.currentTimeMillis();
        final int[][] resultOfMultiplyingWithForkJoin = MatrixUtil.multiplyWitnForkJoin(matrixA, matrixB);
        System.out.println("Mult thread multiplication wit Fork/Join time, sec: " + (System.currentTimeMillis() - start) / 1000);
        System.out.println(MatrixUtil.checkOnEqualsMatrixs(standard, resultOfMultiplyingWithForkJoin) ?
        "Multi thread multiplication with Fork/Join executed successfully" : "Multi thread multiplication with Fork/Join wasn't successful!");
        // TODO implement parallel multiplication matrixA*matrixB
        // TODO compare wih standard;
    }
}
