package tradeService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TradeExchangeRepository extends JpaRepository<TradeExchangeModel, Long> {

	@Query("SELECT t FROM TradeExchangeModel t WHERE t.fiatCurrency = ?1 AND t.cryptoCurrency = ?2")
	Optional<TradeExchangeModel> findByFiatAndCrypto(String fiatCurrency, String cryptoCurrency);
}