import javax.naming.ldap.PagedResultsControl;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class ReadB {
    private HashMap<Integer, Double[]> MailHash;
    private HashSet<Integer> tempSet;
    private int SpamCounter;
    private int HammCounter;
    private double TrueFalse[] = {0.0, 0.0, 0.0, 0.0};

    private void inputToHashMap(String path, int posost) throws FileNotFoundException {
        MailHash = new HashMap<Integer, Double[]>();
        SpamCounter = 0;
        HammCounter = 0;
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            for (File file : dir.listFiles()) {
                if (checkSpam(file)) {
                    SpamCounter++;
                } else {
                    HammCounter++;
                }
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (!word.equals("Subject:")) {
                        int cell = Integer.parseInt(word);
                        tempSet.add(cell);
                    }
                }//end while
                if (checkSpam(file)) {
                    for (int k : tempSet) {
                        if (MailHash.containsKey(k)) {

                            MailHash.get(k)[0] += 1;

                        } else {
                            MailHash.put(k, new Double[]{0.0, 0.0});
                            MailHash.get(k)[0] += 1;
                        }
                    }//end for
                } else {
                    for (int k : tempSet) {
                        if (MailHash.containsKey(k)) {
                            MailHash.get(k)[1] += 1;
                        } else {
                            MailHash.put(k, new Double[]{0.0, 0.0});
                            MailHash.get(k)[1] += 1;
                        }
                    }//end for
                }
                scanner.close();
                // tempSet.clear();
            }

        }
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0]++;
            MailHash.get(lexi)[1]++;
        }
        SpamCounter += 2;
        HammCounter += 2;
        Propability();
        System.out.println("SpamCounter: " + SpamCounter + " HammCounter: " + HammCounter);
        System.out.println("Counter: " + (SpamCounter + HammCounter));
    }

    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }

    private void Propability() {
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0] = Math.log(1.0 + (MailHash.get(lexi)[0] / (double) SpamCounter));
            MailHash.get(lexi)[1] = Math.log(1.0 + (MailHash.get(lexi)[1] / (double) HammCounter));
        }
    }

    public void ReadMail() throws FileNotFoundException {
        String path = "src\\pu_corpora_public\\pu1\\part";
        inputToHashMap(path, 1);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 2);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 3);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 4);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 5);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 6);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 7);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 8);
        CalculateTest(path);
        System.out.println("==================");
        inputToHashMap(path, 9);
        CalculateTest(path);
        System.out.println("==================");
    }

    public double getSpamPropability() {
        return (double) SpamCounter / ((double) SpamCounter + (double) HammCounter);
    }

    public double getHamPropability() {
        return 1.0 - getSpamPropability();
    }

    public void CalculateTest(String path) throws FileNotFoundException {
        TrueFalse[0] = 0.0;
        TrueFalse[1] = 0.0;
        TrueFalse[2] = 0.0;
        TrueFalse[3] = 0.0;
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        for (File file : dir.listFiles()) {
            tempSet = new HashSet<Integer>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (!word.equals("Subject:")) {
                    int cell = Integer.parseInt(word);
                    tempSet.add(cell);
                }
            }//end while
            double SpamPropability = 1.0;
            double HamPropability = 1.0;


            for (int word : tempSet) {
                if (MailHash.containsKey(word)) {

                    SpamPropability = SpamPropability + MailHash.get(word)[0];
                    HamPropability = HamPropability + MailHash.get(word)[1];


                } else {
                    SpamPropability = SpamPropability + Math.log(1.0 + (1.0 / (double) SpamCounter));
                    HamPropability = HamPropability + Math.log(1.0 + (1.0 / (double) HammCounter));

                }
                //  System.out.println("Word: " + word + " spam " + SpamPropability + " ham " + HamPropability);

            }

            if (SpamPropability * getSpamPropability() < HamPropability * getHamPropability()) {
                //System.out.println("Its a Ham eimai sto file " + file.toString());
                /**TrueFalse==TP,TN,FP,FN**/
                if (!checkSpam(file)) {
                    TrueFalse[0]++;
                } else {
                    TrueFalse[2]++;
                }
            } else {
                //System.out.println("Its a Spam eimai sto file " + file.toString());
                if (checkSpam(file)) {
                    TrueFalse[1]++;
                } else {
                    TrueFalse[3]++;
                }
            }
        }//end for
        System.out.println("Accuracy: " + Accuracy());
        System.out.println(getSpamPropability());
        System.out.println(getHamPropability());
        System.out.println("HamPRecision: " + HamPrecision());
        System.out.println("SpamPRecision: " + SpamPrecision());
        System.out.println("HamRecall: " + HamRecall());
        System.out.println("SpamRecall: " + SpamRecall());
    }

    private double Accuracy() {
        return ((TrueFalse[0] + TrueFalse[1]) / (TrueFalse[0] + TrueFalse[1] + TrueFalse[2] + TrueFalse[3])) * 100;
    }

    private double HamPrecision() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[2])) * 100;
    }

    private double SpamPrecision() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[3])) * 100;
    }

    private double HamRecall() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[3])) * 100;
    }

    private double SpamRecall() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[2])) * 100;
    }
}

