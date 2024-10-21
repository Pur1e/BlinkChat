
insert into user_roles(user_id, role_id)
SELECT u.id, r.id
FROM roles r,
     users u
WHERE u.username = 'dosya'
  AND r.name = 'ADMIN';