![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![PL/pgSQL](https://img.shields.io/badge/PL%2FpgSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liquibase-2962FF?style=for-the-badge&logo=liquibase&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![DBeaver](https://img.shields.io/badge/DBeaver-372923?style=for-the-badge&logo=dbeaver&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![psql](https://img.shields.io/badge/psql-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Bash](https://img.shields.io/badge/Bash-4EAA25?style=for-the-badge&logo=gnubash&logoColor=white)


<h2>Наименование дипломной работы:
«Проектирование и реализация логики запросов для базы данных авиаперевозок на базе PostgreSQL и PL/pgSQL»</h2>

| Логин  |Пароль | 
| :---:  | :---: | 

Видеопрезентация: https://disk.yandex.ru/i/2f25iC6lmLAFFQ <br>
Презентация PowerPoint: https://github.com/SergeiAidinov/pg_diploma/blob/master/src/main/resources/Presentation.pptx <br>
Презентация PDF: https://github.com/SergeiAidinov/pg_diploma/blob/master/src/main/resources/Presentation.pdf <br>

<h3>Задачи, выполняемые в рамках работы:</h3>
<ol>
<li> Разработка функций на языке PL/pgSQL для автоматизации поиска сведений по заданным поисковым критериям.</li>
<li> Оптимизация SQL-запросов и процедур с использованием индексов и анализом планов выполнения запросов.</li>
<li> Проведение тестирования и отладки функций, включая обработку исключительных ситуаций (слишком много данных в выборке и т.д.).</li>
<li> Разработка простого бэк - приложения на языке программирования Java для вызова процедур и отображения результатов.</li>
<li> Подготовка презентации и демонстрация работы системы на тестовых данных.</li>
</ol>

---

###### Deploy DB:

###### sergei@debian:/$ psql -U sergei -d postgres -a -f "/opt/Demo_pgsql/demo-big-20170815.sql"

###### DB size:

###### SELECT pg_size_pretty( pg_database_size( 'demo' ) );

###### Подключение к базе: PGPASSWORD=postgres psql -h localhost -U postgres -d demo
