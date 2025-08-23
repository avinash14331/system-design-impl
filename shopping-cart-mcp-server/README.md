# Shopping Cart MCP (Microservice Cart Platform)

[![Java](https://img.shields.io/badge/Java-24-brightgreen)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Latest-green)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Latest-blue)](https://spring.io/projects/spring-ai)
[![MongoDB](https://img.shields.io/badge/MongoDB-Latest-success)](https://www.mongodb.com/)

## Overview
Shopping Cart MCP is an advanced microservice that provides comprehensive shopping cart management with AI-powered capabilities. Built with Spring Boot and MongoDB, it offers a robust and scalable solution for e-commerce platforms.

## Key Features
- 🛒 Full shopping cart management
- 🤖 AI-powered tools integration
- 📊 Real-time cart calculations
- 🔄 Seamless MongoDB persistence
- 🔌 RESTful API endpoints
- 🎯 Spring AI tools integration

## Tech Stack
- **Java 24**: Latest LTS version for optimal performance
- **Spring Boot**: Core framework for microservice architecture
- **Spring Data MongoDB**: Database integration and operations
- **Spring AI**: AI tools and capabilities integration
- **Jakarta EE**: Enterprise Java components
- **Lombok**: Reduces boilerplate code
- **Maven**: Dependency management and build tool

## Prerequisites
- JDK 24
- MongoDB 7.x+
- Maven 3.8+
- Docker (optional, for containerization)

## Quick Start

### 1. Clone Repository### 2. Configure MongoDB
Update `application.properties`:### 3. Build & Run## API Documentation

### Cart Operations

#### Add Item to Cart#### Remove Item from Cart#### Get Cart Contents#### Calculate Cart Total## Project Structure## Key Components

### ShoppingCartMCPService
Core service managing cart operations with AI integration:
- Product addition/removal
- Cart state management
- Total calculations
- AI-powered recommendations

### CartItemRepository
MongoDB repository interface handling:
- CRUD operations
- Custom queries
- Data persistence

## Configuration Properties## Development

### Running Tests### Code Style
This project follows Google Java Style Guide. Configure your IDE accordingly:
- Use 4 spaces for indentation
- Line length limit: 100 characters
- UTF-8 file encoding

## Docker Support

### Build Image### Run Container## Monitoring

The application exposes several endpoints for monitoring:
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
- **Developer**: [Your Name]
- **Email**: [your.email@example.com]
- **Project Link**: https://github.com/yourusername/shopping-cart-mcp

## Acknowledgments
- Spring Framework Team
- MongoDB Team
- Spring AI Community
- All contributors

---
*Made with ❤️ by [Your Name/Organization]*