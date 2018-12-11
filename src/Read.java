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
        System.out.println("getSpam: "+ getSpamPropability());
        System.out.println("getHam: "+ getHamPropability());
        System.out.print("\n");
        getPropability();
    }
    private void getPropability()
    {
        int max1 =0;
        int max2 =0;
        int thesi1=0;
        int thesi2=0;
        int mail1=0;
        int mail2=0;
        for (int i=0; i< MailArray.size(); i++)
         {
             if (MailArray.get(i)[max+1]==1)
             {

                 for(int j=0; j< WordArray.length; j++)
                 {
                     if(max1<MailArray.get(i)[j])
                     {
                         max1=MailArray.get(i)[j];
                         thesi1=j;
                         mail1=i;
                     }
                 }
             }
             else
             {
                 for(int j=0; j< WordArray.length; j++)
                 {
                     if(max2<MailArray.get(i)[j])
                     {
                         max2=MailArray.get(i)[j];
                         thesi2=j;
                         mail2=i;
                     }
                 }
             }
         }
        System.out.println("spam max: "+ max1+" thesi " + thesi1+" mail1: " +mail1);
        System.out.println("legit max: "+max2+" thesi " + thesi2+" mail2: " +mail2);
        System.out.println("thesi: "+ MailArray.get(mail1)[thesi1]);
        System.out.println("thesi2: "+ MailArray.get(mail2)[thesi2]);

        double bill=0;
        //return bill;
    }
    private double getSpamPropability()
    {
        double count =0;
        for (int i=0;i<MailArray.size();i++)
        {
            if(MailArray.get(i)[max+1]==1)
            {
                count++;
            }
        }
        return count/MailArray.size();
    }
    private double getHamPropability()
    {
        double count =0;
        for (int i=0;i<MailArray.size();i++)
        {
            if(MailArray.get(i)[max+1]==0)
            {
                count++;
            }
        }
        return count/MailArray.size();
    }


    private  void inputToArray(String path)throws  FileNotFoundException
    {
        int ArrayCounter=0;
        //TrainingParts max = 8
        for (int i = 1; i <= TrainingParts; i++) {
            /** path:src\pu_corpora_public\pu1\part
             * **/
            String localPath = path + Integer.toString(i);
            //makes an new object named dir
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                //
                WordArray = new int[max+2];
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if(!word.equals("Subject:"))
                    {
                        int thesi=Integer.parseInt(word);
                        WordArray[thesi]++;
                    }
                    if(ArrayCounter == 533)
                    {
                        System.out.println("file: "+ file.toString());
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
