INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Do liberalismo a apostasia','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Carta aberta aos católicos perplexos','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Vendeia, uma família de bandidos','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Rosa Hu','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('Iota Unum','Permanência',10,45);
INSERT INTO tb_books (name,editor,stock_Quantity,price) values ('A Igreja católica e a outra','Permanência',10,45);

INSERT INTO tb_client(id,name,cellphone) VALUES(1,'Nathan','4199557171');

INSERT INTO tb_buy_order (id,payment_type,client_id,total) VALUES (1,'PIX',1,90);
INSERT INTO tb_order_book (quantity,sold_value,sub_total,book_id,buy_order_id) values(2,45,90,1,1);