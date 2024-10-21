package com.allancleitonppma.gmail.WareMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allancleitonppma.gmail.WareMap.config.ConfigManager;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.services.UtilService;

@SpringBootApplication
public class WareMapApplication {

	public static void main(String[] args) {
		//SpringApplication.run(WareMapApplication.class, args);

		final int numberOfChambers = 6;
		int choice = 0;
		String path;

		UtilService service = new UtilService();
		Scanner sc = new Scanner(System.in);

		System.out.println(Color.ANSI_PURPLE_BACKGROUND + " WELCOME TO THE WAREMAPE APLICATION      " + Color.ANSI_RESET);
		
		System.out.print(" Para continuar é preciso que o usuário\n "
						+ "informe o cominho do arquivo que"
						+ "\n contem a copia das posições dos "
						+ "\n produtos: ");
		path = sc.nextLine();
		List<Chamber> chambers = service.chargeCameras(path, numberOfChambers, sc);
		
		
		// limpar("cmd", "/c", "cls");

		do {
			System.out.println(Color.ANSI_PURPLE_BACKGROUND + " ESCOLHA UMA OPÇÃO.                      "+ Color.ANSI_RESET);
			System.out.println(" Gerar Separação:..................(1).");
			System.out.println(" Configurações:....................(2).");
			System.out.println(" Sair:.............................(3).");
			System.out.print(" -> "); 
			choice = sc.nextInt();

			switch (choice) {
			
			case 1: {
				gerarSeparacao(service, chambers, sc);
				break;
			}
			case 2: {
				configuracao(sc);
				break;
			}
			case 3: {
				System.out.println(" Sair.");
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choice);
			}
		} while (choice != 3);

		sc.close();

		/*
		 * Scanner sc = new Scanner(System.in);
		 * 
		 * UtilService service = new UtilService();
		 * 
		 * LoadOrder order = service.getloadOrder("c://temp//ordemdecarga.txt", 8689);
		 * 
		 * Separation fS = service.simpleSeparation(order,service.chargeCameras("c://temp//test.txt", 5, sc));
		 * 
		 * System.out.println(fS);
		 * 
		 * 
		 * fS.createArquiveWithSeparation("c://temp//separacao.txt");
		 * 
		 * sc.close();
		 */

		/*try {
			ConfigManager config = new ConfigManager("\\WareMap\\src\\main\\resources\\config.properties");
			int days = 300;
			config.setProperty("congelado", "x -> x.getDays < " + days);
			config.setProperty("qtdecamaras", "300");

			System.out.println(config.getProperty("congelado"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public static void limpar(String arg0, String arg1, String arg2) {
		try {
			ProcessBuilder builder = new ProcessBuilder(arg0, arg1, arg2);
			Process processo = builder.start();

			// Aguarda o processo terminar
			int status = processo.waitFor();

			// Lê a saída do processo
			BufferedReader reader = new BufferedReader(new InputStreamReader(processo.getInputStream()));
			String linha;
			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
			}

			reader.close();
			if (processo.isAlive()) {
				System.out.println(status);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void tipycalSeparation(UtilService service, List<Chamber> chambers, Scanner sc) {
		Separation separation = null;
		String orderCharger, path;
		boolean verify = false;
	
		while (!verify) {
			try {
				System.out.print("\nInsira o caminho do arquivo de ordem de carga: ");
				path = sc.nextLine();
				
				System.out.print("\nInsira o número da ordem de carga: ");
				orderCharger = sc.nextLine();
				
				separation = service.simpleSeparation(service.getloadOrder(path, orderCharger), chambers);  //"c://temp//ordemdecarga.txt"				  
				
				System.out.print("\nCaminho de Onde deseja salvar o arquivo: ");
				path = sc.nextLine();
				
				verify = separation.createArquiveWithSeparation(path); //"c://temp//separacao.txt"
				if(verify) {
					System.out.println("Separação gerada com sucesso!");
					System.out.println("\tDisponivel em: " + path);
					
				}
			} catch (Exception e) {
				System.out.println("O caminho informado não foi encontrado.");
				continue;
			}
		} 
	}

	public static void parametrosGerais(Scanner sc) {
		String path;
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                          " + Color.ANSI_RESET);
		ConfigManager config = null;
		int choiceGeralConfig = 0, valor;
		
		System.out.print(" Informe o caminho do arquivo\n"
						+ " de configuração: ");
		path = sc.nextLine();
	
		try {
			config = new ConfigManager(path);
			
			do {
				System.out.println(Color.ANSI_CYAN_BACKGROUND + " ESCOLHA UMA OPÇÃO.                      " + Color.ANSI_RESET);
				System.out.println(" Fifo congelados suíno:.............(1).");
				System.out.println(" Fifo congelados aviário:...........(2).");
				System.out.println(" Fifo r/ suíno fora do estado:......(3).");
				System.out.println(" Fifo r/ suíno dentro do estado:....(4).");
				System.out.println(" Qtde max p/ separação chão:........(5).");
				System.out.println(" Voltar:............................(6).");
				System.out.print(" -> ");
				choiceGeralConfig = sc.nextInt();
				sc.nextLine();
				switch (choiceGeralConfig) {
					case 1: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.nextInt();
						config.setProperty("congelado_suino", "x -> x.getDays <" + valor);
						System.out.println("Sucesso.");
						break;
						}
					case 2: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.nextInt();
						config.setProperty("congelado_aviario", "x -> x.getDays <" + valor);
						System.out.println("Sucesso.");
						break;
						}
					case 3: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.nextInt();
						config.setProperty("resfri_suino_fora_estado", "x -> x.getDays <" + valor);
						System.out.println("Sucesso.");
						break;
						}
					case 4: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.nextInt();
						config.setProperty("resfri_suino_dentro_estado", "x -> x.getDays <" + valor);
						System.out.println("Sucesso.");
						break;
						}
					case 5: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.nextInt();
						config.setProperty("separacao_chao", String.valueOf(valor));
						System.out.println("Sucesso.");
						break;
						}
					default:
						System.out.println(" Opção inválida! " + choiceGeralConfig);
					}
				
				
				
			} while (choiceGeralConfig != 6);
			

			System.out.println(config.getProperty("congelado"));
		} catch (IOException e) {
			System.out.println(" Erro ao carregar o arquivo: " + e.getMessage());
		}

	}

	public static void configuracao(Scanner sc) {
		int choiceConfig = 0;
		do {
			System.out.println(Color.ANSI_CYAN_BACKGROUND + " ESCOLHA UMA OPÇÃO.                      " + Color.ANSI_RESET);
			System.out.println(" Parametros Gerais:.................(1).");
			System.out.println(" Parametros de Produto:.............(2).");
			System.out.println(" Voltar:............................(3).");
			System.out.print(" -> ");
			choiceConfig = sc.nextInt();
			sc.nextLine();
			
			switch (choiceConfig) {
			case 1: {
				parametrosGerais(sc);
				break;
			}case 2: {
				parametrosDeProduto(sc);
				break;
			}
			default:
				System.out.println("Opção inválida! " + choiceConfig);
			}
			
		} while (choiceConfig != 3);
	}
	
	public static void gerarSeparacao(UtilService service, List<Chamber> chambers, Scanner sc) {
		int choiceSeparation = 0;
		do {
			System.out.println(Color.ANSI_GREEEN_BACKGROUND + " ESCOLHA O TIPO DE CARGA.               "+ Color.ANSI_RESET);
			System.out.println(" Dentro do estado:.................(1).");
			System.out.println(" Fora do estado:...................(2).");
			System.out.println(" Simples:..........................(3).");
			System.out.println(" Voltar:...........................(4).");
			System.out.print(" -> ");
			choiceSeparation = sc.nextInt();
			sc.nextLine();
			
			switch (choiceSeparation) {
			case 1: {
				
				break;
			}case 2: {
				
				break;
			}case 3: {
				tipycalSeparation(service, chambers, sc);
				break;
			}case 4:{
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceSeparation);
			}
			
		} while (choiceSeparation != 4);
	}

	public static void parametrosDeProduto(Scanner sc) {
		String path;
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                          " + Color.ANSI_RESET);
		ConfigManager config = null;
		int valor;
		
		System.out.print(" Informe o caminho do arquivo\n"
						+ " de configuração: ");
		path = sc.nextLine();
		
		try {
			String key;
			config = new ConfigManager(path);
			System.out.print(" Informe o codigo do produto: ");
			key = sc.nextLine();
			System.out.println(" Para definir como congelado:......(1).");
			System.out.println(" Para definir como resfriado:......(2).");
			valor = sc.nextInt();
			if(valor == 1) {
				config.setProperty(key, "congelado");
				System.out.println("Sucesso!");
			}else if(valor == 2){
				config.setProperty(key, "resfriado");
				System.out.println("Sucesso!");
			}else{
				System.out.println(" Opção inválida! " + valor);
			}
			
			
		} catch (IOException e) {
			System.out.println(" Erro ao carregar o arquivo: " + e.getMessage());
		}
	}
}
