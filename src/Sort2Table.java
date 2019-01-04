import java.util.*;
class Sort2Table {
    public void bubbleSort(double[][] arr, int k) {
        int n = arr.length;
        double temp = 0.0;
        double temp1 = 0.0;
        double temp2 =0.0;
        if(k==2)
        {
            for(int i=0; i < n; i++){
                for(int j=1; j < (n-i); j++){
                    if(arr[j-1][k] < arr[j][k]){
                        //swap elements
                        temp = arr[j-1][1];
                        arr[j-1][1] = arr[j][1];
                        arr[j][1] = temp;

                        temp1 = arr[j-1][0];
                        arr[j-1][0] = arr[j][0];
                        arr[j][0] = temp1;

                        temp2 = arr[j-1][2];
                        arr[j-1][2] = arr[j][2];
                        arr[j][2] = temp2;
                    }

                }
            }
        }
        else
            {
                for(int i=0; i < n; i++){
                    for(int j=1; j < (n-i); j++){
                        if(arr[j-1][k] > arr[j][k]){
                            //swap elements
                            temp = arr[j-1][1];
                            arr[j-1][1] = arr[j][1];
                            arr[j][1] = temp;

                            temp1 = arr[j-1][0];
                            arr[j-1][0] = arr[j][0];
                            arr[j][0] = temp1;

                            temp2 = arr[j-1][2];
                            arr[j-1][2] = arr[j][2];
                            arr[j][2] = temp2;
                        }

                    }
                }
            }


    }




}