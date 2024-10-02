package basico.task.management.service;

import basico.task.management.model.primary.Portfolio;

public interface PortfolioService {
    Portfolio findById(Long portfolioId);
}
