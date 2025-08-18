package currencyExchange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchangeModel, Integer>{
	
	CurrencyExchangeModel findByFromAndTo(String from, String to);
}
