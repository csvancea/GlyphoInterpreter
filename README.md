# Glypho Interpreter - Cosmin-Razvan VANCEA - 333CA


Structura si implementare:
--------------------------
* Codul este impartit in 3 pachete:
  1. decoder (decodifica un sir de glife)
  2. executor (executa instructiunile decodificate)
  3. main (citeste si valideaza argumentele primite de la linia de comanda)

* Pachetul `decoder`:
  - are rolul de a traduce un sir de glife intr-o lista de instructiuni
  - o instructiune decodificata este formata din:
    - cod de operatie (opcode)
    - jump index (optional; se aplica doar operatiilor care efectueaza salturi (*BRACE))
  - decodificarea se desfasoara astfel:
    - se verifica daca numarul de glife este divizibil cu 4
    - se citesc blocuri de cate 4 simboluri si se determina codul de operatie
    - se genereaza instructiunea decodificata si se adauga in lista
    - in plus: exista o stiva care memoreaza adresele la care s-au gasit instructiuni
    LBRACE pentru a se putea face asocierea LBRACE-RBRACE (balanced brackets)
    - daca la orice pas se detecteaza o eroare de sintaxa, atunci se arunca exceptia
    `SyntaxException` care indica indexul instructiunii si o scurta descriere a
    problemei (descrierea este doar pentru debugging)
  - la finalul traducerii va rezulta o lista de instructiuni in formatul prezentat mai sus

* Pachetul `executor`:
  - se comporta ca un procesor - are rolul de a executa un sir de instructiuni decodificate
  - clasa `Executor` are in componenta:
    - o lista de instructiuni decodificate (primita de la Decoder)
    - un "registru" care indica indexul instructiunii care trebuie executata la pasul curent
    - o stiva pentru simboluri (implementata printr-o lista dublu-inlantuita
    deoarece instructiunile lucreaza cu ambele capete ale "stivei")
    - o functie care "implementeaza" codul din spatele fiecarui opcode
  - daca la orice pas se detecteaza o eroare de runtime, atunci se arunca exceptia
  `RuntimeException` care indica indexul instructiunii si o scurta descriere a
  problemei (descrierea este doar pentru debugging)

* Pachetul `main`:
  - imbina pachetele `decoder` si `executor`
  - prinde exceptiile aruncate si afiseaza erorile corespunzator la stderr
