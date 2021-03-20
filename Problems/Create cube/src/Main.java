class ArrayOperations {
    public static int[][][] createCube() {
        int[][][] cube = new int[3][3][3];
        int number = 0;
        for(int i = 0; i < cube.length; i ++) {
            for(int j = 0; j < cube[i].length; j ++){
                for(int u = 0; u < cube[j].length; u++){
                    cube[i][j][u] = number;
                    number++;
                }

            }
            number = 0;
        }
        return cube;
    }
}