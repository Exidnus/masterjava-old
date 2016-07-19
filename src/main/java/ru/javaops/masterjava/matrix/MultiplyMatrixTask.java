package ru.javaops.masterjava.matrix;

import java.util.concurrent.RecursiveTask;

/**
 * Created by DVarygin on 12.07.2016.
 */
public class MultiplyMatrixTask extends RecursiveTask<int[][]> {

    private static final int BORDER = 2;

    private final int[][] source1;
    private final int[][] source2;

    public MultiplyMatrixTask(int[][] source1, int[][] source2) {
        this.source1 = source1;
        this.source2 = source2;
    }

    @Override
    protected int[][] compute() {
        if (source1.length <= BORDER) {
            return multiplyMatrixsStandard(source1, source2);
        } else {
            Four four1 = getFourFromMatrix(source1);
            Four four2 = getFourFromMatrix(source2);
            return multiplyFours(four1, four2);
        }
    }

    private int[][] multiplyFours(Four four1, Four four2) {

        int[][] A11 = four1.matrix1;
        int[][] A12 = four1.matrix2;
        int[][] A21 = four1.matrix3;
        int[][] A22 = four1.matrix4;

        int[][] B11 = four2.matrix1;
        int[][] B12 = four2.matrix2;
        int[][] B21 = four2.matrix3;
        int[][] B22 = four2.matrix4;

        MultiplyMatrixTask P1Multiply = new MultiplyMatrixTask(sum(A11, A22), sum(B11, B22));
        MultiplyMatrixTask P2Multiply = new MultiplyMatrixTask(sum(A21, A22), B11);
        MultiplyMatrixTask P3Multiply = new MultiplyMatrixTask(A11, subtract(B12, B22));
        MultiplyMatrixTask P4Multiply = new MultiplyMatrixTask(A22, subtract(B21, B11));
        MultiplyMatrixTask P5Multiply = new MultiplyMatrixTask(sum(A11, A12), B22);
        MultiplyMatrixTask P6Multiply = new MultiplyMatrixTask(subtract(A21, A11), sum(B11, B12));
        MultiplyMatrixTask P7Multiply = new MultiplyMatrixTask(subtract(A12, A22), sum(B21, B22));

        P7Multiply.fork();
        P6Multiply.fork();
        P5Multiply.fork();
        P4Multiply.fork();
        P3Multiply.fork();
        P2Multiply.fork();
        P1Multiply.fork();

        int[][] P1 = P1Multiply.join();
        int[][] P2 = P2Multiply.join();
        int[][] P3 = P3Multiply.join();
        int[][] P4 = P4Multiply.join();
        int[][] P5 = P5Multiply.join();
        int[][] P6 = P6Multiply.join();
        int[][] P7 = P7Multiply.join();

        int[][] C11 = subtract(sum(P1, P4), sum(P5, P7));
        int[][] C12 = sum(P3, P5);
        int[][] C21 = sum(P2, P4);
        int[][] C22 = sum(subtract(P1, P2), sum(P3, P6));

        return getMatrixFromFourMatrix(C11, C12, C21, C22);
    }

    private int[][] multiplyMatrixsStandard(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                int sum = 0;
                for (int k = 0; k < matrix1.length; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    private int[][] sum(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    private int[][] subtract(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return result;
    }

    private Four getFourFromMatrix(int[][] big) {
        int newSize = big.length >> 1;
        int oldSize = big.length;
        int[][] matrix1 = new int[newSize][newSize];
        int[][] matrix2 = new int[newSize][newSize];
        int[][] matrix3 = new int[newSize][newSize];
        int[][] matrix4 = new int[newSize][newSize];
        for (int x = 0; x < newSize; x++) {
            for (int y = 0; y < newSize; y++) {
                matrix1[x][y] = big[x][y];
            }
        }
        for (int x = newSize; x < oldSize; x++) {
            for (int y = 0; y < newSize; y++) {
                matrix2[x - newSize][y] = big[x][y];
            }
        }
        for (int x = 0; x < newSize; x++) {
            for (int y = newSize; y < oldSize; y++) {
                matrix3[x][y - newSize] = big[x][y];
            }
        }
        for (int x = newSize; x < oldSize; x++) {
            for (int y = newSize; y < oldSize; y++) {
                matrix4[x - newSize][y - newSize] = big[x][y];
            }
        }
        return new Four(matrix1, matrix2, matrix3, matrix4);
    }

    private int[][] getMatrixFromFourMatrix(int[][] m1, int[][] m2, int[][] m3, int[][] m4) {
        int newSize = m1.length << 1;
        int oldSize = m1.length;
        int[][] result = new int[newSize][newSize];

        for (int x = 0; x < oldSize; x++) {
            for (int y = 0; y < oldSize; y++) {
                result[x][y] = m1[x][y];
            }
        }
        for (int x = oldSize; x < newSize; x++) {
            for (int y = 0; y < oldSize; y++) {
                result[x][y] = m2[x - oldSize][y];
            }
        }
        for (int x = 0; x < oldSize; x++) {
            for (int y  = oldSize; y < newSize; y++) {
                result[x][y] = m3[x][y - oldSize];
            }
        }
        for (int x = oldSize; x < newSize; x++) {
            for (int y = oldSize; y < newSize; y++) {
                result[x][y] = m4[x - oldSize][y - oldSize];
            }
        }
        return result;
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
