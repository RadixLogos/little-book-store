INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Do liberalismo a apostasia','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Carta aberta aos católicos perplexos','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Vendeia, uma família de bandidos','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Rosa Hu','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Iota Unum','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('A Igreja católica e a outra','Permanência',10,45);

INSERT INTO tb_client(name,cellphone) VALUES('Nathan','4199557171');
INSERT INTO tb_client(name,cellphone) VALUES('Rubens','4199557333');
INSERT INTO tb_client(name,cellphone) VALUES('Jamile','4396557333');

INSERT INTO tb_buy_order (payment_type,client_id,order_date,total) VALUES (1,1,TIMESTAMP WITH TIME ZONE '2025-09-12T10:00:00Z',90);
INSERT INTO tb_order_book (quantity,sold_value,sub_total,book_id,buy_order_id) values(2,45,90,1,1);

