package cryptoConversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "api.proxies")
@ComponentScan(basePackages = { "cryptoConversion", "util" })
public class CryptoConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoConversionApplication.class, args);
	}

}