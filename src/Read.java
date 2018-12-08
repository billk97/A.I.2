import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Read {
    //contains in each column an integer table with the number of times the words occur
    private ArrayList<int[]> MailArray =  new ArrayList<int[]>();
    private int TrainingParts=8;
    private int[] WordArray;
    private int max;

    public void ReadFile() throws FileNotFoundException {

        String path = "src\\pu_corpora_public\\pu1\\part";
        max = getMax(path);
        /***The array that contains the time that its word is used
        +1 in order to use all the words
         **/
        //WordArray=new int[max+2];
        inputToArray(path);
        System.out.println("Max length: " +MailArray.size());
        for(int i=0; i<WordArray.length; i++)
        {
            System.out.println("i= "+i+ " MailArray: " + MailArray.get(4)[i]);
        }

    }

    private  void inputToArray(String path)throws  FileNotFoundException
    {
        int ArrayCounter=0;
        for (int i = 1; i <= TrainingParts; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                WordArray = new int[max+2];
                System.out.println("file Name: "+ file.toString());
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if(!word.equals("Subject:"))
                    {
                        int thesi=Integer.parseInt(word);
                        WordArray[thesi]++;
                    }
                }//endwhile
                //if spam then puts 1 in the end
                if(checkSpam(file)){
                    WordArray[max+1]++;
                }
                MailArray.add(ArrayCounter,WordArray);
                ArrayCounter++;
                scanner.close();
            }//end for file
        }//end for part number
    }
    /**check if the given file is spam or ham**/
    private  boolean checkSpam(File dir) throws FileNotFoundException
    {
        if(dir.getName().contains("spmsg"))
        {
            return true;
        }
        return false;
    }
    private  int getMax(String path) throws FileNotFoundException {
        int maxNumber = 0;
        for (int i = 1; i <= TrainingParts; i++) {
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

}
