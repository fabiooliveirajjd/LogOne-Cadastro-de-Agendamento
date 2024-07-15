package com.teste.pratico;

import com.teste.pratico.entity.Solicitante;
import com.teste.pratico.repository.SolicitanteRepository;
import com.teste.pratico.repository.VagasRepository;
import com.teste.pratico.service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.teste.pratico"})
//@EntityScan(basePackages = {"com.teste.pratico.entity"})
public class TestePraticoApplication implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private VagasRepository vagasRepository;

	@Autowired
	private SolicitanteRepository solicitanteRepository;

	@Autowired
	private SolicitanteService solicitanteService;

	public static void main(String[] args) {
		SpringApplication.run(TestePraticoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("DataSource: " + dataSource.getConnection().getMetaData().getURL());
		System.out.println("Username: " + dataSource.getConnection().getMetaData().getUserName());
		//password
		System.out.println("Password: " + dataSource.getConnection().getMetaData().getUserName());

	}
}
