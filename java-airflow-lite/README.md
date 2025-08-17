# java-airflow-lite

Minimal Airflow-like orchestrator skeleton in Java.

--- How to run spring manged version ---
1. Create MySQL DB + user: `CREATE DATABASE airflowlite; CREATE USER 'airflow'@'%' IDENTIFIED BY 'airflow'; GRANT ALL ON airflowlite.* TO 'airflow'@'%';`
2. Update `application.yml` if credentials differ.
3. `mvn spring-boot:run`
4. Check `http://localhost:8080/api/dags` and `POST /api/dags/sample_dag/trigger` to run manually.


## How to build & run
1. Install JDK 17+ and Maven.
2. `mvn package`
3. `java -cp target/java-airflow-lite-0.1.0.jar com.example.orchestrator.AppMain`

Notes:
- This skeleton uses Quartz for scheduling and runs tasks in a thread pool.
- DAGs are YAML files in `src/main/resources/dags/`.

## Next steps / improvements
- Add persistence (Postgres + JPA) for DAG metadata and run/task state.
- Add retry/timeout policies per task.
- Support more task types (shell, HTTP, Java class loader tasks).
- Add a REST API and a small UI (Spring Boot + React).
- Add observability (Micrometer + Prometheus) and centralized logs.