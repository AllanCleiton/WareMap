package com.allancleitonppma.gmail.WareMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allancleitonppma.gmail.WareMap.config.ConfigManager;
import com.allancleitonppma.gmail.WareMap.core.FloorSeparation;
import com.allancleitonppma.gmail.WareMap.core.ForkliftSeparation;
import com.allancleitonppma.gmail.WareMap.core.Separation;
import com.allancleitonppma.gmail.WareMap.core.Separations;
import com.allancleitonppma.gmail.WareMap.entities.Chamber;
import com.allancleitonppma.gmail.WareMap.services.UtilService;


@SpringBootApplication
public class WareMapApplication {

	public static void main(String[] args) {
		//SpringApplication.run(WareMapApplication.class, args);
		
		
		UtilService servicef = new UtilService();
		Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation> s = null;
 		try {
			List<Chamber> chambers = servicef.chargeCameras("C:\\temp", 5);
			
			s = servicef.stateSeparation(servicef.getloadOrder("C:\\temp", "45884"), chambers,
					new ConfigManager("C:\\temp\\config\\geralparameters.properties"));
			
			s.getForklift().getDtoProducts().forEach(System.out::println);
			s.getFloor().getDtoProducts().forEach(System.out::println);
			s.getCold().getDtoProducts().forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		final int numberOfChambers = 5;
		String path;
		int choice = 0;
		
		UtilService service = new UtilService();
		Scanner sc = new Scanner(System.in);

		System.out.println(Color.ANSI_PURPLE_BACKGROUND + " WELCOME TO THE WAREMAPE APLICATION      " + Color.ANSI_RESET);
		
		System.out.print(" Para continuar é preciso que o usuário\n "
						+ "informe o cominho do arquivo que"
						+ "\n contem a copia das posições dos "
						+ "\n produtos: ");
		path = sc.nextLine();
		List<Chamber> chambers = null;
		do {
			try {
				chambers = service.chargeCameras(path, numberOfChambers);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(" " + e.getMessage());
			}
			if(chambers == null) {
				System.out.print(" O caminho informado nao foi encontrado.\n Informe o caminho: ");
				path = sc.nextLine();
			}
		} while (chambers == null);
		
		
		
		//clearScreen();

		do {
			System.out.println(Color.ANSI_PURPLE_BACKGROUND + " ESCOLHA UMA OPÇÃO.                      "+ Color.ANSI_RESET);
			System.out.println(" Gerar Separação:..................(1).");
			System.out.println(" Configurações:....................(2).");
			System.out.println(" Sair:.............................(3).");
			System.out.print(" -> "); 
			choice = sc.nextInt();

			switch (choice) {
			
			case 1: {
				gerarSeparacao(service, chambers, sc, path);
				break;
			}
			case 2: {
				configuracao(path ,sc);
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


	}

	public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Comando para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Comando para Linux e macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar o terminal: " + e.getMessage());
        }
     }
	
	public static void comandTerminal(String arg0, String arg1, String arg2) {
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

	public static void tipycalSeparation(UtilService service, List<Chamber> chambers, Scanner sc, String defaultPath) {
		Separation separation = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean verify = false;
		boolean success = false; 
	
		while (!verify) {
			try {
				
				String os = System.getProperty("os.name");
				if (os.contains("Windows")) {
					
					success = new File(defaultPath + "//separations").mkdir();
					
					if(success) {
						prefix = defaultPath +  "//separations//";

						System.out.print("\n Número da ordem de carga: ");
						orderCharger = sc.nextLine().trim();
						
						String oc = orderCharger.concat(".txt"); 
						finalPath = prefix + oc;
						
					}else {
						System.out.print("\n Número da ordem de carga: ");
						orderCharger = sc.nextLine().trim();
						
						String oc = orderCharger.concat(".txt"); 
						finalPath = defaultPath + "//separations//" + oc;				
						}

				} else {
					success = new File(defaultPath + "/separations").mkdir();
					
					if(success) {
						prefix = defaultPath + "/separations/";		

						System.out.print("\n Número da ordem de carga: ");
						orderCharger = sc.nextLine().trim();
						
						String oc = orderCharger.concat(".txt"); 
						finalPath = prefix + oc;
						
					}else {
						System.out.print("\n Número da ordem de carga: ");
						orderCharger = sc.nextLine().trim();
						
						String oc = orderCharger.concat(".txt"); 
						finalPath = defaultPath + "/separations/" + oc;				
						}

				}
			
								
				separation = service.simpleSeparation(service.getloadOrder(defaultPath, orderCharger), chambers);  //"c://temp//ordemdecarga.txt"				  

								
				
				verify = separation.createArquiveWithSeparation(finalPath);
				
				if(verify) {
					System.out.println(" Separação gerada com sucesso!");
					System.out.println("\tDisponivel em: " + finalPath);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}

	public static void stateSeparation(UtilService service, List<Chamber> chambers, Scanner sc, String defaultPath) {
		Separations<ForkliftSeparation, FloorSeparation, ForkliftSeparation> separations = null;
		ConfigManager propert = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean verify = false;
		boolean success = false; 
		boolean p,q,r;
	
		while (!verify) {
			try {
				
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("\n Número da ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("\n Número da ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				propert = new ConfigManager(defaultPath + "/config/geralparameters.properties");
				separations = service.stateSeparation(service.getloadOrder(defaultPath, orderCharger), chambers, propert);  //"c://temp//ordemdecarga.txt"				  

				p = separations.getForklift().createArquiveWithSeparation(finalPath.replace(".txt", "_forklift") + ".txt");
				q = separations.getFloor().createArquiveWithSeparation(finalPath.replace(".txt", "_floor") + ".txt");
				r = separations.getCold().createArquiveWithSeparation(finalPath.replace(".txt", "_cold") + ".txt");
				
				if(p || q || r) {
					System.out.println(" Separação gerada com sucesso!");
					System.out.println("\tDisponivel em: " + finalPath);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}
	
	public static void parametrosGerais(String defaultPath ,Scanner sc) {
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                          " + Color.ANSI_RESET);
		ConfigManager config = null;
		int choiceGeralConfig = 0; 
		String valor;
		
		String  finalFile;

		FileWriter writer = null;
		

			
			//verify if no exist a folder CONFIG
			if( !(new File(defaultPath + "/config").exists())) {
				
				if(new File(defaultPath + "/config").mkdir()) {
					try {
						writer = new FileWriter(defaultPath + "/config/geralparameters.properties");
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}finally {
						if(writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
					}
				}
			}
			//verify if no exist a file GERALPARAMETERS.PROPERTIES
			else if(!(new File(defaultPath + "/config/geralparameters.properties").exists())) {
				try {
					writer = new FileWriter(defaultPath + "/config/geralparameters.properties");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}finally {
					if(writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
				}
			}
			
			finalFile = defaultPath +  "/config/geralparameters.properties";
		
	
		try {
			config = new ConfigManager(finalFile);
			
			do {
				System.out.println(Color.ANSI_CYAN_BACKGROUND + " ESCOLHA UMA OPÇÃO.                      " + Color.ANSI_RESET);
				System.out.println(" Fifo congelados:...................(1).");
				System.out.println(" Fifo resfriado fora do estado:.....(2).");
				System.out.println(" Fifo resfriado dentro do estado:...(3).");
				System.out.println(" Qtde max p/ separação chão:........(4).");
				System.out.println(" Voltar:............................(5).");
				System.out.print(" -> ");
				choiceGeralConfig = sc.nextInt();
				sc.nextLine();
				switch (choiceGeralConfig) {
					case 1: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.next();
						config.setProperty("congelado", valor);
						System.out.println(" Sucesso.");
						break;
						}
					case 2: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.next();
						config.setProperty("resfri_fora_estado", valor);
						System.out.println(" Sucesso.");
						break;
						}
					case 3: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.next();
						config.setProperty("resfri_dentro_estado", valor);
						System.out.println(" Sucesso.");
						break;
						}
					case 4: {
						System.out.print(" Informe o novo valor: ");
						valor = sc.next();
						config.setProperty("separacao_chao",valor);
						System.out.println(" Sucesso.");
						break;
						}
					case 5:{
						System.out.println("Voltar.");
					}
					default:
						System.out.println(" Opção inválida! " + choiceGeralConfig);
					}
				
				
				
			} while (choiceGeralConfig != 5);
			

			//System.out.println(config.getProperty(" congelado"));
		} catch (IOException e) {
			System.out.println(" Erro ao carregar o arquivo: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static void configuracao(String defaultPath, Scanner sc) {
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
				parametrosGerais(defaultPath ,sc);
				break;
			}case 2: {
				parametrosDeProduto(defaultPath, sc);
				break;
			}
			case 3: {
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceConfig);
			}
			
		} while (choiceConfig != 3);
	}
	
	public static void gerarSeparacao(UtilService service, List<Chamber> chambers, Scanner sc, String defaultPath) {
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
				stateSeparation(service, chambers, sc, defaultPath);
				break;
			}case 2: {
				
				break;
			}case 3: {
				tipycalSeparation(service, chambers, sc, defaultPath);
				break;
			}case 4:{
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceSeparation);
			}
			
		} while (choiceSeparation != 4);
	}

	public static void parametrosDeProduto(String defaultPath ,Scanner sc) {
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                          " + Color.ANSI_RESET);
		ConfigManager config = null;
		int valor;
		
		String  finalFile;
		FileWriter writer = null;
			//verify if exist a folder CONFIG
			if( !(new File(defaultPath + "/config").exists())) {
				
				if(new File(defaultPath + "/config").mkdir()) {
					try {
						writer = new FileWriter(defaultPath + "/config/productparameters.properties");
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}finally {
						if(writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
					}
				}
			}else if(!(new File(defaultPath + "/config/productparameters.properties").exists())) {
				try {
					writer = new FileWriter(defaultPath + "/config/productparameters.properties");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}finally {
					if(writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
				}
			}
			
			finalFile = defaultPath +  "/config/productparameters.properties";
		

		
		try {
			String key;
			config = new ConfigManager(finalFile);
			System.out.print(" Informe o codigo do produto: ");
			key = sc.nextLine();
			System.out.println(" Para definir como congelado:......(1).");
			System.out.println(" Para definir como resfriado:......(2).");
			System.out.print(" -> ");
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
			e.printStackTrace();
		}
		
	}	
}
