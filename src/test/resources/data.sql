insert into medicine (name, quantity, price) values
        ('Ibuprofen', 10, 8.0),
        ('Nurofen', 20, 7.5);

insert into "user" (full_name, email_address, password, address, role) values
        ('User', 'user@example.com', '12345', 'City1', 'USER'),
        ('Admin', 'admin@example.com', '12345', 'City2', 'ADMIN');

insert into "order" ("user_id", created, status) values
        (1, now(), 'PENDING'),
        (2, now(), 'DELIVERED');

insert into order_item values
        (5, 1, 1),
        (10, 2, 1),
        (5, 1, 2),
        (10, 2, 2)