import java.io.*;
import java.util.*;

public class LogisticRegression{
    public HashMap<Integer, Integer[]> MailHash = new HashMap<Integer, Integer[]>();//contains the vocabulary
    public HashSet<Integer> tempSet; //contains the vocabulary of each mail at the time
    public double[][] words; //[lexi,thesi,ig] just like a pointer table
    public int[][] MainTable;//contains all the mails in a vector form <0,1,0,1,0,1,0,1,0,0>
    public String path = "src\\pu_corpora_public\\pu1\\part";
    private int MailCounter;
    public int TrainingDataNumber = 8;
    public double SpamCounter ;
    public double HamCounter ;
    public int epoxes=5;
    public double htta=0.001;

    public double[]  TrueFalse ;/**TrueFalse==TP,TN,FP,FN**/

    public String getPath() {
        return path;
    }

    public int getMailCounter() {
        return MailCounter;
    }

    public void setMailCounter(int mailCounter) {
        MailCounter = mailCounter;
    }

    public int getTrainingDataNumber() {
        return TrainingDataNumber;
    }

    public void setTrainingDataNumber(int trainingDataNumber) {
        TrainingDataNumber = trainingDataNumber;
    }

    public double getSpamCounter() {
        return SpamCounter;
    }

    public void setSpamCounter(double spamCounter) {
        SpamCounter = spamCounter;
    }

    public double getHamCounter() {
        return HamCounter;
    }

    public void setHamCounter(double hamCounter) {
        HamCounter = hamCounter;
    }

    public void initializeData() throws FileNotFoundException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("LogisticRegration.txt"), "utf-8"));
            writer.write("MailCounter  Accuracy  HamPrecision  HamRecall  SpamPrecision  SpamRecall");
            ((BufferedWriter) writer).newLine();
            String localPath = path;
            for (int i = 1; i <= TrainingDataNumber; i++)
            {
                localPath = path+ Integer.toString(i);
                System.out.println("i: " +i + " ====================================================");
                setMailCounter(0);
                setSpamCounter(0);
                setHamCounter(0);
                TrueFalse = new double[]{0.0, 0.0, 0.0, 0.0};
                inputToHashMap(path, i);
                Pruning();
                HashWordToTable();
                Read(MailCounter,i,path);
                System.out.println("maintable.length: Mails "+MainTable.length+" Maintable[0].length: Vocabulary "+MainTable[0].length);
                //PrintMainTable();
      /*  System.out.println("sigmoid(5): "+Sigmoid(5));
        System.out.println("sigmoid(2)+sigmoid(3): "+(Sigmoid(3)+Sigmoid(2)));*/
                regression();
                CalculateTest(path);
                writer.write((getSpamCounter() + getHamCounter())
                        + " " + Double.toString(Accuracy()).replace(".", ",")
                        + " " + Double.toString(HamPrecision()).replace(".", ",")
                        + " " + Double.toString(HamRecall()).replace(".", ",")
                        + " " + Double.toString(SpamPrecision()).replace(".", ",")
                        + " " + Double.toString(SpamRecall()).replace(".", ","));
                ((BufferedWriter) writer).newLine();
                System.out.println("==================");

            }

        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }

    }

    public double Accuracy() {
        return ((TrueFalse[0] + TrueFalse[1]) / (TrueFalse[0] + TrueFalse[1] + TrueFalse[2] + TrueFalse[3])) * 100;
    }

    public double HamPrecision() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[2])) * 100;
    }

    public double SpamPrecision() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[3])) * 100;
    }

    public double HamRecall() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[3])) * 100;
    }

    public double SpamRecall() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[2])) * 100;
    }

    /**
     * this function creates the vocabulary
     **/
    public void inputToHashMap(String path, int TrainingDataNumber) throws FileNotFoundException {
        MailCounter = 0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= TrainingDataNumber; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
                ReadMail(file);
                UpdateMailHash();
            }//end for2
        }//end for1

    }//end input To HashMap

    /**
     * reduces the vocabulary keeps the most important words
     **/
    private void Pruning() {
        Iterator<Integer> it = MailHash.keySet().iterator();
        while (it.hasNext()) {
            int onoma = it.next();
            if (MailHash.get(onoma)[0] < 30 || MailHash.get(onoma)[0] > 2000) {
                it.remove();
            }
        }//end while
    }

    /**
     * reads a single mail and saves it to a temporary hash set
     **/
    public void ReadMail(File file) throws FileNotFoundException {
        tempSet = new HashSet<Integer>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (!word.equals("Subject:")) {
                int cell = Integer.parseInt(word);
                tempSet.add(cell);
            }
        }//end while
        scanner.close();
    }//end ReadMail

    /**this function Updates the MailHash HashMap
     * and increases the counter when an word is found in a email
     * loops all the tempSet which contains
     * unique word
     **/
    /**checks if the (word)=k that is contained in the mail
     * exists in the known words or dictionary
     * and ads an counter which means that the word is found
     * at least 1 time**/
    /**
     * if (word)=k does not exists it create on
     * which contains 1 table 1 column for the number of spam
     * 1 for the number of ham
     **/
    private void UpdateMailHash() {
        for (int k : tempSet) {
            if (MailHash.containsKey(k)) {
                MailHash.get(k)[0] += 1;
            } else {
                MailHash.put(k, new Integer[]{1});
            }
        }//end for
    }//end UpdateMailHash

    /**
     * words[][0]=leksi, words[][1]=i, words[][2]= weights
     **/
    public void HashWordToTable() {
        int count = 0;
        words = new double[MailHash.size()][3];
        for (int lexi : MailHash.keySet()) {
            words[count][0] = lexi;
            words[count][1] = count;
            words[count][2] = 0.0;
            count++;

        }
        System.out.println("Mailhash: " + MailHash.size() + " count: " + count + " size of table " + words.length + " size of column " + words[0].length);

    }//end HashWordToTable


    /**
     * this function is responsible for inputting the all the mails in a single table
     * example
     * <0,0,0,0,1,1,1,0>
     * <0,0,0,0,1,1,1,0>
     **/
    public void Read(int TotalMails, int posost, String path) throws FileNotFoundException {
        System.out.println("Total Mails: " + TotalMails + " Words.length " + words.length);
        SpamCounter=0.0;
        HamCounter=0.0;
        MainTable = new int[TotalMails][words.length];
        HashSet<Integer> MailWords;
        int MailCounter = 0;
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
                        int lexi = Integer.parseInt(word);
                        MailWords.add(lexi);
                    }//end if
                }//end while
                scanner.close();
                if (checkSpam(file)) {
                    SpamCounter++;
                    InputToMainTable(MailCounter, MailWords, 0);
                } else {
                    HamCounter++;
                    InputToMainTable(MailCounter, MailWords, 1);
                }
                MailCounter++;
            }//end for2
        }//end for1
    }

    /**
     * this function inputs the data from the Hash Table to the 2 dimensional table
     **/
    public void InputToMainTable(int Mails, HashSet<Integer> TempMailWords, int SpamORham) {
        for (int i = 0; i < words.length; i++) {
            if (TempMailWords.contains((int) words[i][0])) {
                MainTable[Mails][i] = 1;
            }
        }
        MainTable[Mails][words.length - 1] = SpamORham;

    }

    /**
     * checks if the given file is spam or ham based on the Title
     **/
    public boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam

    /**
     * this function prints the main table
     **/
    private void PrintMainTable() {
        int spam=0;
        int ham=0;

        for (int i = 0; i < MainTable.length; i++) {
            for (int j = 0; j < MainTable[0].length; j++) {
                System.out.print(MainTable[i][j] + " ");
            }
            System.out.println("");

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //READ TELOS

    private double Sigmoid(double x,double w){
        double ginomeno=w*x;
        return 1.0/(1.0+ Math.exp(-ginomeno));
    }

    private double Sigmoid(double x){
        double ginomeno=x;
        return 1.0/(1.0+ Math.exp(-ginomeno));
    }


    private void regression(){
        for(int l=0;l<epoxes;l++){
            double likelihood=0.0;
            for(int i=0;i<MainTable.length;i++){
                int y=MainTable[i][MainTable[0].length-1];
                int [] temp= fillTempTable(i);
                for(int j=0;j<words.length;j++){
                   // System.out.println("class "+MailClassify(temp));
                    words[j][2]+=htta*(y-MailClassify(temp))*MainTable[i][j];//kanonas enhmerwshs varwn
                }
                //System.out.println("class: "+MailClassify(temp));
                likelihood=(y*Math.log(MailClassify(temp))+(1.0-y)*(Math.log(1.0-MailClassify(temp))));
                //System.out.println("likelihood: "+likelihood);
              //  System.out.println(("i:" +i+" weight[1] "+ Arrays.toString(words[1])));


            }//end for mails

            System.out.println("likelihood: "+likelihood+" i: "+l);

        }//end for epoxes

    }

    private int[] fillTempTable(int thesi){
        int[] temp=new int[words.length];
        for(int i=0; i<words.length;i++){
            temp[i]=MainTable[thesi][i];
        }
        return temp;
    }

    //Calculate total probality for all x
    private double MailClassify(int temp[]){
        double result=0.0;
        for(int i=0;i<words.length;i++){
            result+= temp[i]*words[i][2];
        }
        return Sigmoid(result);
    }

    /**this function loads and checks how the good the algorithm does in unknown data**/
    public void CalculateTest(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        int Spam = 0;
        int Ham = 0;
        int MailCouonter=0;
        for (File file : dir.listFiles()) {
            MailCouonter++;
            tempSet = new HashSet<Integer>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (!word.equals("Subject:")) {
                    int cell = Integer.parseInt(word);
                    tempSet.add(cell);
                }
            }//end while

            int temp[]=new int[words.length];
            for(int i=0;i<words.length;i++){
                if(tempSet.contains((int)words[i][0])){
                    temp[i]=1;
                }else{
                    temp[i]=0;
                }
            }//end for
       //     System.out.println(Arrays.toString(temp));
            System.out.println("file name:"+file);
            if(MailClassify(temp)>1.0-MailClassify(temp)){
                System.out.println(" ham propability "+MailClassify(temp));
                System.out.println(" HAM");
                if(!checkSpam(file)){
                    TrueFalse[0]++;
                    Ham++;
                }
                else
                    {
                        TrueFalse[2]++;
                    }
            }else{
                System.out.println(" spam propability "+MailClassify(temp));
                System.out.println(" SPAM");
                if(checkSpam(file)){
                    Spam++;
                    TrueFalse[1]++;
                }
                else
                    {
                        TrueFalse[3]++;
                    }
            }
        }//end for
        System.out.println(" ACCURACY: "+(((double)Spam+(double) Ham)/(double) MailCouonter)*100);

    }
}
