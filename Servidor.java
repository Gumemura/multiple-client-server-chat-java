import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

//UI
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
	private static ArrayList<BufferedWriter>clientes;
	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr
;	private static String anagramaDisplay;
	private static List<String> anagramaGabarito;

	/*
	*
	* Método construtor
	* @param com do tipo Socket
	*/
	public Servidor(Socket con){
	   this.con = con;
	   try {
	        in = con.getInputStream();
	        inr = new InputStreamReader(in);
	        bfr = new BufferedReader(inr);
	   } catch (IOException e) {
	        e.printStackTrace();
	   }
	}

	public static String swap(String a, int i, int j) 
    { 
        char temp; 
        char[] charArray = a.toCharArray(); 
        temp = charArray[i]; 
        charArray[i] = charArray[j]; 
        charArray[j] = temp; 
        return String.valueOf(charArray); 
    } 

	private static void geraAnagrama(String str, int start, int end) 
    { 
        if (start != end) {
            for (int i = start; i <= end; i++) { 
                str = swap(str, start, i); 
                geraAnagrama(str, start + 1, end); 
                str = swap(str, start, i); 
            } 
        }

        if (!anagramaGabarito.contains(str)){
        	anagramaDisplay = str;
        }
    }

	public static void escolheAnagrama() throws IOException
	{
		BufferedReader depositoAnagramas = new BufferedReader(new FileReader("anagramas.txt"));
		try {
			int txtLimit = 0;
			String line = depositoAnagramas.readLine();;

		    while (line != null) {
		    	txtLimit++;
		    	line = depositoAnagramas.readLine();
		    }

		    depositoAnagramas.close();
		    depositoAnagramas = new BufferedReader(new FileReader("anagramas.txt"));

			Random rand = new Random();
			int anagramaEscolhido = rand.nextInt(txtLimit) + 1; 
			for (int i = 0; i < anagramaEscolhido; i++) {
				line = depositoAnagramas.readLine();
		    }

		    anagramaGabarito = Arrays.asList(line.split(" "));
		    String primeiraPalavra = anagramaGabarito.get(0);
		    geraAnagrama(primeiraPalavra, 0, primeiraPalavra.length() - 1);
		}finally {
			depositoAnagramas.close();
		}
	}

	/**
	  * Método run
	  */
	public void run(){
		try{
		    String msg;
		    OutputStream ou =  this.con.getOutputStream();
		    Writer ouw = new OutputStreamWriter(ou);
		    BufferedWriter bfw = new BufferedWriter(ouw);
		    
		    clientes.add(bfw);
		    nome = msg = bfr.readLine();

		    while(!"Sair".equalsIgnoreCase(msg) && msg != null)
		    {
		    	if(anagramaGabarito.contains(msg)){
					System.out.println("SUCESSO");
		    	}
		    	msg = bfr.readLine();
				sendToAll(bfw, msg);
				System.out.println(msg);
		    }

	   	}catch (Exception e) {
	    	e.printStackTrace();
	   	}
	}

	/***
	 * Método usado para enviar mensagem para todos os clients
	 * @param bwSaida do tipo BufferedWriter
	 * @param msg do tipo String
	 * @throws IOException
	 */

	public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException
	{
		BufferedWriter bwS;

		for(BufferedWriter bw : clientes){
			bwS = (BufferedWriter)bw;
   			if(!(bwSaida == bwS)){
    			bw.write(nome + " -> " + msg+"\r\n");
	     		bw.flush();
	   		}
	  	}
	}

	/***
	   * Método main
	   * @param args 
	*/
	public static void main(String []args) {
	  	try{
			escolheAnagrama();
		    //Cria os objetos necessário para instânciar o servidor
		    JLabel lblMessage = new JLabel("Porta do Servidor:");
		    JTextField txtPorta = new JTextField("12345");
		    Object[] texts = {lblMessage, txtPorta};
		    JOptionPane.showMessageDialog(null, texts);
		    server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
		    clientes = new ArrayList<BufferedWriter>();
		    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+ txtPorta.getText());
		    while(true){
		       System.out.println("Aguardando conexão...");
		       Socket con = server.accept();
		       System.out.println("Cliente conectado...");
		       Thread t = new Servidor(con);
		       t.start();
		    }
	  	}catch (Exception e) {
	  		e.printStackTrace();
	  	}
	}// Fim do método main
} //Fim da classe

