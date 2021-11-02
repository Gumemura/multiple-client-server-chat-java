import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Anagrama {
    private static final String arquivo = "anagrama.txt";
    private String palavra;
    private String anagrama;
    private boolean comparar;

    public Anagrama() {
        try {
            SecureRandom randomNumbers = new SecureRandom();
            int randomValue = randomNumbers.nextInt(261797);

            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); // lê a primeira linha
            // a variável "linha" recebe o valor "null" quando o processo
            // de repetição atingir o final do arquivo texto
            int i = 0;
            while (true) {
                if (i == randomValue) {
                    palavra = linha;
                    System.out.println(linha);
                    break;
                }

                i++;
                linha = lerArq.readLine(); // lê da segunda até a última linha
            }
            List<String> lista = Arrays.asList(palavra.split(""));
            Collections.shuffle(lista);
            for (String caracter : lista) {
                if (anagrama == null) {
                    anagrama = caracter;
                } else {
                    anagrama += caracter;
                }

            }
            System.out.println(anagrama);
            comparar = true;

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }

    public String getPalavra() {
        return palavra;
    }

    public String getAnagrama() {
        return anagrama;
    }

    public boolean getComparar() {
        return comparar;
    }
    public void getComparar(boolean comparar) {
        this.comparar = comparar;
    }

}
