package sort;

public class RadixSort {
    private static int getMax(int[] data) {
        int max = data[0];
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private static void countingSort(int[] data, int exp, int digitBase)
    {
        int size = data.length;

        int[] output =  new int[size];
        int[] counts = new int[digitBase];

        for (int value : data) {
            int index = (value / exp) % digitBase;
            counts[index]++;
        }

        for (int i = 1; i < digitBase; i++) {
            counts[i] += counts[i - 1];
        }

        for (int i = size - 1; i >= 0; i--)
        {
            int index = (data[i] / exp) % digitBase;
            output[counts[index] - 1] = data[i];
            counts[index]--;
        }

        System.arraycopy(output, 0, data, 0, size);
    }

    public static void sort(int[] data, int digitBase) {
        int max = getMax(data);

        for (int exp = 1; max / exp > 0; exp *= digitBase) {
            countingSort(data, exp, digitBase);
        }
    }

    public static void sort(Integer[] data, int digitBase) {
        sort(util.ArrayUtils.toPrimitive(data), digitBase);
    }
}
