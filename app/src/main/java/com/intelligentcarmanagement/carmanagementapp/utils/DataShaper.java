package com.intelligentcarmanagement.carmanagementapp.utils;

public class DataShaper {
    private static final int N_FEATURES = 6;
    private static final int N_LENGTH = 5;
    private static final int N_ROWS = 1;
    private static final int N_COLUMNS = 4;

    // Taking as input a list of samples, each sample with 6 features
    public static float[][][][] shapeFeatures(float[][] features) {
        float[][][][] shapedData = new float[N_COLUMNS][N_ROWS][N_LENGTH][N_FEATURES];

        int index = 0;
        for(int j = 0; j < N_COLUMNS; j++) {
            for(int k = 0; k < N_ROWS; k++) {
                for(int m = 0; m < N_LENGTH; m++) {
                    shapedData[j][k][m] = features[index];
                    index++;
                }
            }
        }

        return shapedData;
    }
}
