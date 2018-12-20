import java.util.*;
class Sort2Table {
    public void bubbleSort(double[][] arr) {
        int n = arr.length;
        double temp = 0.0;
        double temp1 = 0.0;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if(arr[j-1][1] < arr[j][1]){
                    //swap elements
                    temp = arr[j-1][1];
                    arr[j-1][1] = arr[j][1];
                    arr[j][1] = temp;

                    temp1 = arr[j-1][0];
                    arr[j-1][0] = arr[j][0];
                    arr[j][0] = temp1;
                }

            }
        }

    }
}