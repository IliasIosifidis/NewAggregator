# Happy Tree News
A news aggregator that collects articles from public APIson **news-ingestor** and distributes them through **RabbitMQ** to independent consumers.

    News APIs → news-ingestor → RabbitMQ (news.articles) → article-store → Frontend
*This is a learning project, not a professional application.*

## News-Ingestor
This is a **Spring-boot** backend that:
- Gathers news from various sources
- Changes their shape to a homogeneous form
- Uses **RabbitMQ** as a producer
- A problematic article will be disabled, not crushing the app
- Optimize for deduplication of articles with **Caffeine** in-memory Database

***Caffeine** has the tradeoff of losing its memory on restart, but it's a trade-off I was willing to make for the shake of efficiency. Redis could be an alternative in case of scaling*  
