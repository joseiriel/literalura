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
	BookRepository bookRepo;
	@Autowired
	AuthorRepository authorRepo;

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

	void saveBook(Book book) {
		var existingBook = bookRepo.findByTitle(book.title);
		if (existingBook == null) {
			var author = book.author();
			var existingAuthor = authorRepo.findByName(author.name());
			if (existingAuthor.isEmpty()) {
				var savedAuthor = authorRepo.save(author);
				book.setAuthor(savedAuthor);
				savedAuthor.addBook(book);
			} else {
				var savedAuthor = existingAuthor.get();
				book.setAuthor(savedAuthor);
				savedAuthor.addBook(book);
			}
			bookRepo.save(book);
		}
	}

	void searchBooks() {
		System.out.print("Digite a sua consulta: ");
		var query = scanner.nextLine();
		var booksData = api.searchBooks(query);
		try {
			var bookData = booksData.get(0);
			var book = new Book(bookData);
			System.out.println(book);
			saveBook(book);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Livro não encontrado.");
		}
	}

	void listBooks() {
		var books = bookRepo.findAll();
		if (books.isEmpty()) {
			System.out.println("Nenhum livro foi buscado ainda.");
			return;
		}
		System.out.println("Livros buscados: ");
		books.stream().sorted(Comparator.comparing(book -> book.title)).forEach(System.out::println);
	}

	void listBooksByLanguage() {
		System.out.print("Digite a linguagem que quer buscar (ex.: en, pt): ");
		var lang = scanner.nextLine();
		var list = bookRepo.findAll().stream().filter(book -> book.languages.contains(lang)).toList();
		if (list.isEmpty()) {
			System.out.println("Nenhum livro nessa linguagem encontrado.");
		} else {
			list.forEach(System.out::println);
		}
	}

	void listAuthors() {
		var list = authorRepo.findAll();
		list.sort(Comparator.comparing(Author::name));
		if (list.isEmpty()) {
			System.out.println("Nenhum autor encontrado.");
		} else {
			list.forEach(System.out::println);
		}
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
		var authors = authorRepo.findAliveAtYear(year);
		if (authors.isEmpty()) {
			System.out.println("Nenhum autor vivo nesse ano foi buscado ainda.");
		} else {
			authors.forEach(System.out::println);
		}
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
