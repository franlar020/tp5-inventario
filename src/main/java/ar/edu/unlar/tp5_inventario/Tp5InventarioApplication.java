package ar.edu.unlar.tp5_inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ar.edu.unlar.tp5_inventario.config.StockConfig;

@SpringBootApplication
@EnableConfigurationProperties(StockConfig.class)
public class Tp5InventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp5InventarioApplication.class, args);
	}

}
