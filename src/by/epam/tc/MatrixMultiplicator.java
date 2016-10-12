package by.epam.tc;

public class MatrixMultiplicator {

    private final static int THREAD_DIVIDER = 5;

    private int threadsCount;
    private int[][] matrixOne;
    private int[][] matrixTwo;
    private int resultMatrix[][];
    private int currentRow;
    private int countEndThreads;
    private final Object objectLock = new Object();

    public MatrixMultiplicator(int[][] matrixOne, int[][] matrixTwo) {
        this.matrixOne = matrixOne;
        this.matrixTwo = matrixTwo;
        threadsCount = matrixOne.length / THREAD_DIVIDER;
    }

    public MatrixMultiplicator(int[][] matrixOne, int[][] matrixTwo, int threadsCount) {
        this(matrixOne, matrixTwo);
        this.threadsCount = threadsCount;
    }

    public int[][] multiplyMatrices() throws Exception {
        currentRow = -1;
        countEndThreads = 0;
        boolean temp = checkCompliesMatrices();
        if (!temp) {
            throw new Exception("Can not multiply matrices");
        } else {
            resultMatrix = new int[matrixOne.length][matrixOne[0].length];
            for (int i = 0; i < threadsCount; i++) {
                multiplyInThread();
            }
        }
        while (!isAllThreadsStopped());
        return resultMatrix;
    }

    private void multiplyInThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isRunning = true;
                while (isRunning) {
                    try {
                        int temp;
                        temp = threadsController();
                        multiplyRowOnColumns(temp);
                    } catch (NoMoreRowsException e) {
                        isRunning = false;
                    }
                }
                setThreadEnd();
            }
        }).start();
    }
    
    private void setThreadEnd() {
        synchronized (objectLock) {
            countEndThreads++;
        }
    }
    
    private boolean isAllThreadsStopped() {
        synchronized (objectLock) {
            return countEndThreads == threadsCount;
        }
    }

    private int threadsController() throws NoMoreRowsException {
        int result;
        synchronized (objectLock) {
            currentRow++;
            if (currentRow < matrixOne.length) {
                result = currentRow;
            } else {
                throw new NoMoreRowsException();
            }
        }
        return result;
    }

    private boolean isNeedMoreThreads() {
        boolean result;
        synchronized (objectLock) {
            result = currentRow < matrixOne.length;
        }
        return result;
    }

    private void multiplyRowOnColumns(int indexRow) {
        int length = matrixOne[indexRow].length;
        int currentSum;
        //System.out.println("Compute thread = " + Thread.currentThread().getId());
        for (int i = 0; i < length; i++) {
            currentSum = 0;
            for (int j = 0; j < length; j++) {
                currentSum += matrixOne[indexRow][j] * matrixTwo[j][i];
            }
            this.resultMatrix[indexRow][i] = currentSum;
        }
    }

    private boolean checkCompliesMatrices() {
        boolean result;
        if (matrixOne == null || matrixTwo == null) {
            return false;
        }
        result = matrixOne[0].length == matrixTwo.length;
        return result;
    }
}
