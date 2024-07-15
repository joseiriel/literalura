package com.github.joseiriel.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

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
				new Action("Buscar livro", this::searchBooks),
				new Action("Listar livros buscados", this::listBooks),
				new Action("Listar buscados por linguagem", this::listBooksByLanguage),
				new Action("Listar autores", this::listAuthors),
				new Action("Listar autores vivos em ano", this::listAuthorsAliveAtYear),
				new Action("Sair", () -> System.exit(0))
			});
			}
	}

	void searchBooks() {
		System.out.print("Digite a sua consulta: ");
		var query = scanner.nextLine();
		var books = api.searchBooks(query);
		books.stream().limit(1).forEach(System.out::println);
		try {
			var book = books.get(0);
//			searchedBooks.add(book);
			repo.save(book);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Livro não encontrado.");
		}
	}

	void listBooks() {
		var books = repo.findAll();
		if (books.isEmpty()) {
			System.out.println("Nenhum livro foi buscado ainda.");
		}
		System.out.println("Livros buscados: ");
		books.forEach(System.out::println);
	}

	void listBooksByLanguage() {
		System.out.print("Digite a linguagem que quer buscar (ex.: en, pt): ");
		var lang = scanner.nextLine();
		searchedBooks.stream().filter(book -> book.languages.contains(lang)).forEach(System.out::println);
	}

	void listAuthors() {
		searchedBooks.stream().filter(book -> book.author.isPresent()).forEach(book -> System.out.println(book.author.orElseThrow()));
	}

	void listAuthorsAliveAtYear() {
		var maybeYear = OptionalInt.empty();
		while (maybeYear.isEmpty()) {
			System.out.print("Digite o ano que quer consultar: ");
			var input = scanner.nextLine();
			try {
				maybeYear = OptionalInt.of(Integer.parseInt(input));
			} catch (NumberFormatException e) {
				System.err.println("Ano inválido, tente novamente.");
			}
		}
		var year = maybeYear.orElseThrow();
		searchedBooks.stream().filter(book -> {
			if (book.author.isEmpty()) return false;
			var author = book.author.get();
			if (author.birthYear() == null || author.deathYear() == null) return false;
			var birthYear = author.birthYear();
			var deathYear = author.deathYear();
			return (birthYear <= year && deathYear >= year);
		}).forEach(book -> System.out.println(book.author.orElseThrow()));
	}

	record Action(String description, Runnable f) {}
    static void choiceMenu(String message, Action[] actions) {
		Optional<Action> choice = Optional.empty();
		while (choice.isEmpty()) {
			for (int i = 0; i < actions.length; i++) {
				System.out.printf("%d: %s\n", i+1, actions[i].description);
			}
			System.out.print(message);
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
