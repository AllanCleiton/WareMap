package com.allancleiton.waremap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import com.allancleiton.waremap.config.ConfigManager;
import com.allancleiton.waremap.config.ParameterProduct;
import com.allancleiton.waremap.entities.Category;
import com.allancleiton.waremap.entities.Separation;
import com.allancleiton.waremap.entities.enums.SeparationSet;
import com.allancleiton.waremap.services.IntegrationService;
import com.allancleiton.waremap.services.SeparationFactory;


public class WareMapApplication {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		final String path = "/users/duda/waremap";
		int choice = 0;
		
		do {
			System.out.println(Color.ANSI_PURPLE_BACKGROUND + " WELCOME TO THE WAREMAPE APLICATION          "+ Color.ANSI_RESET);
			System.out.println(" Gerar Separação:..................(1).");
			System.out.println(" Configurações:....................(2).");
			System.out.println(" Sair:.............................(3).");
			System.out.print(" -> "); 
			choice = sc.nextInt();
			clearScreen();
			switch (choice) {
			case 1: {
				gerarSeparacao(sc, path);
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

	public static void tipycalSeparation(Scanner sc, String defaultPath) {
		Separation separation = null;
		SeparationFactory factory = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean verify = false;
		boolean success = false; 
	
		while (!verify) {
			try {
				
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				
				factory = new SeparationFactory(new IntegrationService(defaultPath));
								
				separation = factory.simpleSeparation();		 	
				
				verify = separation.createArquiveWithSeparation(finalPath);
				
				if(verify) {
					System.out.println(Color.ANSI_PURPLE_BACKGROUND + " Separação de carga.                         "+ Color.ANSI_RESET);
					System.out.println(" Separação gerada com sucesso!");
					System.out.println(" Disponivel em: " + finalPath);
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}

	public static void stateSeparation(Scanner sc, String defaultPath) {
		SeparationSet<Separation, Separation, Separation> separations = null;
		SeparationFactory factory = null;
		ConfigManager propert = null;
		String orderCharger;
		String prefix;
		String finalPath;
		boolean success = false; 
		boolean p = false,q = false,r = false;
	
		while (!(p || q || r)) {
			try {
				factory = new SeparationFactory(new IntegrationService(defaultPath));
				success = new File(defaultPath + "/separations").mkdir();
				
				if(success) {
					prefix = defaultPath + "/separations/";		
	
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = prefix + oc;
					
				}else {
					System.out.print("Informe a ordem de carga: ");
					orderCharger = sc.nextLine().trim();
					clearScreen();
					
					String oc = orderCharger.concat(".txt"); 
					finalPath = defaultPath + "/separations/" + oc;				
				}

				propert = new ConfigManager(defaultPath + "/config/geralparameters.properties");
				separations = factory.stateSeparation(propert); 			  

				p = separations.getForklift().createArquiveWithSeparation(finalPath.replace(".txt", "_forklift") + ".txt");
				q = separations.getFloor().createArquiveWithSeparation(finalPath.replace(".txt", "_floor") + ".txt");
				r = separations.getCold().createArquiveWithSeparation(finalPath.replace(".txt", "_cold") + ".txt");
				
				if(p || q || r) {
					System.out.println(Color.ANSI_PURPLE_BACKGROUND + " Separação de carga.                         "+ Color.ANSI_RESET);

					System.out.println(" Separação gerada com sucesso!");
					System.out.println(" Disponivel em:\n " + finalPath);
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
	}
	
	public static void parametrosGerais(String defaultPath ,Scanner sc) {
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                              " + Color.ANSI_RESET);
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
				System.out.println(" Fifo congelados:...................(1).");
				System.out.println(" Fifo resfriado fora do estado:.....(2).");
				System.out.println(" Fifo resfriado dentro do estado:...(3).");
				System.out.println(" Qtde max p/ separação chão:........(4).");
				System.out.println(" Voltar:............................(5).");
				System.out.print(" -> ");
				choiceGeralConfig = sc.nextInt();
				sc.nextLine();
				clearScreen();
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
						break;
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
			System.out.println(Color.ANSI_CYAN_BACKGROUND + " ESCOLHA UMA OPÇÃO.                          " + Color.ANSI_RESET);
			System.out.println(" Parametros Gerais:.................(1).");
			System.out.println(" Parametros de Produto:.............(2).");
			System.out.println(" Parametros de Categorias:..........(3).");
			System.out.println(" Voltar:............................(4).");
			System.out.print(" -> ");
			choiceConfig = sc.nextInt();
			sc.nextLine();
			clearScreen();
			switch (choiceConfig) {
			case 1: {
				parametrosGerais(defaultPath ,sc);
				break;
			}case 2: {
				parametrosDeProduto(defaultPath, sc);
				break;
			}
			case 3: {
				configCategories(defaultPath, sc);
				break;
			}
			case 4: {
				break;
			}
			default:
				System.out.println(" Opção inválida! " + choiceConfig);
			}
			
		} while (choiceConfig != 3);
	}
	
	public static void gerarSeparacao(Scanner sc, String defaultPath) {
		int choiceSeparation = 0;
		do {
			System.out.println(Color.ANSI_GREEEN_BACKGROUND + " ESCOLHA O TIPO DE CARGA.                    "+ Color.ANSI_RESET);
			System.out.println(" Dentro do estado:.................(1).");
			System.out.println(" Fora do estado:...................(2).");
			System.out.println(" Simples:..........................(3).");
			System.out.println(" Voltar:...........................(4).");
			System.out.print(" -> ");
			choiceSeparation = sc.nextInt();
			sc.nextLine();
			clearScreen();
			
			switch (choiceSeparation) {
			case 1: {
				stateSeparation(sc, defaultPath);
				break;
			}case 2: {
				
				break;
			}case 3: {
				tipycalSeparation(sc, defaultPath);
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
		System.out.println(Color.ANSI_CYAN_BACKGROUND + " CONFIGURAÇÕES.                             " + Color.ANSI_RESET);
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
	
	public static void configCategories(String defaultPath, Scanner sc) {
		ParameterProduct parameterProduct = null;
		try {
			parameterProduct = new ParameterProduct(defaultPath);
			
			
			int choiceConfig = 0;
			do {
				System.out.println(Color.ANSI_CYAN_BACKGROUND + " CATEGORIAS DE PRODUTOS.                     " + Color.ANSI_RESET);
				System.out.println(" Listar Categorias:.................(1).");
				System.out.println(" Editar Categoria:..................(2).");
				System.out.println(" Adicionar Categoria:...............(3).");
				System.out.println(" Remover Categoria:.................(4).");
				System.out.println(" Voltar:............................(5).");
				System.out.print(" -> ");
				choiceConfig = sc.nextInt();
				sc.nextLine();
				clearScreen();
				switch (choiceConfig) {
				case 1: {
					if(parameterProduct != null) {
						System.out.println(" Categorias[ ");
						for(Category category : parameterProduct.getCategories()) {
							System.out.println(String.format("  Categoria de %d dias.", category.getValidity()));
						}
						System.out.println(" ]");
					}
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();

					break;
				}case 2: {
					break;
				}
				case 3: {
					int validity;
					System.out.println(" Informe o Valor de validade da Categoria: ");
					System.out.print(" -> ");
					validity = sc.nextInt();
					sc.nextLine();
					parameterProduct.createdCategory(new Category(validity, null));
					parameterProduct.salveParameters(defaultPath);
					System.out.println(" Categoria "+validity+ " dias Criada.");
					System.out.println(" Pressione Enter para continuar...");
				    sc.nextLine();
				    clearScreen();

					
					break;
				}
				case 4:{
					if(parameterProduct != null) {
						int choice,i = 0;
						List<Entry<Integer, Integer>> index = new ArrayList<>();
						
						for(Category category : parameterProduct.getCategories()) {
							System.out.println(String.format(" Categoria de %d dias..........(%d)", category.getValidity(), i));
							index.add(new SimpleEntry<>(i, category.getValidity()));
							i++;
						}
						
						System.out.println();
						System.out.println(" Informe o número correspondente\n"
										 + " a categoria que deseja remover.");
						System.out.print(" -> ");
						choice = sc.nextInt();
						sc.nextLine();
						clearScreen();
						int validity = 0;
						
						for (Entry<Integer, Integer> entry : index) {
							int key = entry.getKey();
							int value = entry.getValue();
							if (choice == key) {
								validity = value;
							}
						}
						
						Category cat = null;
						for (Category c : parameterProduct.getCategories()) {
							if (c.getValidity() == validity) {
								cat = c;
							}
						}
						
						
						if(cat != null && parameterProduct.removeCategory(cat)) {
							parameterProduct.salveParameters(defaultPath);
							System.out.println(" Categoria removida com sucesso.");
							System.out.println(" Pressione Enter para continuar...");
						    sc.nextLine();
						    clearScreen();
						}else{
							System.out.println(" Não foi possivel remover a categoria.");
							System.out.println(" Pressione Enter para continuar...");
						    sc.nextLine();
						    clearScreen();
						};
						
						
					}
					break;
				}
				case 5:{
					break;
				}
				default:
					System.out.println(" Opção inválida! " + choiceConfig);
				}
				
			} while (choiceConfig != 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
