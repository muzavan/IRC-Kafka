# IRC-Kafka
Simple chat service , experiment using Apache Kafka and Zookeeper

#Created by :
1. Joshua Bezaleel Abednego (13512013)
2. Muhammad Reza Irvanda (13512042)

#Requirements
Java Development Kit 1.8

#Petunjuk instalasi
1. Buka IDE yang dimiliki Netbeans

2. Import project dari src yang ada (source utama terdapat pada direktori src)

3. Jalankan Run dan Build Project

#Testing
1. Mengirimkan message ke salah satu channel dan mengecek apakah user yang tidak terdapat pada channel tersebut mengirimkan pesan

	a. Instance client dapat dijalankan dengan melakukan run file PATChatClient pada package pat.kafka
	
	b. Jalankan tiga instance client
	
	c. Pada instance pertama
	
		/NICK joshua
		
		/JOIN asd
		
	d. Pada instance kedua
	
		/NICK reza
		
		/JOIN asd
		
	c. Pada instance ketiga
	
		/NICK zaky
		
	d. Pada instance pertama
	
		@asd asdasdasd
		
	e. Pada instance kedua, akan ditampilkan message asdasdasd sedangkan pada instance ketiga tidak ada.
	
2. Mengirimkan message setelah LEAVE dari sebuah CHANNEL

	a. Instance client dapat dijalankan dengan melakukan run file PATChatClient pada package pat.kafka
	
	b. Jalankan tiga instance client
	
	c. Pada instance pertama
	
		/NICK joshua
		
		/JOIN asd
		
	d. Pada instance kedua
	
		/NICK reza
		
		/JOIN asd
		
	c. Pada instance pertama
	
		@asd asdasd
		
	d. Pada instance kedua, akan tampil message asdasd
	
	e. Pada instance kedua
	
		/LEAVE asd
		
	f. Pada instance pertama
	
		@asd asdasd
		
	g. Pada instance kedua, message asdasd tidak lagi muncul
	
3. Broadcast pesan ketika channel tidak dispesifikasi

	a. Instance client dapat dijalankan dengan melakukan run file PATChatClient pada package pat.kafka
	
	b. Jalankan tiga instance client
	
	c. Pada instance pertama
	
		/NICK joshua
		
	d. Pada instance kedua
	
		/NICK reza
		
	e. Pada instance pertama
	
		asdasdasd
		
	f. Pada instance kedua, akan tampil message asdasdasd
	
