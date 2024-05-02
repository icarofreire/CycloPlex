package cycloplex;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Optional;
import java.util.StringTokenizer;
import cycloplex.wordsLang;


public class CyclomaticComplexity {

    /**\/ profundidade da recursão por diretórios; */
    private final int deepRec = 20;
    /**\/ obter do usuário o tipo de avaliação a ser realizado; */
    private int tipoParaAnalise;
    /**\/ tipos de avaliações; */
    public static enum tiposAnalise {
        BLOCOS,
        INTERNAL_BLOCOS,
        LINHAS
    };
    /**\/ complexidade mínima a ser exibida; */
    private int minComplex = 0;

    public void setTipoParaAnalise(int tipoParaAnalise){
        this.tipoParaAnalise = tipoParaAnalise;
    }

    public void setMinComplex(int minComplex){
        this.minComplex = minComplex;
    }

	private int checkByBlocks(final String fileName, final String lang) {
		int complexity = 0;
        final String[] blocks = wordsLang.langBlocks.get(lang);
		final String[] keywords = wordsLang.langWords.get(lang);
		try {
			final FileReader fr = new FileReader(fileName);
			final BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

            int iniLine = -1;
            int fimLine = -1;
            int indB = 0;

            int conLine = 0;
			while (line != null)
			{
                conLine++;
				final StringTokenizer stTokenizer = new StringTokenizer(line);
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
                            avaliarExibirComplex(fileName, iniLine, fimLine, complexity);
                            iniLine = -1;
                            fimLine = -1;
                            complexity = 0;
                        }
                    }
				}
				line = br.readLine();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return (complexity);
	}

    private int checkByNestedLines(final String fileName, final String lang) {
		int complexity = 0;
		final String[] keywords = wordsLang.langWords.get(lang);
		try {
			final FileReader fr = new FileReader(fileName);
			final BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

            int iniLine = -1;
            int fimLine = -1;

            int conLine = 0;
			while (line != null)
			{
                conLine++;
				final StringTokenizer stTokenizer = new StringTokenizer(line);
				while (stTokenizer.hasMoreTokens())
				{
					String words = stTokenizer.nextToken();
                    for(int i=0; i<keywords.length; i++)
                    {
                        if (keywords[i].equals(words))
                        {
                            complexity++;
                            if(iniLine == -1) iniLine = conLine;
                        }
                    }
				}
				line = br.readLine();
                if(line != null && line.isBlank()){
                    if(iniLine != -1 && complexity > 0){
                        fimLine = conLine;
                        avaliarExibirComplex(fileName, iniLine, fimLine, complexity);
                        iniLine = -1;
                        fimLine = -1;
                        complexity = 0;
                    }
                }
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return (complexity);
	}

    private int checkByInternalBlocks(final String fileName, final String lang) {
		int complexity = 0;
        final String[] blocks = wordsLang.langBlocks.get(lang);
		final String[] keywords = wordsLang.langWords.get(lang);
		try {
			final FileReader fr = new FileReader(fileName);
			final BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

            Vector<Integer> vbloc = new Vector<Integer>();
            Vector<Integer> queue = new Vector<Integer>();
            HashMap<Integer, Vector<Integer>> blockLines = new HashMap<Integer, Vector<Integer>>();
            int iniLine = -1;
            int fimLine = -1;
            int indB = 1;

            int conLine = 0;
			while (line != null)
			{
                conLine++;
				final StringTokenizer stTokenizer = new StringTokenizer(line);
				while (stTokenizer.hasMoreTokens())
				{
					String words = stTokenizer.nextToken();
                    if(words.equals(blocks[0]) || words.indexOf(blocks[0]) != -1){
                        if(iniLine == -1) iniLine = conLine;
                        if(vbloc.contains(indB))indB++;
                        vbloc.add(indB);
                        queue.add(indB);

                        blockLines.put(indB, new Vector<Integer>(Arrays.asList(conLine)));
                    }

                    for(int i=0; i<keywords.length; i++)
                    {
                        if (keywords[i].equals(words))
                        {
                            complexity++;
                            vbloc.add(-conLine);
                        }
                    }

                    if(words.equals(blocks[1]) || words.indexOf(blocks[1]) != -1){
                        int ulti = queue.get(queue.size()-1);
                        vbloc.add(queue.get(queue.size()-1));
                        queue.remove(queue.size()-1);

                        Vector<Integer> vecLinesTemp = blockLines.get(ulti);
                        vecLinesTemp.add(conLine);
                        blockLines.put(ulti, vecLinesTemp);

                        if(indB == 0){
                            fimLine = conLine;
                            avaliarExibirComplex(fileName, iniLine, fimLine, complexity);
                            iniLine = -1;
                            fimLine = -1;
                            complexity = 0;
                        }
                    }
				}
				line = br.readLine();
			}
            for(int i=0; i<vbloc.size(); i++){
                int l = vbloc.get(i);
                if(l > 0){
                    int conNeg = 0;
                    int ind = vbloc.indexOf(l, i+1);

                    if(ind != -1){
                        for(int k=i; k<ind; k++){
                            if(vbloc.get(k) < 0){
                                conNeg++;
                            }
                        }
                    }
                    if(conNeg > 0){
                        // System.out.println(">>" + l + " - " + (ind) + " : " + conNeg + " : [" + blockLines.get(l).get(0) + "L, " + blockLines.get(l).get(1) + "L]");
                        avaliarExibirComplex(fileName, blockLines.get(l).get(0), blockLines.get(l).get(1), conNeg);
                    }
                }
            }
		}catch (IOException e){
			e.printStackTrace();
		}
		return (complexity);
	}

    private void showComplex(String fileName, int iniLine, int fimLine, int complexity){
        System.out.println(fileName + ": " + iniLine + "L - " + fimLine + "L: " + complexity + "; " + showLevelCyclomaticComplexity(complexity) + ";");
    }

    private void avaliarExibirComplex(String fileName, int iniLine, int fimLine, int complexity){
        if(this.minComplex == 0 && complexity > 0){
            showComplex(fileName, iniLine, fimLine, complexity);
        }else if(this.minComplex > 0 && complexity >= this.minComplex){
            showComplex(fileName, iniLine, fimLine, complexity);
        }
    }

	private String showLevelCyclomaticComplexity(int ccValue) {
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

    /**\/ fazer avaliação de um arquivo; */
    public void checkFile(final String fileName){
        String ext = getExtensionByStringHandling(fileName).orElse(null);
        if(ext != null){
            ext = ext.toLowerCase();
            if(wordsLang.langWords.keySet().contains(ext)){
                if(this.tipoParaAnalise == tiposAnalise.BLOCOS.ordinal()){
                    checkByBlocks(fileName, ext);
                }else if(this.tipoParaAnalise == tiposAnalise.LINHAS.ordinal()){
                    checkByNestedLines(fileName, ext);
                }else if(this.tipoParaAnalise == tiposAnalise.INTERNAL_BLOCOS.ordinal()){
                    checkByInternalBlocks(fileName, ext);
                }
            }
        }
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private void checkFilesDir(final String dir, final int deep){
        final File dirBase = new File(dir);
        if(dirBase.exists()){
            final File[] arqs = dirBase.listFiles();
            for(File arq : arqs){
                if(arq.isFile()){
                    String ext = getExtensionByStringHandling(arq.getAbsolutePath()).orElse(null);
                    if(ext != null){
                        ext = ext.toLowerCase();
                        if(wordsLang.langWords.keySet().contains(ext)){
                            if(this.tipoParaAnalise == tiposAnalise.BLOCOS.ordinal()){
                                checkByBlocks(arq.getAbsolutePath(), ext);
                            }else if(this.tipoParaAnalise == tiposAnalise.LINHAS.ordinal()){
                                checkByNestedLines(arq.getAbsolutePath(), ext);
                            }
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