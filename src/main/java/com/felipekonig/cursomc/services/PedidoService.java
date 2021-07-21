package com.felipekonig.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipekonig.cursomc.domain.ItemPedido;
import com.felipekonig.cursomc.domain.PagamentoComBoleto;
import com.felipekonig.cursomc.domain.Pedido;
import com.felipekonig.cursomc.domain.enums.EstadoPagamento;
import com.felipekonig.cursomc.repositories.ItemPedidoRepository;
import com.felipekonig.cursomc.repositories.PagamentoRepository;
import com.felipekonig.cursomc.repositories.PedidoRepository;
import com.felipekonig.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private PagamentoRepository repoPagamento;

	@Autowired
	private ItemPedidoRepository repoItemPedido;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private BoletoService boletoService;

	public Pedido find(Integer id) {
		Optional<Pedido> cat = repo.findById(id);
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {

		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		repoPagamento.save(obj.getPagamento());

		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		repoItemPedido.saveAll(obj.getItens());

		return obj;
	}
}
