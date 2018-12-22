import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Id3Data {
    public HashMap<Integer, Integer[]> MailHash;
    public HashSet<Integer> tempSet;
    private  int [] words ;
    public int [][] MainTable;
    private String path = "src\\pu_corpora_public\\pu3\\part";
    private int MailCounter;
    private  int TrainingDataNumber=8;
    private int SpamCounter=0;
    private int HamCounter=0;
    /**
     * this function inputs all the data from the txt
     * into a HashMap
     **/
    public  void Initializer() throws FileNotFoundException {
        inputToHashMap(path,TrainingDataNumber);
        HashWordToTable();
        ReadMail(MailCounter,TrainingDataNumber,path);
        PrintMainTable();
        System.out.println("SpamCounter: "+ SpamCounter + " HamCounter: "+ HamCounter + " = MailCounter: "+ MailCounter);
        Ckeck2DTableSH();
    }
    /**this function creates the vocabulary **/
    public void inputToHashMap(String path, int TrainingDataNumber) throws FileNotFoundException {
        MailHash = new HashMap<Integer, Integer[]>();
        MailCounter=0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= TrainingDataNumber; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        int lexi =Integer.parseInt(word);
                        tempSet.add(lexi);
                    }//end if
                }//end while
                scanner.close();
                UpdateMailHash();
            }//end for2
        }//end for1
        Iterator<Integer> it = MailHash.keySet().iterator();
        while (it.hasNext())
        {
            int onoma=it.next();
            if(MailHash.get(onoma)[0]<3||MailHash.get(onoma)[0]>2000)
            {
                it.remove();
            }
        }//end while
        System.out.println("Mailhash: "+ MailHash.size());
    }//end input To HashMap
    /**this function Updates the MailHash HashMap
     * and increases the counter when an word is found in a email **/
    private void UpdateMailHash()
    {
        /**loops all the tempSet which contains
         * unique word **/
        for (int k : tempSet) {
            /**checks if the (word)=k that is contained in the mail
             * exists in the known words or dictionary
             * and ads an counter which means that the word is found
             * at least 1 time**/
            if (MailHash.containsKey(k)) {
                MailHash.get(k)[0] += 1;
            } else {
                /**if (word)=k does not exists it create on
                 * which contains 1 table 1 column for the number of spam
                 * 1 for the number of ham**/
                MailHash.put(k, new Integer[]{1});
            }
        }//end for
    }//end UpdateMailHash
    /**this function keeps track of the words in the main table each
     * word has a unique number **/
    public void HashWordToTable()
    {
        int count=0;
        words=new int[MailHash.size()];
        for(int lexi :MailHash.keySet())
        {
            words[count]=lexi;
           // System.out.println("count: "+count+" : "+ words[count]);
            count++;
        }
        System.out.println("Mailhash: "+ MailHash.size() + " count: "+ count);
    }//end HashWordToTable

    private void ReadMail(int TotalMails,int posost,String path) throws FileNotFoundException {
        System.out.println("Total Mails: " + TotalMails +" Words.length " + words.length);
        MainTable= new int [TotalMails][words.length];
        HashSet <Integer> MailWords;
        int MailCounter=0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {

                 MailWords = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        int lexi =Integer.parseInt(word);
                        MailWords.add(lexi);
                    }//end if
                }//end while
                scanner.close();
                if(checkSpam(file))
                {
                    SpamCounter++;
                    InputToMainTable(MailCounter,MailWords,0);
                }else
                    {
                        HamCounter++;
                        InputToMainTable(MailCounter,MailWords,1);
                    }
                MailCounter++;
            }//end for2
        }//end for1
    }
    /**this function inputs the data from the Hash Table to the 2 dimensional table**/
    public void InputToMainTable(int Mails,HashSet<Integer> TempMailWords,int SpamORham)
    {
        for(int i=0; i< words.length; i++)
        {
            if(TempMailWords.contains(words[i]))
            {
                MainTable[Mails][i]=1;
            }
        }
        MainTable[Mails][words.length-1]=SpamORham;
        //System.out.println("words.length-1: " + (words.length-1));
    }
    private void PrintMainTable()
    {
        for(int i=0; i<MainTable.length ; i++)
        {
            for (int j =0; j<MainTable[i].length; j++)
            {
                System.out.print(MainTable[i][j] + " ");
            }
            System.out.println("");
        }
    }
    /**checks if the given file is spam or ham based on the Title**/
    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam
    /**in order to take the number of rows
     * MainTable.length =3304 = number of emails
     * MainTable[0].length =16693 = number of words
     * MainTable[i][MainTable[0].length-1] = 0 or 1 **/
    private int Ckeck2DTableSH()
    {
        int Spam=0;
        int Ham =0;
        for(int i =0; i<MainTable.length; i++)
        {
            if(MainTable[i][MainTable[0].length-1] == 1)
            {
                Ham++;
            }
            else
                {
                    Spam++;
                }
        }
        System.out.println("Spam: "+ Spam + " Ham: " + Ham);
        return  Spam;
    }

    public double log2(double n)
    {
        return (Math.log(n) / Math.log(2));
    }

    private double getSpamProp(){
        return SpamCounter/MailCounter;
    }

    private double getHamProp(){
        return 1-getSpamProp();
    }

    private double TotalEntropy(){
        //System.out.println("SpamProbability: "+getSpamProp()+" HamProbability: "+getHamProp() );
        //System.out.println("logSpamProbability: "+log2(getSpamProp())+" logHamProbability: "+log2(getHamProp()) );

        return -(getSpamProp()*log2(getSpamProp())+getHamProp()*log2(getHamProp()));
    }
}
