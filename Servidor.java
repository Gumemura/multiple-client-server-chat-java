import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//UI
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
	private static ArrayList<BufferedWriter> clientes;
	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private LocalDateTime agora;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	/**
	 * Método construtor
	 * 
	 * @param com do tipo Socket
	 */
	public Servidor(Socket con) {
		this.con = con;
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método run
	 */
	public void run() {
		try {
			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			nome = msg = bfr.readLine();

			while (!"/sair".equalsIgnoreCase(msg) && msg != null) {
				msg = bfr.readLine();
				
				sendToAll(bfw, msg);
				System.out.println(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/***
	 * Método usado para enviar mensagem para todos os clients
	 * 
	 * @param bwSaida do tipo BufferedWriter
	 * @param msg     do tipo String
	 * @throws IOException
	 */
	public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
		BufferedWriter bwS;
		agora = LocalDateTime.now();
		String horario = agora.format(formatter);

		for (BufferedWriter bw : clientes) {
			bwS = (BufferedWriter) bw;
			if (!(bwSaida == bwS)) {
				bw.write("[" + horario + " " + nome + "]: " + msg + "\r\n");
				bw.flush();
			}
		}
	}

	/***
	 * Método main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Cria os objetos necessário para instânciar o servidor
			JLabel lblMessage = new JLabel("Porta do Servidor:");
			JTextField txtPorta = new JTextField("1003");
			Object[] texts = { lblMessage, txtPorta };
			JOptionPane.showMessageDialog(null, texts);
			server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
			clientes = new ArrayList<BufferedWriter>();
			JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

			while (true) {
				System.out.println("Aguardando jogador conectar...");
				Socket con = server.accept();
				System.out.println(con.getInetAddress() + " conectado...");
				Thread t = new Servidor(con);
				t.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}// Fim do método main
} // Fim da classe
