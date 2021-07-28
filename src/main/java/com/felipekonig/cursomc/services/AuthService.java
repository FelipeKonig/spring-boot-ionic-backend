package com.felipekonig.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipekonig.cursomc.domain.Cliente;
import com.felipekonig.cursomc.repositories.ClienteRepository;
import com.felipekonig.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailService emailService;

	private Random random = new Random();

	public void sendNewPassword(String email) {

		Cliente cliente = repo.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		repo.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {

		char vet[] = new char[10];

		for (int i = 0; i < vet.length; i++) {
			vet[i] = randomchar();
		}

		return new String(vet);
	}

	private char randomchar() {

		int opt = random.nextInt(3);

		if (opt == 0) { // gera um digito
			return (char) (random.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (random.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}
	}
}