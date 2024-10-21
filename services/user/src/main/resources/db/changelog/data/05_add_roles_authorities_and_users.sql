insert into roles(name)
values ('USER'),
       ('ADMIN');

-- password: qweqwe123
insert into users(username, password, name, surname, enabled)
VALUES ('dosya', '$2a$12$fWUtAFdyYJaym2dO/Etsjecjl9DgajlX7F3WB8fXwZFLd9AoZKz9y', 'Dastan', 'Abrikos', true);
