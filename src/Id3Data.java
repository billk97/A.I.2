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
    /**
     * this function inputs all the data from the txt
     * into a HashMap
     **/
    public void inputToHashMap(String path, int posost) throws FileNotFoundException {
        MailHash = new HashMap<Integer, Integer[]>();
        int MailCounter=0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= posost; i++) {
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
        HashWordToTable();
        ReadMail(MailCounter+1,posost,path);
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
    public void HashWordToTable()
    {
        int count=0;
        words=new int[MailHash.size()+1];
        for(int lexi :MailHash.keySet())
        {
            words[count]=lexi;
            System.out.println("count: "+count+" : "+ words[count]);
            count++;
        }
        System.out.println("Mailhash: "+ MailHash.size());
    }//end HashWordToTable
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
    }
    private void ReadMail(int TotalMails,int posost,String path) throws FileNotFoundException {
        MainTable= new int [TotalMails][words.length];
        HashSet <Integer> MailWords;
        int MailCounter=0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
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
                    InputToMainTable(MailCounter,MailWords,0);
                }else
                    {
                        InputToMainTable(MailCounter,MailWords,1);
                    }

            }//end for2
        }//end for1
        PrintMainTable();
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
        System.out.println("mailLength: "+ MainTable.length);
    }
    /**checks if the given file is spam or ham based on the Title**/
    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam
}
