package basico.task.management.service.impl;

import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Portfolio;
import basico.task.management.repository.primary.PortfolioRepository;
import basico.task.management.service.PortfolioService;
import org.springframework.stereotype.Service;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public Portfolio findById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElseThrow(() -> new NotFoundException("portfolio.not.found"));
    }
}
