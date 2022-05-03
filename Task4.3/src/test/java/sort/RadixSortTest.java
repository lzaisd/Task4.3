package sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RadixSortTest {
    @Test
    void sortTest1(){
        int[] input = {2, 111, 12, 5};
        int[] answer = {2, 5, 12, 111};
        sort.RadixSort.sort(input, 10);
        assertArrayEquals(answer, input);
    }
    @Test
    void sortTest2(){
        int[] input = {1};
        int[] answer = {1};
        sort.RadixSort.sort(input, 10);
        assertArrayEquals(answer, input);
    }
    @Test
    void sortTest3(){
        int[] input = {2, 2, 2, 2};
        int[] answer = {2, 2, 2, 2};
        sort.RadixSort.sort(input, 10);
        assertArrayEquals(answer, input);
    }
    @Test
    void sortTest4(){
        int[] input = {5, 4, 3, 2};
        int[] answer = {2, 3, 4, 5};
        sort.RadixSort.sort(input, 10);
        assertArrayEquals(answer, input);
    }
}