package com.github.joseiriel.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	static ApiHelper api = new ApiHelper();
	static Scanner scanner = new Scanner(System.in);

	@Autowired
	BookRepository repo;
	List<Book> searchedBooks = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Bem vindo ao Literalura.");

		for (;;) {
			choiceMenu("Escolha uma opção: ", new Action[]{
				new Action("Buscar livros", this::searchBooks),
				new Action("Listar livros buscados", this::listBooks),
				new Action("Sair", () -> System.exit(0))
			});
			}
	}

	void searchBooks() {
		System.out.print("Digite a sua consulta: ");
		var query = scanner.nextLine();
		var books = api.searchBooks(query);
		books.results().stream().limit(1).forEach(System.out::println);
		searchedBooks.add(books.results().get(0));
	}

	void listBooks() {
		System.out.println("Livros buscados: ");
		searchedBooks.forEach(System.out::println);
	}

	record Action(String description, Runnable f) {}

    static void choiceMenu(String message, Action[] actions) {
		for (int i = 0; i < actions.length; i++) {
			System.out.printf("%d: %s\n", i+1, actions[i].description);
		}
		System.out.print(message);

		Optional<Action> choice = Optional.empty();
		while (choice.isEmpty()) {
			var entrada = scanner.nextLine();
			try {
				var numero = Integer.parseInt(entrada);
				choice = Optional.of(actions[numero-1]);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.err.println("Escolha inválida, tente novamente.");
			}
		}

		choice.orElseThrow().f.run();
	}
}
