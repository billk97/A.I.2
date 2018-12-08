import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Read {
    private static int[] WordArray;
    public void ReadFile() throws FileNotFoundException {
        String path = "src\\pu_corpora_public\\pu1\\part";
        int max = getMax(path);
        WordArray=new int[max+1];
        Arrays.fill(WordArray,0);
        inputToArray(path);
        int maxl = 0;
        int thesi =0;
        int totalNumberOfWords=0;
        for(int i=0; i<WordArray.length ;i++)
        {
            if(WordArray[i]>=maxl)
            {
                maxl=WordArray[i];
                thesi=i;
            }
            totalNumberOfWords=totalNumberOfWords+WordArray[i];
        }
        System.out.println("the dictionary is: "+WordArray.length);
        System.out.println("total number of words is: "+ totalNumberOfWords);
        System.out.println("The number of spam messages is: "+ getSpam(path));
        System.out.println("The number of Legit messages is: "+ getLegit(path));
        System.out.println("The most used Word is: "+ thesi+" and is used: "+maxl+" times");

    }
    private static int getSpam(String path) throws FileNotFoundException
    {
        int SpamCount=0;
        for (int i = 1; i <= 10; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                if(file.getName().contains("spmsg"))
                {
                    SpamCount++;
                }
            }//end for file
        }//end for part number
        return SpamCount;
    }
    private static int getLegit(String path) throws FileNotFoundException
    {
        int LegitCount=0;
        for (int i = 1; i <= 10; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                if(file.getName().contains("legit"))
                {
                    LegitCount++;
                }
            }//end for file
        }//end for part number
        return LegitCount;
    }

    private static int getMax(String path) throws FileNotFoundException {
        int maxNumber = 0;
        for (int i = 1; i <= 10; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (word.equals("Subject:")) {
                        //System.out.println("Subject");
                    } else {
                        if (maxNumber < Integer.parseInt(word)) {
                            maxNumber = Integer.parseInt(word);
                        }
                    }
                }//endwhile
                scanner.close();
            }//end for file
        }//end for part number
        return maxNumber;
    }//end getMaX
    private static void inputToArray(String path)throws  FileNotFoundException
    {
        for (int i = 1; i <= 10; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if(!word.equals("Subject:"))
                    {
                        int thesi=Integer.parseInt(word);
                        WordArray[thesi]++;
                    }
                }//endwhile
                scanner.close();
            }//end for file
        }//end for part number
    }
}
