/**
 * Classe responsável por tratar as mensagens dos clientes
 */
package fontes;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author 38014335104
 * Gerencia a s mensagens dos clientes
 */
public class ClienteChatPC {

	/**
	 * Costrutor padrão da classe
	 */
	public ClienteChatPC() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * Realizando meus testes
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		//-- Se conectando com um cliente -  por IP e Porta --//
		Socket cliente = new Socket("127.0.0.1", 12345);
		
		System.out.println("O cliente se conectou a um servidor!");
		
		//-- Lendo os dados informados pelo cliente, da entrada padrão (teclado) --//
		PrintStream saida = new PrintStream(cliente.getOutputStream());
		Scanner teclado = new Scanner(System.in);
		while(teclado.hasNext()){
			//-- lê a linha e faz algo com ela - jogar no buffer de saida  --//
			saida.println(teclado.nextLine());
			
		}
		saida.close();
		teclado.close();

	}

}
