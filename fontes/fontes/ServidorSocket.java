package fontes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorSocket extends Thread {
	/**
	 * definindo os atributos da classe
	 * **/
    private static Map<String, PrintStream> MAP_CLIENTES;
    private Socket conexao;
    private String nomeCliente;
    private static List<String> LISTA_DE_NOMES = new ArrayList<String>();
    
    /**
     * Passando um cleinte para o servidor
     * **/
    public ServidorSocket(Socket socket) {
        this.conexao = socket;
    }

    /**
     * Armazenando o nome do cliente na lista 
     * **/
    public boolean armazena(String newName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(newName))
                return true;
        }
        LISTA_DE_NOMES.add(newName);
        return false;
    }
    
    /**
     * Removendo o cliente antigo - velho da lista
     * **/
    public void remove(String oldName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(oldName))
                LISTA_DE_NOMES.remove(oldName);
        }
    }

    /**
     * realizando o teste dos métodos da classe 
     * **/
    public static void main(String args[]) {
        MAP_CLIENTES = new HashMap<String, PrintStream>();
        try {
            ServerSocket server = new ServerSocket(12345);
            System.out.println("ServidorSocket rodando na porta 12345");
            
            //-- Trabalhando com vários clientes - multithred --//
            while (true) {
                Socket conexao = server.accept();
                
                //-- Iniciando a thread --//
                Thread t = new ServidorSocket(conexao);
                t.start();
            }
            
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    /**
     * Tratando a thread 
     * **/
    public void run() {
        try {
        	//-- Tratando o buffer de entrada --//
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            
            //-- Tratando o buffer de saída --//
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            
            //-- adicionando a entrada da linha ao cliente --//
            this.nomeCliente = entrada.readLine();
            
            //-- Armazena o cliente na lista - coleção --//
            if (armazena(this.nomeCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
                this.conexao.close();
                return;
                
            } else {
                //-- mostra o nome do cliente conectado ao servidor --//
                System.out.println(this.nomeCliente + " : Conectado ao Servidor!");

                String s = "";
                
                for (String aux : LISTA_DE_NOMES) {
                    if (!aux.equalsIgnoreCase(this.nomeCliente)) {
                        s = s + aux + " ";
                    }
                }
                
                //Quando o cliente se conectar recebe todos que estao conectados
                saida.println("Conectados: " + s);

                //envia lista para todos assim que qualquer cliente se conecta
                sendListToAll(this.nomeCliente);
            }

            //-- verifica se o cliente é igual a null se sim retorna null --//
            if (this.nomeCliente == null) {
                return;
            }
            
            //adiciona os dados de saida do cliente no objeto MAP_CLIENTES. A chave sera o nome e valor o printstream msg
            MAP_CLIENTES.put(this.nomeCliente, saida);

            String[] msg = entrada.readLine().split(":");
            
            while (msg != null && !(msg[0].trim().equals(""))) {
                send(saida, " escreveu: ", msg);
                msg = entrada.readLine().split(":");
            }
            
            System.out.println(this.nomeCliente + " saiu do bate-papo!");
            
            String[] out = {" do bate-papo!"};
            
            send(saida, " saiu", out);
            
            //-- remove o cliente --//
            remove(this.nomeCliente);

            //-- remove o cliente da lista --//
            MAP_CLIENTES.remove(this.nomeCliente);

            this.conexao.close();
            
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }

    /**
     * Se o array da msg tiver tamanho igual a 1, entao envia para todos
     * Se o tamanho for 2, envia apenas para o cliente escolhido
     */
    public void send(PrintStream saida, String acao, String[] msg) {
        out:
        for (Map.Entry<String, PrintStream> cliente : MAP_CLIENTES.entrySet()) {
            PrintStream chat = cliente.getValue();
            if (chat != saida) {
                if (msg.length == 1) {
                    chat.println(this.nomeCliente + acao + msg[0]);
                    
                } else {
                    if (msg[1].equalsIgnoreCase(cliente.getKey())) {
                        chat.println(this.nomeCliente + acao + msg[0]);
                        break out;
                    }
                }
            }
        }
    }

    //-- Setando para todos clientes ativos --//
    public void sendListToAll(String nome) {
        for (Map.Entry<String, PrintStream> cliente : MAP_CLIENTES.entrySet()) {
            if (!cliente.getKey().equalsIgnoreCase(nome)) {
                String aux = "";
                for (String s : LISTA_DE_NOMES) {
                    if (!s.equalsIgnoreCase(cliente.getKey())) {
                        aux = aux + s + " ";
                    }
                }
                PrintStream chat = cliente.getValue();
                chat.println("[" + aux + "]");
                chat.flush();
            }
        }
    }
}