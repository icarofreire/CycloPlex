package cycloplex;

import java.io.*;
import java.util.*;
import cycloplex.wordsLang;


public class CyclomaticComplexity {

    private final int deepRec = 10;

	public int check(final String fileName, final String lang) {
		int complexity = 0;

        final String[] blocks = wordsLang.langBlocks.get(lang);
		final String[] keywords = wordsLang.langWords.get(lang);
		String line = null;
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			line = br.readLine();

            int iniLine = -1;
            int fimLine = -1;
            int indB = 0;

            int conLine = 0;
			while (line != null)
			{
                conLine++;
				StringTokenizer stTokenizer = new StringTokenizer(line);
				while (stTokenizer.hasMoreTokens())
				{
					String words = stTokenizer.nextToken();
                    if(words.equals(blocks[0]) || words.indexOf(blocks[0]) != -1){
                        if(iniLine == -1) iniLine = conLine;
                        indB++;
                    }

                    for(int i=0; i<keywords.length; i++)
                    {
                        if (keywords[i].equals(words))
                        {
                            complexity++;
                        }
                    }

                    if(words.equals(blocks[1]) || words.indexOf(blocks[1]) != -1){
                        indB--;
                        if(indB == 0){
                            fimLine = conLine;
                            System.out.println(fileName + ": " + iniLine + "L - " + fimLine + "L: " + complexity + "; " + showCyclomaticComplexity(complexity) + ";");

                            iniLine = -1;
                            fimLine = -1;
                            complexity = 0;
                        }
                    }
				}
				line = br.readLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return (complexity);
	}

	public String showCyclomaticComplexity(int ccValue) {
		String resultRisk = "";
		if (ccValue> 50)
			resultRisk = "Most complex and highly unstable method";
		else if(ccValue>= 21 && ccValue<=50)
			resultRisk = "High risk";
		else if(ccValue>= 11 && ccValue<=20)
			resultRisk = "Moderate risk";
		else
			resultRisk = "Low risk program";
        return resultRisk;
	}

    public void checkFile(String fileName){
        CyclomaticComplexity cc = new CyclomaticComplexity();
        String ext = getExtensionByStringHandling(fileName).orElse(null);
        if(ext != null){
            ext = ext.toLowerCase();
            if(wordsLang.langWords.keySet().contains(ext)){
                cc.check(fileName, ext);
            }
        }
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public void checkFilesDir(String dir, int deep){
        File dirBase = new File(dir);
        if(dirBase.exists()){
            File[] arqs = dirBase.listFiles();
            CyclomaticComplexity cc = new CyclomaticComplexity();
            for(File arq : arqs){
                if(arq.isFile()){
                    String ext = getExtensionByStringHandling(arq.getAbsolutePath()).orElse(null);
                    if(ext != null){
                        ext = ext.toLowerCase();
                        if(wordsLang.langWords.keySet().contains(ext)){
                            cc.check(arq.getAbsolutePath(), ext);
                        }
                    }
                }else if(arq.isDirectory() && deep <= deepRec){
                    checkFilesDir(arq.getAbsolutePath(), deep+1);
                }
            }
        }
    }

    public void checkFilesDir(String dir){
        checkFilesDir(dir, 0);
    }
}