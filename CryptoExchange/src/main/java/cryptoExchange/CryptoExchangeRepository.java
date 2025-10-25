package cryptoExchange;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoExchangeRepository extends JpaRepository<CryptoExchangeModel, Long> {

	CryptoExchangeModel findByCryptoFromAndCryptoTo(String cryptoFrom, String cryptoTo);
}