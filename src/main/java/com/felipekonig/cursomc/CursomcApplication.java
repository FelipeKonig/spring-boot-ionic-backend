package com.felipekonig.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.felipekonig.cursomc.domain.Categoria;
import com.felipekonig.cursomc.domain.Cidade;
import com.felipekonig.cursomc.domain.Estado;
import com.felipekonig.cursomc.domain.Produto;
import com.felipekonig.cursomc.repositories.CategoriaRepository;
import com.felipekonig.cursomc.repositories.CidadeRepository;
import com.felipekonig.cursomc.repositories.EstadoRepository;
import com.felipekonig.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository repoCategoria;

	@Autowired
	private ProdutoRepository repoProduto;

	@Autowired
	private CidadeRepository repoCidade;

	@Autowired
	private EstadoRepository repoEstado;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.0);
		Produto p2 = new Produto(null, "Impressora", 800.0);
		Produto p3 = new Produto(null, "Mouse", 80.0);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().add(p1);

		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().add(cat1);

		repoCategoria.saveAll(Arrays.asList(cat1, cat2));
		repoProduto.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().add(c1);
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		repoEstado.saveAll(Arrays.asList(est1, est2));
		repoCidade.saveAll(Arrays.asList(c1, c2, c3));
	}

}
