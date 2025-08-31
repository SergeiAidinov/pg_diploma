![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Debian](https://img.shields.io/badge/Debian-D70A53?style=for-the-badge&logo=debian&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

Наименование дипломной работы:
«Проектирование и реализация логики запросов для базы данных авиаперевозок на базе PostgreSQL и PL/pgSQL»

Видеопрезентация: https://disk.yandex.ru/i/2f25iC6lmLAFFQ
Презентация PowerPoint: https://github.com/SergeiAidinov/pg_diploma/blob/master/src/main/resources/Presentation.pptx

Задачи, выполняемые в рамках работы:
1. Разработка функций на языке PL/pgSQL для автоматизации поиска сведений по заданным поисковым критериям.
2. Оптимизация SQL-запросов и процедур с использованием индексов и анализом планов выполнения запросов.
3. Проведение тестирования и отладки функций, включая обработку исключительных ситуаций (слишком много данных в выборке и т.д.).
4. Разработка простого бэк - приложения на языке программирования Java для вызова процедур и отображения результатов.
5. Подготовка презентации и демонстрация работы системы на тестовых данных.

Deploy DB:

sergei@debian:/$ psql -U sergei -d postgres -a -f "/opt/Demo_pgsql/demo-big-20170815.sql"

DB size:

SELECT pg_size_pretty( pg_database_size( 'demo' ) );

Подключение к базе: PGPASSWORD=postgres psql -h localhost -U postgres -d demo
