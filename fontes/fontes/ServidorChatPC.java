/**
 * Esta classe é responsável por gerenciar o servidor
 */
package fontes;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * @author 38014335104
 * Gerenciamento de servidor socket
 */
public class ServidorChatPC {

	/**
	 * Construtor padrão da classe
	 */
	public ServidorChatPC() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Criando meus testes com o servidor
	 * **/
	public static void main(String args[])throws IOException{
		ServerSocket ss = new ServerSocket(12345);
		
		System.out.println("Porta 12345 aberta!\n");
		
		//-- Aguardando por um cliente, este métodos fica bloqueado até que um cliente se comunique --//
		Socket cliente = ss.accept();
		
		//-- imprime o ip do cliente --//
		System.out.println("Nova conexão com o cliente: "+cliente.getInetAddress().getHostAddress()+"\n");
		
		//-- Lendo toda as informações que o cliente envia --//
		Scanner scanner = new Scanner(cliente.getInputStream());
		
		while(scanner.hasNext()){
			System.out.println(scanner.nextLine());
		}
		
		//-- fechando as conexões, começada pelo fluxo --//
		scanner.close();
		ss.close();
		cliente.close();
		
	}

}
