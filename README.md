ВОПЩЕМ НУЖЕН РАБОЧИЙ ДОКЕР

заходите в папку проекта
И пишите
docker compose up -d
Далее работаете 
Ес че
Есть багуля, лучше сразу проверьте
При работающес композе пишете что-то по типу
docker exec -it mirea_llm-postgres-db-1 psql -U postgres -d Mirea_GPT
Внутри пишете SELECT * FROM role;
если там нет значения ROLE_USER
То фигачите INSERT INTO role (id, role_name) VALUES(1, 'ROLE_USER');
Далее молитесь что все сработает и короче выйти из контейнера это exit
ЗАПУСКАЕТСЯ ДОЛГО ПОТОМУ ЧТО ФРОНТ ГОВНО (Я ЛЮБЯ)